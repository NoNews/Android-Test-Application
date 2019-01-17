package ru.alexbykov.revoluttest.currencies.presentation.ui.adapter

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.alexbykov.revoluttest.R
import ru.alexbykov.revoluttest.currencies.data.storage.entity.Currency


class CurrenciesViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

    companion object {
        private const val LAYOUT = R.layout.item_currency
    }

    private var tvCurrencyName: TextView = itemView.findViewById(R.id.tv_currency_name)
    private var etCurrencyValue: EditText = itemView.findViewById(R.id.tv_currency_value)

    constructor(parent: ViewGroup) : this(LayoutInflater.from(parent.context).inflate(LAYOUT, parent, false))


    fun setupItem(
        currency: Currency,
        inputClickListener: ((Currency) -> Unit)?,
        inputChangeListener: ((String) -> Unit)?
    ) {

        setupUi(currency)
        setupUx(currency, inputClickListener, inputChangeListener)
    }

    private fun setupUx(
        currency: Currency,
        inputClickListener: ((Currency) -> Unit)?,
        inputChangeListener: ((String) -> Unit)?
    ) {
        if (inputClickListener != null) {
            etCurrencyValue.setOnFocusChangeListener { v, hasFocus ->
                if (!hasFocus) {
                    return@setOnFocusChangeListener
                }
                etCurrencyValue.setSelection(etCurrencyValue.text.length - 1)
                inputClickListener.invoke(currency)
            }
        }

        if (inputChangeListener != null) {
            etCurrencyValue.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }

                override fun afterTextChanged(s: Editable?) {
                    if (etCurrencyValue.isFocused) {
                        inputChangeListener.invoke(s.toString())
                    }
                }

            })
        }
    }

    private fun setupUi(currency: Currency) {
        updateName(currency.name)
        updateValue(currency.value)
    }

    fun updateName(name: String) {
        tvCurrencyName.text = name
    }

    fun updateValue(value: Float) {
        if (etCurrencyValue.isFocused) {
            return
        }


        val formattedCurrency = String.format("%.2f", value)
        etCurrencyValue.setText(formattedCurrency)
    }
}