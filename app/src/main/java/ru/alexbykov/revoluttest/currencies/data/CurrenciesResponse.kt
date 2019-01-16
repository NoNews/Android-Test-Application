package ru.alexbykov.revoluttest.currencies.data

import com.google.gson.annotations.SerializedName

data class CurrenciesResponse(
    @SerializedName("base") val base: String,
    @SerializedName("date") val date: String,
    @SerializedName("rates") val rates: Map<String, Double>
)
