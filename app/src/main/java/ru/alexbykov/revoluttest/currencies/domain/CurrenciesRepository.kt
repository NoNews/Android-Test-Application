package ru.alexbykov.revoluttest.currencies.domain

import io.reactivex.Observable
import ru.alexbykov.revoluttest.currencies.data.common.entity.CurrencyInfo

interface CurrenciesRepository {


    fun changeCurrency(currency: String)
    fun changeCurrencyCount(count: Double)

    fun observeCurrencies(): Observable<CurrencyInfo>
}
