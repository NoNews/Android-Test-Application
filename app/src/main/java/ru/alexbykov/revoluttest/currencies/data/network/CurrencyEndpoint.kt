package ru.alexbykov.revoluttest.currencies.data.network

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import ru.alexbykov.revoluttest.currencies.data.network.entity.CurrenciesResponse

interface CurrencyEndpoint {

    @GET("latest?")
    fun getLatest(@Query("base") baseCurrency: String): Single<CurrenciesResponse>
}
