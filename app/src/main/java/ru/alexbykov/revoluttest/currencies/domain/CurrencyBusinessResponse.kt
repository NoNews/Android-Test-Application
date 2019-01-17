package ru.alexbykov.revoluttest.currencies.domain

import ru.alexbykov.revoluttest.currencies.data.storage.entity.Currency
import ru.alexbykov.revoluttest.currencies.data.storage.entity.CurrencyMeta

data class CurrencyBusinessResponse(
    val meta: CurrencyMeta,
    val currencies: List<Currency>
)