package ru.alexbykov.revoluttest.currencies.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.alexbykov.revoluttest.R
import ru.alexbykov.revoluttest.currencies.data.Currency


class CurrenciesViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

    companion object {
        private const val LAYOUT = R.layout.item_currency
    }

    private var tvCurrencyName: TextView = itemView.findViewById(R.id.tv_currency_name)
    private var etCurrencyValue: EditText = itemView.findViewById(R.id.tv_currency_value)

    constructor(parent: ViewGroup) : this(LayoutInflater.from(parent.context).inflate(LAYOUT, parent, false))


    fun setupItem(currency: Currency, inputClickListener: ((Currency) -> Unit)?) {
        setupUi(currency)
        setupUx(currency, inputClickListener)
    }

    private fun setupUx(currency: Currency, inputClickListener: ((Currency) -> Unit)?) {
        if (inputClickListener != null) {

            etCurrencyValue.setOnFocusChangeListener { v, hasFocus ->
                if (!hasFocus) {
                    return@setOnFocusChangeListener
                }
                inputClickListener.invoke(currency)
            }
        }
    }

    private fun setupUi(currency: Currency) {
        updateName(currency.name!!)
        updateValue(currency.value)
    }

    fun updateName(name: String) {
        tvCurrencyName.text = name
    }

    fun updateValue(value: Double) {
        etCurrencyValue.setText(value.toString())
    }
}