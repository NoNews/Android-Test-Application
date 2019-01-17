package ru.alexbykov.revoluttest.currencies.data.common.entity

data class CurrencyInfo(
    val lastUpdateDate: String,
    val defaultCurrency: Currency,
    val currencies: List<Currency>
)

