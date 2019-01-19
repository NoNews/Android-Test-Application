package ru.alexbykov.revoluttest.currencies.presentation.ui.adapter

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import ru.alexbykov.revoluttest.R
import ru.alexbykov.revoluttest.currencies.domain.entity.CurrencyDetail


class CurrenciesViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

    companion object {
        private const val LAYOUT = R.layout.item_currency
    }


    private var inputClickListener: ((CurrencyDetail) -> Unit)? = null
    private var inputChangeListener: ((String) -> Unit)? = null

    private var tvCurrencyName: TextView = itemView.findViewById(R.id.tv_currency_name)
    private var etCurrencyValue: EditText = itemView.findViewById(R.id.tv_currency_value)

    constructor(
        inflater: LayoutInflater, parent: ViewGroup,
        inputClickListener: ((CurrencyDetail) -> Unit)?,
        inputChangeListener: ((String) -> Unit)?
    ) : this(inflater.inflate(LAYOUT, parent, false)) {
        this.inputChangeListener = inputChangeListener
        this.inputClickListener = inputClickListener
    }


    fun setupItem(currency: CurrencyDetail, isBase: Boolean) {
        setupUi(currency, isBase)
        setupUx(currency, isBase)
    }


    private fun setupUi(
        currency: CurrencyDetail,
        base: Boolean
    ) {
        updateName(currency.name)
        updateCalculatedValue(currency.calculatedValue, base)
    }

    private fun setupUx(
        currency: CurrencyDetail,
        base: Boolean
    ) {
        etCurrencyValue.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                return@setOnFocusChangeListener
            }
            inputClickListener?.invoke(currency)
        }

        etCurrencyValue.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                
            }
        })

    }


    fun updateName(name: String) {
        tvCurrencyName.text = name
    }

    fun updateCalculatedValue(value: Float, isBase: Boolean) {
        if (isBase) {
            etCurrencyValue.isEnabled = true
            etCurrencyValue.setTextColor(ContextCompat.getColor(etCurrencyValue.context, android.R.color.black))
            return
        }

        etCurrencyValue.isEnabled = value > 0.00F

        if (etCurrencyValue.isEnabled) {
            etCurrencyValue.setTextColor(ContextCompat.getColor(etCurrencyValue.context, android.R.color.black))
        } else {
            etCurrencyValue.setTextColor(ContextCompat.getColor(etCurrencyValue.context, android.R.color.darker_gray))
        }

        val formattedCurrency = String.format("%.2f", value)
        etCurrencyValue.setText(formattedCurrency)
    }
}