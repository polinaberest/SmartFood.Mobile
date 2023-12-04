package com.example.smartfood.services;

import android.content.Context;

import com.example.smartfood.Constants.ODataEndpointsNames;
import com.example.smartfood.Models.Filial;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FilialService {
    private Context context;

    public FilialService(Context сontext) {
        this.context = сontext;
    }
    public List<Filial> getFilials() {
        List<Filial> filials = new ArrayList<>();
        ODataService<Filial> t = new ODataService<Filial>(Filial.class, context);
        ODataQueryBuilder builder = new ODataQueryBuilder();
        builder.expand("OwnerOrganization");
        try {
            filials = t.getAll(ODataEndpointsNames.FILIALS, builder);
        } catch (IOException e) {
        }

        return filials;
    }
}
