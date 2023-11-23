package com.example.smartfood;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.smartfood.RequestModels.LoginRequest;
import com.example.smartfood.RequestModels.LoginResponse;
import com.example.smartfood.RequestModels.RegisterRequest;
import com.example.smartfood.RequestModels.RegisterResponse;
import com.example.smartfood.UserActivities.UserHomeActivity;
import com.example.smartfood.services.AuthService;

public class RegisterActivity extends AppCompatActivity {
    private Button registerButton;
    private EditText emailInput, passwordInput, nameInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerButton = (Button) findViewById(R.id.register_btn);
        emailInput = (EditText) findViewById(R.id.register_email_input);
        passwordInput = (EditText) findViewById(R.id.register_password_input);
        nameInput = (EditText) findViewById(R.id.register_name_input);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validInput())
                {
                    attemptRegister();
                }
                else{
                    Toast.makeText(RegisterActivity.this, getResources().getString(R.string.validation_fail), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean validInput()
    {
        boolean isEmailValid = isEmailValid(emailInput);
        boolean isPasswordValid = isPasswordValid(passwordInput);
        boolean isNameValid = isNameValid(nameInput);

        return isEmailValid && isPasswordValid && isNameValid;
    }

    private void attemptRegister() {
        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();
        String name = nameInput.getText().toString();

        RegisterResponse response = AuthService.getInstance(this).register(new RegisterRequest(email, password, name));

        if (response != null) {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        else {
            Toast.makeText(this, getResources().getString(R.string.universal_fail), Toast.LENGTH_SHORT).show();
        }
    }

    private static boolean isEmailValid(EditText emailEditText) {
        CharSequence email = emailEditText.getText().toString().trim();
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    private static boolean isPasswordValid(EditText passwordEditText) {
        return passwordEditText.getText().toString().trim().length() >= 6;
    }

    private static boolean isNameValid(EditText nameEditText) {
        return !TextUtils.isEmpty(nameEditText.getText().toString().trim());
    }
}