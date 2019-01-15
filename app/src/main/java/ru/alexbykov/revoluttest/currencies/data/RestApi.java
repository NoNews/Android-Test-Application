package ru.alexbykov.revoluttest.currencies.data;

import okhttp3.OkHttpClient;

public class RestApi {

    OkHttpClient okHttpClient;

    public RestApi(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
    }
}
