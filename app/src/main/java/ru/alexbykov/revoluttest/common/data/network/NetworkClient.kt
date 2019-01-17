package ru.alexbykov.revoluttest.common.data.network

import io.reactivex.annotations.NonNull
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import ru.alexbykov.revoluttest.currencies.data.network.CurrencyEndpoint


class NetworkClient(okHttpClient: OkHttpClient) {

    companion object {
        private const val URL = "https://revolut.duckdns.org/"
    }


    private val retrofitClient: Retrofit
    val currencyEndpoint: CurrencyEndpoint

    init {
        retrofitClient = buildRetrofitClient(okHttpClient)
        currencyEndpoint = retrofitClient.create(CurrencyEndpoint::class.java)
    }

    @NonNull
    private fun buildRetrofitClient(@NonNull client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }


}
