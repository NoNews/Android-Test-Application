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


    override fun changeBaseCurrency(baseCurrency: CurrencyDetail, baseCurrencyCount:Float): Single<CurrencyBusinessResponse> {
        return currenciesRepository.changeCurrency(baseCurrency.name, baseCurrencyCount)
            .map { mapToBusinessResponse(it) }
    }

    override fun changeBaseCurrencyValue(baseCurrencyCount: String): Single<CurrencyBusinessResponse> {

        val count = if (baseCurrencyCount.isEmpty().or(equals("0"))) {
            BASE_INPUT_VALUE
        } else {
            baseCurrencyCount.toFloat()
        }
        return currenciesRepository.changeBaseCurrencyValue(count)
            .map { mapToBusinessResponse(it) }
    }

    private fun mapToBusinessResponse(it: CurrencyInfo): CurrencyBusinessResponse {
        val meta = it.meta

        val currenciesDetail = it.currencies
            .map {
                val currencyValue = it.value
                val currencyName = it.name
                CurrencyDetail(
                    currencyName,
                    currencyValue,
                    calculateCurrencyValue(meta.baseCurrencyCount, currencyValue)
                )
            }

        var baseCurrencyPosition = 0

        currenciesDetail.forEachIndexed { index, currencyDetail ->
            if (currencyDetail.name == meta.baseCurrency) {
                baseCurrencyPosition = index
                return@forEachIndexed
            }
        }
        if (baseCurrencyPosition != 0) {
            Collections.swap(currenciesDetail, baseCurrencyPosition, BASE_CURRENCY_REQUIRED_POSITION)
        }

        return CurrencyBusinessResponse(meta.baseCurrency, meta.updateTime, currenciesDetail)
    }

    private fun calculateCurrencyValue(baseCurrencyCount: Float, currencyValue: Float): Float {
        if (baseCurrencyCount == BASE_INPUT_VALUE) {
            return BASE_INPUT_VALUE
        }

        if (currencyValue == Float.NEGATIVE_INFINITY) {
            return baseCurrencyCount
        }
        return baseCurrencyCount * currencyValue
    }


}