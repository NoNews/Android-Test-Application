package ru.alexbykov.revoluttest.currencies.presentation

import io.reactivex.Observable
import io.reactivex.Single
import ru.alexbykov.revoluttest.currencies.data.storage.entity.Currency
import ru.alexbykov.revoluttest.currencies.domain.CurrencyBusinessResponse


interface CurrenciesInteractor {

    fun observeCurrencies(): Observable<CurrencyBusinessResponse>

    fun changeBaseCurrency(currency: Currency): Single<CurrencyBusinessResponse>
}
