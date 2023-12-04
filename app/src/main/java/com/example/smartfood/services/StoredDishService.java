package com.example.smartfood.services;

import android.content.Context;

import com.example.smartfood.Constants.ODataEndpointsNames;
import com.example.smartfood.Models.Filial;
import com.example.smartfood.Models.StoredDish;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class StoredDishService {
    private Context context;

    public StoredDishService(Context сontext) {
        this.context = сontext;
    }

    public List<StoredDish> getFilialStoredDishes(UUID filialId) {
        List<StoredDish> storedDishes = new ArrayList<>();
        ODataService<StoredDish> t = new ODataService<StoredDish>(StoredDish.class, context);
        ODataQueryBuilder builder = new ODataQueryBuilder();
        builder.expand("Dish, Fridge");
        builder.filter("Fridge/FilialId eq " + filialId.toString());
        try {
            storedDishes = t.getAll(ODataEndpointsNames.STORED_DISHES, builder);
        } catch (IOException e) {
        }

        return storedDishes;
    }

    public List<StoredDish> getSuppliersEmptyStoredDishes(UUID supplierId) {
        List<StoredDish> storedDishes = new ArrayList<>();
        ODataService<StoredDish> t = new ODataService<StoredDish>(StoredDish.class, context);
        ODataQueryBuilder builder = new ODataQueryBuilder();
        builder.expand("Dish($expand=Supplier), Fridge($expand=Filial)");
        builder.filter("Dish/SupplierId eq " + supplierId.toString()
                + " and CountAvailable eq 0" );
        try {
            storedDishes = t.getAll(ODataEndpointsNames.STORED_DISHES, builder);
        } catch (IOException e) {
        }

        return storedDishes;
    }
}
