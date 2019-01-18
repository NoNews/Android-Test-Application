package ru.alexbykov.revoluttest.currencies.domain.entity

data class CurrencyDetail(
    val name: String,
    val value: Float,
    val calculatedValue: Float
)
