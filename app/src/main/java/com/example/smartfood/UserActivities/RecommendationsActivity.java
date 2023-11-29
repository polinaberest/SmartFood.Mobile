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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartfood.Constants.Constants;
import com.example.smartfood.Constants.ODataEndpointsNames;
import com.example.smartfood.Constants.Status;
import com.example.smartfood.MainActivity;
import com.example.smartfood.Models.Order;
import com.example.smartfood.R;
import com.example.smartfood.Utils.MyDateDeserializer;
import com.example.smartfood.services.AuthService;
import com.example.smartfood.services.HttpClientFactory;
import com.example.smartfood.services.ODataService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RecommendationsActivity extends AppCompatActivity {

    private ImageView backBtn;
    private ArrayList<Order> recommendationsList;
    private TextView noRecommendationsText;
    private LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendations);

        backBtn = (ImageView) findViewById(R.id.back_btn);
        noRecommendationsText = (TextView) findViewById(R.id.no_recommendations_received);
        layout = (LinearLayout) findViewById(R.id.recommendations_layout);

        outputRecommendations(AuthService.getInstance(RecommendationsActivity.this).getUser().getId());

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent home = new Intent(RecommendationsActivity.this, UserHomeActivity.class);
                startActivity(home);
                finish();
            }
        });
    }

    private void outputRecommendations(UUID id) {
        recommendationsList = (ArrayList<Order>) getUserRecommendations(id);
        InsertToLayout(recommendationsList);
    }

    private void InsertToLayout(ArrayList<Order> recommendations) {
        if(recommendations==null || recommendations.size() == 0){
            noRecommendationsText.setVisibility(View.VISIBLE);
            return;
        }

        for (Order order : recommendations) {
            TextView textView = new TextView(this);

            textView.setText("\uD83D\uDC9C" + order.toString());
            textView.setTag(order.getId().toString());

            setDesign(textView);

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String orderId = textView.getTag().toString();
                    String title = getString(R.string.recommendation_title);
                    String description = makeFullDescription(order);

                    AlertDialog.Builder builder = new AlertDialog.Builder(RecommendationsActivity.this);
                    builder.setTitle(title);
                    builder.setMessage(description);

                    builder.setPositiveButton(getResources().getString(R.string.repeat_order) + " \u2705", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            RepeatOrder(order);
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

    private void RepeatOrder(Order order) {
        ODataService<Order> t = new ODataService<Order>(Order.class, this);
        Order newOrder = new Order(order.getCustomerId(),
                                   order.getCount(),
                                   order.getOrderedDishId(),
                                   order.getTotalPrice());
        try {
            t.create(ODataEndpointsNames.ORDERS, newOrder);
            Toast.makeText(getApplicationContext(),
                  getResources().getString(R.string.succesful_order) +
                  getResources().getString(R.string.pick_up_in_fridge) +
                  order.getOrderedDish().getFridge().getPlacementDescription(), Toast.LENGTH_SHORT).show();
            restartActivity();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.failed_order), Toast.LENGTH_SHORT).show();
        }
    }

    private void setDesign(TextView textView)
    {
        textView.setBackgroundResource(R.color.green);
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

    private String makeFullDescription(Order order) {
        return getResources().getString(R.string.about_order) + ": " +
                order.toString() + "\n\n";
    }

    private List<Order> getUserRecommendations(UUID id) {
        String url = Constants.BASE_URL + "/recommended-orders/" + id;
        OkHttpClient client = HttpClientFactory.getInstance(this).getHttpClient();
        Gson gson = new GsonBuilder().setLenient().registerTypeAdapter(Date.class, new MyDateDeserializer()).create();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        Response response = null;

        try {
            response = client.newCall(request).execute();

            if (response.isSuccessful()) {
                String responseBody = response.body().string(); // тут все ок - шо дальше
                return parseList(responseBody, gson);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    private List<Order> parseList(String responseBody, Gson gson) {
        Type listType = new ParameterizedType() {
            public Type[] getActualTypeArguments() {
                return new Type[]{Order.class};
            }

            public Type getRawType() {
                return List.class;
            }

            public Type getOwnerType() {
                return null;
            }
        };

        return gson.fromJson(responseBody, listType);
    }

    private void restartActivity() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}