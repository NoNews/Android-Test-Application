package ru.alexbykov.revoluttest.currencies.domain.entity

data class CurrencyDetail(
    val code: String,
    val displayName: String,
    val exchangeRate: Float,
    val calculatedValue: Float
)
