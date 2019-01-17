package ru.alexbykov.revoluttest.currencies.presentation.ui.adapter

import android.os.Bundle
import androidx.recyclerview.widget.DiffUtil
import ru.alexbykov.revoluttest.currencies.data.storage.entity.Currency


class CurrenciesDiffUtilItemCallback : DiffUtil.ItemCallback<Currency>() {


    override fun areItemsTheSame(oldItem: Currency, newItem: Currency): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: Currency, newItem: Currency): Boolean {
        return oldItem == newItem
    }

    override fun getChangePayload(oldItem: Currency, newItem: Currency): Any? {
        val bundle = Bundle()

        if (oldItem.value != newItem.value) {
            bundle.putFloat(CurrenciesAdapter.EXTRAS_CURRENCY_VALUE, newItem.value)
        }

        if (oldItem.name != newItem.name) {
            bundle.putString(CurrenciesAdapter.EXTRAS_CURRENCY_NAME, newItem.name)
        }

        if (bundle.isEmpty) {
            return null
        }

        return bundle
    }
}