package ru.alexbykov.revoluttest.common.data.network

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query
import ru.alexbykov.revoluttest.currencies.data.CurrenciesResponse

interface CurrencyEndpoint {

    @GET("latest?")
    fun getLatest(@Query("base") baseCurrency: String): Single<CurrenciesResponse>
}
