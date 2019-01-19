package ru.alexbykov.revoluttest.currencies.domain

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import ru.alexbykov.revoluttest.currencies.data.CurrencyInfo

interface CurrenciesRepository {

    fun changeCurrency(currency: String, baseCurrencyCount: Float): Completable

    fun observeCurrencies(): Observable<CurrencyInfo>

    fun changeBaseCurrencyValue(calculatedValue: Float): Single<CurrencyInfo>

}
