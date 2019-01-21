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


    override fun changeBaseCurrency(baseCurrency: CurrencyDetail): Completable {
        return currenciesRepository.changeCurrency(baseCurrency.code)
    }

    override fun changeBaseCurrencyValue(baseCurrencyCount: String): Single<CurrencyBusinessResponse> {

        val count = if (baseCurrencyCount.isEmpty()) {
            0.0
        } else {
            baseCurrencyCount.toDouble()
        }
        return currenciesRepository.changeBaseCurrencyValue(count)
            .map { mapToBusinessResponse(it) }
    }

    private fun mapToBusinessResponse(currencyInfo: CurrencyInfo): CurrencyBusinessResponse {
        val meta = currencyInfo.meta

        val currenciesDetail = currencyInfo.currencies
            .map {
                val currencyValue = it.value
                val code = it.name
                val isBase = it.name == meta.baseCurrency

                val displayName = getDisplayName(code)
                val calculateCurrencyValue = calculateCurrencyValue(meta.baseCurrencyCount, it.value, isBase)
                CurrencyDetail(
                    code,
                    displayName,
                    currencyValue,
                    calculateCurrencyValue
                )
            }

        var baseCurrencyPosition = 0

        currenciesDetail.forEachIndexed { index, currencyDetail ->
            if (currencyDetail.code == meta.baseCurrency) {
                baseCurrencyPosition = index
                return@forEachIndexed
            }
        }
        if (baseCurrencyPosition != 0) {
            Collections.swap(currenciesDetail, baseCurrencyPosition, BASE_CURRENCY_REQUIRED_POSITION)
        }

        return CurrencyBusinessResponse(meta.baseCurrency, meta.updateTime, currenciesDetail)
    }

    private fun getDisplayName(code: String) = Currency.getInstance(code).displayName

    private fun calculateCurrencyValue(baseCurrencyCount: Double, value: Double, isBase: Boolean): Double {
        if (isBase) {
            return baseCurrencyCount
        }
        return baseCurrencyCount * value
    }


}