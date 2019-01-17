package ru.alexbykov.revoluttest.currencies.presentation.ui.adapter

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import ru.alexbykov.revoluttest.currencies.data.Currency

class CurrenciesAdapter(val inflater: LayoutInflater, diffCallback: DiffUtil.ItemCallback<Currency>) :
    ListAdapter<Currency, CurrenciesViewHolder>(diffCallback) {


    companion object {
        const val EXTRAS_CURRENCY_VALUE = "extras_value_key"
        const val EXTRAS_CURRENCY_NAME = "extras_name_key"
        const val WRONG_CURRENCY_VALUE = 0.0
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrenciesViewHolder {
        return CurrenciesViewHolder(parent)
    }

    override fun onBindViewHolder(holder: CurrenciesViewHolder, position: Int) {
        val currency = getItem(position)
        holder.setupItem(currency)
        Log.d("CURRENCY","ALL: "+ currency.value)

    }

    override fun onBindViewHolder(holder: CurrenciesViewHolder, position: Int, payloads: MutableList<Any>) {


        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
            return
        }

        val bundle = payloads.first()

        if (bundle is Bundle) {
            val name = bundle.getString(EXTRAS_CURRENCY_NAME, "")
            val value = bundle.getDouble(EXTRAS_CURRENCY_VALUE, WRONG_CURRENCY_VALUE)
            if (!name.isEmpty()) {
                holder.updateName(name)
            }

            if (value != WRONG_CURRENCY_VALUE) {
                holder.updateValue(value)
                Log.d("CURRENCY","PAYLOAD: "+value)
            }
        } else {
            throw AssertionError("Payload must be Bundle instance")
        }


    }
}
