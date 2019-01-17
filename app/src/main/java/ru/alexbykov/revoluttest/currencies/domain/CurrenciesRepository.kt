package ru.alexbykov.revoluttest.currencies.domain

import io.reactivex.Observable
import io.reactivex.Single
import ru.alexbykov.revoluttest.currencies.data.CurrencyInfo

interface CurrenciesRepository {


    fun changeCurrency(currency: String): Single<CurrencyInfo>

    fun changeCurrencyCount(count: Double)

    fun observeCurrencies(): Observable<CurrencyInfo>
}
