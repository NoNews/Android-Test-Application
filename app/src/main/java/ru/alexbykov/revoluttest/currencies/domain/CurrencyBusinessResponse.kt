package ru.alexbykov.revoluttest.currencies.domain

import ru.alexbykov.revoluttest.currencies.data.common.entity.Currency

data class CurrencyBusinessResponse(
    val updateDate: String,
    val currencies: List<Currency>
)