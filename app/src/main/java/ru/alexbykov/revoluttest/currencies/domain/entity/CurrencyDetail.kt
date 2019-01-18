package ru.alexbykov.revoluttest.currencies.domain.entity

data class CurrencyDetail(
    val name: String,
    val exchangeRate: Float,
    val calculatedValue: Float
)
