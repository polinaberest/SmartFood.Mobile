package com.example.smartfood.services;

import android.content.Context;

import com.example.smartfood.interceptors.AuthInterceptor;

import okhttp3.OkHttpClient;

public class HttpClientFactory {
    private static HttpClientFactory instance;
    private OkHttpClient client;

    private HttpClientFactory(Context context) {
        client = new OkHttpClient.Builder()
                .addInterceptor(new AuthInterceptor(new TokenStorageService(context), AuthService.getInstance(context)))
                .build();
    }

    public static HttpClientFactory getInstance(Context context) {
        if (instance == null) {
            instance = new HttpClientFactory(context);
        }
        return instance;
    }

    public OkHttpClient getHttpClient() {
        return client;
    }
}
