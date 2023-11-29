package com.example.smartfood.UserActivities;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartfood.Constants.ODataEndpointsNames;
import com.example.smartfood.Constants.Status;
import com.example.smartfood.MainActivity;
import com.example.smartfood.Models.Dish;
import com.example.smartfood.Models.Order;
import com.example.smartfood.R;
import com.example.smartfood.RequestModels.LoginRequest;
import com.example.smartfood.services.AuthService;
import com.example.smartfood.services.ODataQueryBuilder;
import com.example.smartfood.services.ODataService;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class UserHomeActivity extends AppCompatActivity {

    private ImageView logoutBtn, makeOrderBtn, getRecommedationsBtn;
    private TextView noOrdersText;
    List<Order> orderList;
    private LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

/*        ODataService<Dish> t = new ODataService<Dish>(Dish.class, this);
        UUID id = UUID.fromString("32654250-2592-4B00-047A-08DBD632DAB0");

        ODataQueryBuilder builder = new ODataQueryBuilder();
        builder.expand("Supplier");
        List<Dish> res;
        try {
            res = t.getAll(ODataEndpointsNames.DISHES, builder);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }*/

        logoutBtn = (ImageView) findViewById(R.id.logout_btn);
        makeOrderBtn = (ImageView) findViewById(R.id.make_order_btn);
        getRecommedationsBtn = (ImageView) findViewById(R.id.recommendations_btn);

        noOrdersText = (TextView) findViewById(R.id.no_orders_made);
        layout = (LinearLayout) findViewById(R.id.orders_layout);

        outputOrders(AuthService.getInstance(UserHomeActivity.this).getUser().getId());

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AuthService.getInstance(UserHomeActivity.this).logout();
                Intent main = new Intent(UserHomeActivity.this, MainActivity.class);
                startActivity(main);
                finish();
            }
        });

        getRecommedationsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent recommendations = new Intent(UserHomeActivity.this, RecommendationsActivity.class);
                startActivity(recommendations);
                finish();
            }
        });

        makeOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent order = new Intent(UserHomeActivity.this, MakeOrderActivity.class);
                startActivity(order);
                finish();
            }
        });
    }

    private void outputOrders(UUID id) {
        orderList = getUserOrders(id);

        InsertToLayout(orderList);
    }

    private List<Order> getUserOrders(UUID id) {
        ODataService<Order> t = new ODataService<Order>(Order.class, this);

        ODataQueryBuilder builder = new ODataQueryBuilder();
        builder.filter("CustomerId eq " + id.toString());
        builder.expand("OrderedDish($expand=Fridge, Dish)");
        List<Order> res;
        try {
            res = t.getAll(ODataEndpointsNames.ORDERS, builder);
        } catch (IOException e) {
            res = null;
        }

        return res;
    }

    private void fulfillOrder(Order order) {
        ODataService<Order> t = new ODataService<Order>(Order.class, this);
        order.setStatus(Status.Fulfilled);
        try {
            t.update(ODataEndpointsNames.ORDERS, order.getId().toString(), order);
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.succesful_dish_pick_up), Toast.LENGTH_SHORT).show();
            restartActivity();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.failed_dish_pick_up), Toast.LENGTH_SHORT).show();
        }
    }

    private void InsertToLayout(List<Order> orderList) {
        if(orderList==null || orderList.size() == 0){
            noOrdersText.setVisibility(View.VISIBLE);
            return;
        }

        Collections.reverse(orderList);

        for (Order order : orderList) {
            TextView textView = new TextView(this);

            textView.setText(order.toString());
            textView.setTag(order.getId().toString());

            setDesign(textView, order.getStatus());

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String orderId = textView.getTag().toString();
                    String title = getString(R.string.order_title);
                    String description = makeFullDescription(order);

                    AlertDialog.Builder builder = new AlertDialog.Builder(UserHomeActivity.this);
                    builder.setTitle(title);
                    builder.setMessage(description);

                    if(!order.getStatus().equals(Status.Fulfilled))
                    {
                        builder.setPositiveButton(getResources().getString(R.string.pick_up) + " \u2705", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                fulfillOrder(order);
                            }
                        });
                    }
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

    private String makeFullDescription(Order order) {
        return getResources().getString(R.string.about_order) + ": " +
               order.toString() + "\n\n" +
               getResources().getString(R.string.order_status) + " " + order.getStatus();
    }

    private void setDesign(TextView textView, Status oderStatus)
    {
        textView.setBackgroundResource(getOrderColor(oderStatus));
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

    private int getOrderColor(Status orderStatus)
    {
        return orderStatus==Status.Approved ? R.color.purple_200 : R.color.ivory;
    }

    private void restartActivity() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}