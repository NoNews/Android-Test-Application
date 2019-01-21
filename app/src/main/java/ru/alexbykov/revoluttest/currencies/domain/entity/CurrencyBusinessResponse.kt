package ru.alexbykov.revoluttest.currencies.domain.entity

data class CurrencyBusinessResponse(
    val baseCurrency: String,
    val lastUpdateTime: String,
    val currencies: List<CurrencyDetail>
)