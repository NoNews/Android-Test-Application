package ru.alexbykov.revoluttest.currencies.domain

import io.reactivex.Observable
import ru.alexbykov.revoluttest.currencies.data.CurrenciesResponse
import ru.alexbykov.revoluttest.currencies.presentation.CurrenciesInteractor
import javax.inject.Inject

class CurrenciesInteractorImpl

@Inject internal constructor(val currenciesRepository: CurrenciesRepository) : CurrenciesInteractor {


    override fun observeCurrencies(): Observable<CurrenciesResponse> {
        return currenciesRepository.observeCurrencies()
    }

}