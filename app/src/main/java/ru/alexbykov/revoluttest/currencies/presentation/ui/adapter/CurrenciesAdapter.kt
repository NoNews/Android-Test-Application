package ru.alexbykov.revoluttest.currencies.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import ru.alexbykov.revoluttest.currencies.data.Currency

class CurrenciesAdapter(val inflater: LayoutInflater, diffCallback: DiffUtil.ItemCallback<Currency>) :
    ListAdapter<Currency, CurrenciesViewHolder>(diffCallback) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrenciesViewHolder {
        return CurrenciesViewHolder(parent)
    }

    override fun onBindViewHolder(holder: CurrenciesViewHolder, position: Int) {
        val currency = getItem(position)
        holder.setupItem(currency)
    }
}
