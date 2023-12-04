package com.example.smartfood.SupplierActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Outline;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.smartfood.MainActivity;
import com.example.smartfood.Models.Order;
import com.example.smartfood.Models.StoredDish;
import com.example.smartfood.R;
import com.example.smartfood.UserActivities.RecommendationsActivity;
import com.example.smartfood.services.AuthService;
import com.example.smartfood.services.StoredDishService;
import com.example.smartfood.services.SupplierService;

import java.util.List;
import java.util.UUID;

public class SupplierHomeActivity extends AppCompatActivity {
    private ImageView logoutBtn;
    private TextView noSuppliesNeededText;
    List<StoredDish> storedDishesList;
    SupplierService supplierService;
    StoredDishService storedDishService;
    private LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supplier_home);

        logoutBtn = (ImageView) findViewById(R.id.logout_btn);

        noSuppliesNeededText = (TextView) findViewById(R.id.no_supplies_needed);
        layout = (LinearLayout) findViewById(R.id.orders_layout);

        outputNeededSupplies(AuthService.getInstance(SupplierHomeActivity.this).getUser().getId());

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AuthService.getInstance(SupplierHomeActivity.this).logout();
                Intent main = new Intent(SupplierHomeActivity.this, MainActivity.class);
                startActivity(main);
                finish();
            }
        });
    }

    private void outputNeededSupplies(UUID userId) {
        UUID supplierId = new SupplierService(this).getSupplierCompany(userId).getId();
        storedDishService = new StoredDishService(this);
        storedDishesList = storedDishService.getSuppliersEmptyStoredDishes(supplierId);
        
        insertToLayout(storedDishesList);
    }

    private void insertToLayout(List<StoredDish> storedDishesList) {
        if(storedDishesList==null || storedDishesList.size() == 0){
            noSuppliesNeededText.setVisibility(View.VISIBLE);
            return;
        }

        for (StoredDish storedDish : storedDishesList) {
            TextView textView = new TextView(this);

            textView.setText("\uD83D\uDD34" + storedDish.toStringForSupplier());
            textView.setTag(storedDish.getId().toString());

            setDesign(textView);

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String storedDishId = textView.getTag().toString();
                    String title = getString(R.string.supply_title);
                    String description = storedDish.toStringForSupplier()
                            + "\n" + getResources().getString(R.string.fridge_placement)
                            + ": " + storedDish.getFridge().getPlacementDescription();

                    AlertDialog.Builder builder = new AlertDialog.Builder(SupplierHomeActivity.this);
                    builder.setTitle(title);
                    builder.setMessage(description);

                    builder.setPositiveButton(getResources().getString(R.string.make_delivery_report) + " \u2705", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MakeDeliveryReport(storedDishId);
                        }
                    });

                    builder.setNeutralButton(getResources().getString(R.string.close), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });

            layout.addView(textView);
        }
    }

    private void MakeDeliveryReport(String storedDishId) {
    }

    private void setDesign(TextView textView)
    {
        textView.setBackgroundResource(R.color.ivory);
        textView.setPadding(20, 10, 20, 10);
        Typeface font = ResourcesCompat.getFont(this, R.font.comfortaa);
        textView.setTypeface(font);
        textView.setTextSize(22);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(20, 20, 20, 0);
        textView.setLayoutParams(params);
        textView.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                int radius = 15;
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), radius);
            }
        });
        textView.setClipToOutline(true);
    }
}