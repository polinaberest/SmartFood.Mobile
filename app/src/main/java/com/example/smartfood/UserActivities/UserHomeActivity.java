package com.example.smartfood.UserActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.smartfood.Constants.ODataEndpointsNames;
import com.example.smartfood.Models.Dish;
import com.example.smartfood.R;
import com.example.smartfood.services.ODataQueryBuilder;
import com.example.smartfood.services.ODataService;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class UserHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        ODataService<Dish> t = new ODataService<Dish>(Dish.class, this);
        UUID id = UUID.fromString("32654250-2592-4B00-047A-08DBD632DAB0");

        ODataQueryBuilder builder = new ODataQueryBuilder();
        builder.expand("Supplier");
        List<Dish> res;
        try {
            res = t.getAll(ODataEndpointsNames.DISHES, builder);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}