package com.example.smartfood.UserActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartfood.Constants.ODataEndpointsNames;
import com.example.smartfood.Models.Filial;
import com.example.smartfood.Models.Order;
import com.example.smartfood.Models.StoredDish;
import com.example.smartfood.R;
import com.example.smartfood.Utils.InputFilterMinMax;
import com.example.smartfood.services.AuthService;
import com.example.smartfood.services.ODataService;
import com.example.smartfood.services.OrderService;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.UUID;

public class MakeOrderActivity extends AppCompatActivity {
    private OrderService orderService;
    private Filial selectedFilial;

    private EditText countInput;
    private TextView outputTotal;
    private Spinner filialsSpinner, dishesSpinner;
    private Button makeOrderBtn;
    private ImageView backBtn;

    private UUID customerId, orderedDishId;
    private int count = 1;
    private double price, totalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_order);
        customerId = AuthService.getInstance(MakeOrderActivity.this).getUser().getId();

        initLayoutElements();
        initSpinners();

        makeOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Order newOrder = getNewOrder();
                    placeOrder(newOrder);
                    returnHome();
                }
                catch (Exception e) {
                    Toast.makeText(MakeOrderActivity.this, getResources().getString(R.string.failed_order), Toast.LENGTH_SHORT).show();
                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnHome();
            }
        });

        countInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                try{
                    count = Integer.parseInt(countInput.getText().toString());
                }
                catch (Exception ex)
                {
                    count = 0;
                }

                setTotalOutput(price);
            }
        });
    }

    @NonNull
    private Order getNewOrder() {
        count = Integer.parseInt(countInput.getText().toString());
        return new Order(customerId, count, orderedDishId, count*price);
    }

    private void initLayoutElements() {
        filialsSpinner = (Spinner) findViewById(R.id.filials_spinner);
        dishesSpinner = (Spinner) findViewById(R.id.dishes_spinner);
        countInput = (EditText) findViewById(R.id.order_count);
        countInput.setFilters(getFilters("1", "10"));
        outputTotal = (TextView) findViewById(R.id.order_total);
        makeOrderBtn = (Button) findViewById(R.id.make_order_btn);
        backBtn = (ImageView) findViewById(R.id.back_home_btn);
    }

    private void initSpinners() {
        orderService = new OrderService(this);
        orderService.seedFilials(filialsSpinner);

        filialsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedFilial = (Filial) parent.getItemAtPosition(position);
                orderService.seedDishes(dishesSpinner, selectedFilial.getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedFilial = null;
            }
        });

        dishesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                orderedDishId = ((StoredDish) parent.getItemAtPosition(position)).getId();
                price = ((StoredDish) parent.getItemAtPosition(position)).getDish().getPrice();
                countInput.setFilters(getFilters("1", 
                        String.valueOf(((StoredDish) parent.getItemAtPosition(position)).getCountAvailable())));
                setTotalOutput(price);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                setTotalOutput(0);
            }
        });
    }

    private void setTotalOutput(double price) {
        outputTotal.setText(
                getResources().getString(R.string.total) + " " + price*count
        );
    }

    private InputFilter[] getFilters(String min, String max) {
        return new InputFilter[]{new InputFilterMinMax(min, max)};
    }

    private void placeOrder (Order order)
    {
        ODataService<Order> t = new ODataService<Order>(Order.class, this);

        try {
            t.create(ODataEndpointsNames.ORDERS, order);
            Toast.makeText(getApplicationContext(),
                    getResources().getString(R.string.succesful_order), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.failed_order), Toast.LENGTH_SHORT).show();
        }
    }

    private void returnHome()
    {
        Intent home = new Intent(MakeOrderActivity.this, UserHomeActivity.class);
        startActivity(home);
        finish();
    }
}