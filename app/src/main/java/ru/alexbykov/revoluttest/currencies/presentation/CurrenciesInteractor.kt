package ru.alexbykov.revoluttest.currencies.presentation

import io.reactivex.Observable
import io.reactivex.Single
import ru.alexbykov.revoluttest.currencies.domain.entity.CurrencyBusinessResponse
import ru.alexbykov.revoluttest.currencies.domain.entity.CurrencyDetail


interface CurrenciesInteractor {

    fun observeCurrencies(): Observable<CurrencyBusinessResponse>

    fun changeBaseCurrency(baseCurrency: CurrencyDetail, baseCurrencyCount: Float): Single<CurrencyBusinessResponse>

    fun changeBaseCurrencyValue(baseCurrencyCount: String): Single<CurrencyBusinessResponse>
}
