package ru.alexbykov.revoluttest.currencies.domain

import io.reactivex.Observable
import ru.alexbykov.revoluttest.currencies.data.Currency
import ru.alexbykov.revoluttest.currencies.data.CurrencyInfo
import ru.alexbykov.revoluttest.currencies.presentation.CurrenciesInteractor
import javax.inject.Inject

class CurrenciesInteractorImpl
@Inject internal constructor(val currenciesRepository: CurrenciesRepository) : CurrenciesInteractor {

    override fun observeCurrencies(): Observable<CurrencyBusinessResponse> {
        return currenciesRepository.observeCurrencies()
            .map {
                val copy = it.currencies!!.toMutableList()
                copy.add(0, it.defaultCurrency!!)

                //TODO remove muttable list here

                CurrencyBusinessResponse(it.lastUpdateDate!!, copy)
            }
    }

    override fun changeBaseCurrency(currency: Currency) {
        currenciesRepository.changeCurrency(currency.name!!)
    }

}