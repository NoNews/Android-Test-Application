package ru.alexbykov.revoluttest.currencies.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.hdodenhof.circleimageview.CircleImageView
import io.reactivex.disposables.Disposable
import ru.alexbykov.revoluttest.R
import ru.alexbykov.revoluttest.common.presentation.RxSimpleTextWather
import ru.alexbykov.revoluttest.common.presentation.setEditTextEnabled
import ru.alexbykov.revoluttest.common.presentation.setTextCurrency
import ru.alexbykov.revoluttest.currencies.domain.entity.CurrencyDetail


class CurrenciesViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

    companion object {
        private const val LAYOUT = R.layout.item_currency
        private const val MAX_LENGTH_BASE_CURRENCY = 9
        private const val MAX_LENGTH_OTHER_CURRENCIES = 20
    }


    private var inputClickListener: ((CurrencyDetail) -> Unit)? = null
    private var inputChangeListener: ((String) -> Unit)? = null

    private var tvCode: TextView = itemView.findViewById(R.id.tv_code)
    private var tvDisplayName: TextView = itemView.findViewById(R.id.tv_display_name)
    private var etCurrentValue: CurrencyEditText = itemView.findViewById(R.id.et_currency_value)
    private var ivCountry: CircleImageView = itemView.findViewById(R.id.iv_country)


    private var textWatcherDisposable: Disposable? = null
    private val textWatcher = object : RxSimpleTextWather() {}

    constructor(
        inflater: LayoutInflater,
        parent: ViewGroup,
        inputClickListener: ((CurrencyDetail) -> Unit)?,
        inputChangeListener: ((String) -> Unit)?
    ) : this(inflater.inflate(LAYOUT, parent, false)) {
        this.inputChangeListener = inputChangeListener
        this.inputClickListener = inputClickListener
    }


    fun setupItem(currency: CurrencyDetail, isBase: Boolean) {
        setupUi(currency, isBase)
        setupUx(currency)
    }


    private fun setupUi(currency: CurrencyDetail, base: Boolean) {
        val code = currency.code
        tvDisplayName.text = currency.displayName
        tvCode.text = code
        ivCountry.setImageResource(CurrencyImage.from(code))
        updateCalculatedValue(currency.calculatedValue, base)


    }

    private fun setupUx(currency: CurrencyDetail) {
        etCurrentValue.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                return@setOnFocusChangeListener
            }
            inputClickListener?.invoke(currency)
        }
    }

    fun updateCalculatedValue(value: Double, isBase: Boolean) {


        if (isBase) {
            if (etCurrentValue.length() > MAX_LENGTH_BASE_CURRENCY) {
                etCurrentValue.setMaxLength(MAX_LENGTH_BASE_CURRENCY)
                return
            }
        } else {
            etCurrentValue.setMaxLength(MAX_LENGTH_OTHER_CURRENCIES)
        }

        etCurrentValue.setEditTextEnabled(isBase || etCurrentValue.isFocused || value > 0.00)
        if (etCurrentValue.isFocused && etCurrentValue.text.isNotEmpty()) {
            return
        }

        etCurrentValue.setTextCurrency(value)
    }

    fun onAttach() {
        etCurrentValue.addTextChangedListener(textWatcher)
        textWatcherDisposable = textWatcher.observeTextChanges()
            .filter { etCurrentValue.isFocused }
            .filter { it != "." }
            .map { it.replace(",", "") }
            .subscribe { inputChangeListener?.invoke(it) }
    }

    fun onDetach() {
        etCurrentValue.clearFocus()
        textWatcherDisposable?.dispose()
        textWatcherDisposable = null
        etCurrentValue.removeTextChangedListener(textWatcher)
    }

}




