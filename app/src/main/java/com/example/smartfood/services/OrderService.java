package com.example.smartfood.services;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.smartfood.Constants.ODataEndpointsNames;
import com.example.smartfood.Models.Filial;
import com.example.smartfood.Models.Order;
import com.example.smartfood.Models.StoredDish;
import com.example.smartfood.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class OrderService {
    private Context context;

    public OrderService(Context сontext) {
        this.context = сontext;
    }


    public void seedFilials(Spinner filialsSpinner) {
        FilialService filialService = new FilialService(context);
        List<Filial> filials = filialService.getFilials();

        if (filials != null) {
            ArrayAdapter<Filial> adapter =
                    new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, filials);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            filialsSpinner.setAdapter(adapter);
        }
    }

    public void seedDishes(Spinner dishesSpinner, UUID filialId) {
        StoredDishService storedDishService = new StoredDishService(context);
        List<StoredDish> storedDishes = storedDishService.getFilialStoredDishes(filialId);

        if (storedDishes != null) {
            ArrayAdapter<StoredDish> adapter =
                    new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, storedDishes);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dishesSpinner.setAdapter(adapter);
        }
    }

}
