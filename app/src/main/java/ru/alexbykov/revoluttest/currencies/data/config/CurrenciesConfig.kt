package ru.alexbykov.revoluttest.currencies.data.config

interface CurrenciesConfig {

    /**
     * Update time for currencies
     * @return time in seconds
     */
    fun getUpdateTime(): Long

    fun getBaseCurrencyCount(): Float


    fun getDeviceCurrency(): String

}