package ru.alexbykov.revoluttest.currencies.domain

import io.reactivex.Observable
import io.reactivex.Single
import ru.alexbykov.revoluttest.currencies.data.CurrencyInfo
import ru.alexbykov.revoluttest.currencies.domain.entity.CurrencyBusinessResponse
import ru.alexbykov.revoluttest.currencies.domain.entity.CurrencyDetail
import ru.alexbykov.revoluttest.currencies.presentation.CurrenciesInteractor
import java.util.*
import javax.inject.Inject

class CurrenciesInteractorImpl
@Inject internal constructor(private val currenciesRepository: CurrenciesRepository) : CurrenciesInteractor {


    companion object {
        const val BASE_CURRENCY_REQUIRED_POSITION = 0
        const val BASE_INPUT_VALUE = 0.0F
    }

    override fun observeCurrencies(): Observable<CurrencyBusinessResponse> {
        return currenciesRepository.observeCurrencies()
            .map { mapToBusinessResponse(it) }
    }


    override fun changeBaseCurrency(currency: CurrencyDetail): Single<CurrencyBusinessResponse> {
        return currenciesRepository.changeCurrency(currency.name)
            .map { mapToBusinessResponse(it) }
    }

    override fun changeBaseCurrencyValue(baseCurrencyValue: String): Single<CurrencyBusinessResponse> {
        var inputValue = BASE_INPUT_VALUE
        if (baseCurrencyValue.isNotEmpty()) {
            inputValue = baseCurrencyValue.toFloat()
        }
        return currenciesRepository.changeBaseCurrencyValue(inputValue)
            .map { mapToBusinessResponse(it) }
    }

    private fun mapToBusinessResponse(it: CurrencyInfo): CurrencyBusinessResponse {
        val meta = it.meta

        val currenciesDetail = it.currencies
            .map {
                val currencyValue = it.value
                val currencyName = it.name
                CurrencyDetail(currencyName, currencyValue, calculateCurrencyValue(meta.lastUserInput, currencyValue))
            }

        var baseCurrencyPosition = 0

        currenciesDetail.forEachIndexed { index, currencyDetail ->
            if (currencyDetail.name == meta.baseCurrency) {
                baseCurrencyPosition = index
                return@forEachIndexed
            }
        }
        Collections.swap(currenciesDetail, baseCurrencyPosition, BASE_CURRENCY_REQUIRED_POSITION)

        return CurrencyBusinessResponse(meta.baseCurrency, meta.updateTime, currenciesDetail)
    }

    private fun calculateCurrencyValue(userInput: Float, currencyValue: Float): Float {
        if (userInput == BASE_INPUT_VALUE) {
            return BASE_INPUT_VALUE
        }
        return userInput * currencyValue
    }


}