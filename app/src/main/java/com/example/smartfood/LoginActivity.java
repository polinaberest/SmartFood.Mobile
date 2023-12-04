package com.example.smartfood;

import static android.app.PendingIntent.getActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartfood.Constants.Role;
import com.example.smartfood.Models.User;
import com.example.smartfood.RequestModels.LoginRequest;
import com.example.smartfood.RequestModels.LoginResponse;
import com.example.smartfood.SupplierActivities.SupplierHomeActivity;
import com.example.smartfood.UserActivities.UserHomeActivity;
import com.example.smartfood.services.AuthService;

public class LoginActivity extends AppCompatActivity {

    private Button loginButton;
    private EditText emailInput, passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = (Button) findViewById(R.id.login_btn);
        emailInput = (EditText) findViewById(R.id.login_email_input);
        passwordInput = (EditText) findViewById(R.id.login_password_input);

        /*emailInput.setText("eater@gmail.com");
        passwordInput.setText("Ja17nuary");*/
        emailInput.setText("pama1user@gmail.com");
        passwordInput.setText("admin123");

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });
    }

    private void attemptLogin() {
        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();

        LoginResponse response = AuthService.getInstance(this).login(new LoginRequest(email, password));

        if (response != null) {
            User user = AuthService.getInstance(this).getUser();
            Intent intent;
            String role = user.getRoles();
            if (role.equals(String.valueOf(Role.employee)))
            {
                intent = new Intent(LoginActivity.this, UserHomeActivity.class);
            }
            else {
                intent = new Intent(LoginActivity.this, SupplierHomeActivity.class);
            }

            startActivity(intent);
            finish();
        }
        else {
            Toast.makeText(this, getResources().getString(R.string.universal_fail), Toast.LENGTH_SHORT).show();
        }
    }
}