package com.example.smartfood.services;

import android.content.Context;

import com.example.smartfood.Constants.ODataEndpointsNames;
import com.example.smartfood.Models.StoredDish;
import com.example.smartfood.Models.Supplier;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SupplierService {
    private Context context;

    public SupplierService(Context сontext) {
        this.context = сontext;
    }

    public Supplier getSupplierCompany(UUID userId) {
        Supplier supplier = null;
        ODataService<Supplier> t = new ODataService<Supplier>(Supplier.class, context);
        ODataQueryBuilder builder = new ODataQueryBuilder();
        builder.filter("ManagerId eq " + userId.toString());
        try {
            supplier = t.getAll(ODataEndpointsNames.SUPPLIERS, builder).get(0);
        } catch (IOException e) {
        }

        return supplier;
    }
}
