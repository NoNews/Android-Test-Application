package ru.alexbykov.revoluttest.currencies.presentation

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import ru.alexbykov.revoluttest.currencies.domain.entity.CurrencyBusinessResponse
import ru.alexbykov.revoluttest.currencies.domain.entity.CurrencyDetail


interface CurrenciesInteractor {

    fun observeCurrencies(): Observable<CurrencyBusinessResponse>

    fun changeBaseCurrency(baseCurrency: CurrencyDetail): Completable

    fun changeBaseCurrencyValue(baseCurrencyCount: String): Single<CurrencyBusinessResponse>
}
