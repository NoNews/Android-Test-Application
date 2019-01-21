package ru.alexbykov.revoluttest

import ru.alexbykov.revoluttest.currencies.data.CurrencyInfo
import ru.alexbykov.revoluttest.currencies.data.network.entity.CurrenciesResponse
import ru.alexbykov.revoluttest.currencies.data.storage.entity.Currency
import ru.alexbykov.revoluttest.currencies.data.storage.entity.CurrencyMeta
import ru.alexbykov.revoluttest.currencies.domain.entity.CurrencyDetail

object StubProvider {


    const val STUB_DATE = "2017-12-12"
    const val EUR_EXCHANGE_RATE = 66.4
    const val USD_EXCHANGE_RATE = 55.5
    const val EUR_CODE = "EUR"
    const val RUB_CODE = "RUB"
    const val USD_CODE = "USD"
    const val EUR_NAME = "Euro"
    const val USD_NAME = "US Dollar"

    fun stubCurrencyDetail() = CurrencyDetail(RUB_CODE, "Russian Ruble", 4444.0, 101001.0)

    fun provideCurrencyInfoBasedRuble(newCurrencyCount: Double) =
        CurrencyInfo(provideCurrencyMeta(newCurrencyCount), provideCurrencies())


    fun provideCurrenciesRepsponse(base: String) =
        CurrenciesResponse(base, STUB_DATE, mapOf(USD_CODE to USD_EXCHANGE_RATE, EUR_CODE to EUR_EXCHANGE_RATE))

    private fun provideCurrencyMeta(newCurrencyCount: Double) = CurrencyMeta(RUB_CODE, STUB_DATE, newCurrencyCount)

    fun provideCurrencies() =
        listOf(Currency(EUR_CODE, EUR_EXCHANGE_RATE), Currency(USD_CODE, USD_EXCHANGE_RATE))


    fun provideMeta() = CurrencyMeta(StubProvider.RUB_CODE, StubProvider.STUB_DATE, Double.NEGATIVE_INFINITY)
}