package ru.alexbykov.revoluttest.common.data.network

import androidx.annotation.NonNull
import ru.alexbykov.revoluttest.currencies.data.network.CurrencyEndpoint

interface NetworkClient {

    @NonNull
    fun getCurrencyEndpoint(): CurrencyEndpoint
}