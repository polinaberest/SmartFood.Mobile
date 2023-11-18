package com.example.smartfood.services;

import android.content.Context;

import com.example.smartfood.interceptors.AuthInterceptor;

import okhttp3.OkHttpClient;

public class HttpClientFactory {
    private static HttpClientFactory instance;
    private OkHttpClient client;

    private HttpClientFactory() {
        client = new OkHttpClient.Builder()
                .addInterceptor(new AuthInterceptor(tokenService, authService))
                .build();
    }

    public static HttpClientFactory getInstance() {
        if (instance == null) {
            instance = new HttpClientFactory();
        }
        return instance;
    }

    public OkHttpClient getHttpClient() {
        return client;
    }
}
