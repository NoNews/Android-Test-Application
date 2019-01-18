package ru.alexbykov.revoluttest.currencies.data.config

import ru.alexbykov.revoluttest.BuildConfig
import java.util.*
import javax.inject.Inject

class CurrenciesConfigImpl @Inject internal constructor() : CurrenciesConfig {


    override fun getUpdateTime(): Long {
        return BuildConfig.CURRENCY_LONG_POLLING_TIME_IN_SECONDS
    }

    override fun getBaseCurrencyCount(): Float {
        return BuildConfig.BASE_CURRENCY_DEFAULT_COUNT
    }


    override fun getDeviceCurrency(): String {
        val locale = Locale.getDefault() ?: return BuildConfig.BASE_CURRENCY_IF_UNABLE_TO_DETERMINE
        val localeCurrency = Currency.getInstance(locale)
        return localeCurrency.currencyCode
    }

}