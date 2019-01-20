package ru.alexbykov.revoluttest.currencies.presentation.ui.adapter

import android.os.Bundle
import androidx.recyclerview.widget.DiffUtil
import ru.alexbykov.revoluttest.currencies.domain.entity.CurrencyDetail


class CurrenciesDiffUtilItemCallback : DiffUtil.ItemCallback<CurrencyDetail>() {


    override fun areItemsTheSame(oldItem: CurrencyDetail, newItem: CurrencyDetail): Boolean {
        return oldItem.code == newItem.code
    }

    override fun areContentsTheSame(oldItem: CurrencyDetail, newItem: CurrencyDetail): Boolean {
        return oldItem == newItem
    }

    override fun getChangePayload(oldItem: CurrencyDetail, newItem: CurrencyDetail): Any? {
        val bundle = Bundle()

        if (oldItem.calculatedValue != newItem.calculatedValue) {
            bundle.putFloat(CurrenciesAdapter.EXTRAS_CURRENCY_CALCULATED_VALUE, newItem.calculatedValue)
        }

        if (bundle.isEmpty) {
            return null
        }

        return bundle
    }
}