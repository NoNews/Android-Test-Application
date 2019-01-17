package ru.alexbykov.revoluttest.currencies.data

import ru.alexbykov.revoluttest.currencies.data.storage.entity.Currency
import ru.alexbykov.revoluttest.currencies.data.storage.entity.CurrencyMeta


data class CurrencyInfo(
    val meta: CurrencyMeta,
    val currencies: List<Currency>
)

