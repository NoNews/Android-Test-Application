package ru.alexbykov.revoluttest.currencies.domain

import io.reactivex.Observable
import io.reactivex.Single
import ru.alexbykov.revoluttest.currencies.data.CurrencyInfo
import ru.alexbykov.revoluttest.currencies.data.storage.entity.Currency
import ru.alexbykov.revoluttest.currencies.presentation.CurrenciesInteractor
import javax.inject.Inject

class CurrenciesInteractorImpl
@Inject internal constructor(private val currenciesRepository: CurrenciesRepository) : CurrenciesInteractor {


    override fun observeCurrencies(): Observable<CurrencyBusinessResponse> {
        return currenciesRepository.observeCurrencies()
            .map { mapToBusinessResponse(it) }
    }


    override fun changeBaseCurrency(currency: Currency): Single<CurrencyBusinessResponse> {
        return currenciesRepository.changeCurrency(currency.name)
            .map { mapToBusinessResponse(it) }
    }

    override fun changeBaseCurrencyValue(baseCurrencyValue: String): Single<CurrencyBusinessResponse> {
        var inputValue = 0.0F
        if (baseCurrencyValue.isNotEmpty()) {
            inputValue = baseCurrencyValue.toFloat()
        }
        return currenciesRepository.changeBaseCurrencyValue(inputValue)
            .map { mapToBusinessResponse(it) }
    }

    private fun mapToBusinessResponse(it: CurrencyInfo): CurrencyBusinessResponse {
        val copy = it.currencies.toMutableList()
        copy.add(0, Currency(it.meta.baseCurrency, it.meta.lastUserInput))
        return CurrencyBusinessResponse(it.meta, copy.toList())
    }

}