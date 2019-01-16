package ru.alexbykov.revoluttest.currencies.presentation.ui.adapter

import androidx.recyclerview.widget.DiffUtil
import ru.alexbykov.revoluttest.currencies.data.Currency

class CurrenciesDiffUtilItemCallback : DiffUtil.ItemCallback<Currency>() {


    override fun areItemsTheSame(oldItem: Currency, newItem: Currency): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: Currency, newItem: Currency): Boolean {
        return oldItem == newItem
    }
}