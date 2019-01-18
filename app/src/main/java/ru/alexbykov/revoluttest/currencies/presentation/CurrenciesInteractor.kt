package ru.alexbykov.revoluttest.currencies.presentation

import io.reactivex.Observable
import io.reactivex.Single
import ru.alexbykov.revoluttest.currencies.domain.entity.CurrencyBusinessResponse
import ru.alexbykov.revoluttest.currencies.domain.entity.CurrencyDetail


interface CurrenciesInteractor {

    fun observeCurrencies(): Observable<CurrencyBusinessResponse>

    fun changeBaseCurrency(currency: CurrencyDetail): Single<CurrencyBusinessResponse>

    fun changeBaseCurrencyValue(it: String): Single<CurrencyBusinessResponse>
}
