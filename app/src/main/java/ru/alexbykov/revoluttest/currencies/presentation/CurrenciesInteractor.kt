package ru.alexbykov.revoluttest.currencies.presentation

import io.reactivex.Observable
import ru.alexbykov.revoluttest.currencies.data.CurrenciesResponse


interface CurrenciesInteractor {

    fun observeCurrencies(): Observable<CurrenciesResponse>
}
