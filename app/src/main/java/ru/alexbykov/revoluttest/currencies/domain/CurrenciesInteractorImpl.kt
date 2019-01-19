package ru.alexbykov.revoluttest.currencies.domain

import io.reactivex.Completable
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
    }

    override fun observeCurrencies(): Observable<CurrencyBusinessResponse> {
        return currenciesRepository.observeCurrencies()
            .map { mapToBusinessResponse(it) }
    }


    override fun changeBaseCurrency(
        baseCurrency: CurrencyDetail
    ): Completable {
        return currenciesRepository.changeCurrency(baseCurrency.name)
    }

    override fun changeBaseCurrencyValue(baseCurrencyCount: String): Single<CurrencyBusinessResponse> {

        val count = if (baseCurrencyCount.isEmpty().or(equals("0"))) {
            0.0F
        } else {
            baseCurrencyCount.toFloat()
        }
        return currenciesRepository.changeBaseCurrencyValue(count)
            .map { mapToBusinessResponse(it) }
    }

    private fun mapToBusinessResponse(currencyInfo: CurrencyInfo): CurrencyBusinessResponse {
        val meta = currencyInfo.meta

        val currenciesDetail = currencyInfo.currencies
            .map {
                val currencyValue = it.value
                val currencyName = it.name
                val isBase = it.name == meta.baseCurrency
                CurrencyDetail(
                    currencyName,
                    currencyValue,
                    calculateCurrencyValue(meta.baseCurrencyCount, it.value, isBase)
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

    private fun calculateCurrencyValue(baseCurrencyCount: Float, value: Float, isBase: Boolean): Float {
        if (isBase) {
            return baseCurrencyCount
        }
        return baseCurrencyCount * value
    }


}