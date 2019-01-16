package ru.alexbykov.revoluttest.currencies.presentation

import io.reactivex.Observable
import ru.alexbykov.revoluttest.currencies.data.CurrenciesResponse
import ru.alexbykov.revoluttest.currencies.data.CurrencyInfo


interface CurrenciesInteractor {

    fun observeCurrencies(): Observable<CurrencyInfo>
}
