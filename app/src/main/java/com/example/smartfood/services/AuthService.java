package com.example.smartfood.services;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;

import com.example.smartfood.Constants.Constants;
import com.example.smartfood.Models.User;
import com.example.smartfood.RequestModels.LoginRequest;
import com.example.smartfood.RequestModels.LoginResponse;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AuthService {
    private static AuthService instance;
    private TokenStorageService tokenStorage;
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private OkHttpClient client;
    private Gson gson;

    private AuthService(Context context) {
        tokenStorage = new TokenStorageService(context);
        client = new OkHttpClient();
        gson = new Gson();
    }

    public static AuthService getInstance(Context context) {
        if (instance == null) {
            instance = new AuthService(context);
        }
        return instance;
    }

    public LoginResponse login(LoginRequest loginRequest) {
        String json = gson.toJson(loginRequest);
        RequestBody body = RequestBody.create(json, JSON);

        Request request = new Request.Builder()
                .url(Constants.BASE_URL + "/api/Auth/login")
                .post(body)
                .build();

        try {
            Response response = client.newCall(request).execute();

            if (response.isSuccessful()) {
                String responseBody = response.body().string();

                LoginResponse loginResponse = gson.fromJson(responseBody, LoginResponse.class);
                tokenStorage.saveToken(loginResponse.getToken());
                tokenStorage.saveRefreshToken(loginResponse.getRefreshToken());

                return loginResponse;
            }
        }
        catch (IOException e) {
            return null;
        }

        return null;
    }

    public LoginResponse refreshToken(String token, String refreshToken) {
        String jsonBody = "{\"token\":\"" + token + "\", \"refreshToken\":\"" + refreshToken + "\"}";

        RequestBody requestBody = RequestBody.create(jsonBody, JSON);

        // Build the request
        Request request = new Request.Builder()
                .url(Constants.BASE_URL + "/api/Auth/refresh-token")
                .post(requestBody)
                .build();

        try {
            Response response = client.newCall(request).execute();

            if (response.isSuccessful()) {
                String responseBody = response.body().string();

                LoginResponse loginResponse = gson.fromJson(responseBody, LoginResponse.class);
                tokenStorage.saveToken(loginResponse.getToken());
                tokenStorage.saveRefreshToken(loginResponse.getRefreshToken());

                return loginResponse;
            }
        }
        catch (IOException e) {
            return null;
        }

        return null;
    }

    public User getUser()
    {
        return tokenStorage.getUser();
    }

    public void logout() {
        tokenStorage.signOut();
    }
}
