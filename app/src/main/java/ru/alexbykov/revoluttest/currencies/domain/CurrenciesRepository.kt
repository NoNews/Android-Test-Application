package ru.alexbykov.revoluttest.currencies.domain

import io.reactivex.Observable
import ru.alexbykov.revoluttest.currencies.data.CurrenciesResponse

interface CurrenciesRepository {


    fun changeCurrency(currency: String)

    fun observeCurrencies(): Observable<CurrenciesResponse>
}
