package ru.alexbykov.revoluttest.currencies.presentation.ui.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import ru.alexbykov.revoluttest.currencies.domain.entity.CurrencyDetail

class CurrenciesAdapter(
    private val inflater: LayoutInflater,
    diffCallback: DiffUtil.ItemCallback<CurrencyDetail>
) : ListAdapter<CurrencyDetail, CurrenciesViewHolder>(diffCallback) {


    companion object {
        const val EXTRAS_CURRENCY_CALCULATED_VALUE = "extras_value_key"
        const val WRONG_CURRENCY_VALUE = -1F
    }

    private var inputClickListener: ((CurrencyDetail) -> Unit)? = null
    private var inputChangeListener: ((String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrenciesViewHolder {
        return CurrenciesViewHolder(inflater, parent, inputClickListener, inputChangeListener)
    }

    override fun onBindViewHolder(holder: CurrenciesViewHolder, position: Int) {
        val currency = getItem(position)
        holder.setupItem(currency, isBase(position))
    }


    override fun onBindViewHolder(holder: CurrenciesViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
            return
        }
        val bundle = payloads.first()

        if (bundle is Bundle) {
            if (bundle.containsKey(EXTRAS_CURRENCY_CALCULATED_VALUE)) {
                val value = bundle.getFloat(EXTRAS_CURRENCY_CALCULATED_VALUE, WRONG_CURRENCY_VALUE)
                holder.updateCalculatedValue(value, isBase(position))
            }
        } else {
            throw AssertionError("Payload must be Bundle instance")
        }
    }


    override fun onViewAttachedToWindow(holder: CurrenciesViewHolder) {
        super.onViewAttachedToWindow(holder)
        holder.onAttach()
    }

    override fun onViewDetachedFromWindow(holder: CurrenciesViewHolder) {
        super.onViewDetachedFromWindow(holder)
        holder.onDetach()
    }

    private fun isBase(position: Int): Boolean {
        return position == 0
    }


    fun onClickInput(inputClickListener: (CurrencyDetail) -> Unit) {
        this.inputClickListener = inputClickListener
    }

    fun onInputChanged(inputChangeListener: (String) -> Unit) {
        this.inputChangeListener = inputChangeListener
    }
}
