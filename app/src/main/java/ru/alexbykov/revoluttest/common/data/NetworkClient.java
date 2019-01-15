package ru.alexbykov.revoluttest.common.data;

import okhttp3.OkHttpClient;

public class NetworkClient {

    OkHttpClient okHttpClient;

    public NetworkClient(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
    }
}
