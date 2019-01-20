package ru.alexbykov.revoluttest.currencies.presentation.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.hdodenhof.circleimageview.CircleImageView
import io.reactivex.disposables.Disposable
import ru.alexbykov.revoluttest.R
import ru.alexbykov.revoluttest.common.presentation.RxSimpleTextWather
import ru.alexbykov.revoluttest.common.presentation.setEditTextEnabled
import ru.alexbykov.revoluttest.currencies.domain.entity.CurrencyDetail
import java.math.BigDecimal
import java.text.DecimalFormat



class CurrenciesViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

    companion object {
        private const val LAYOUT = R.layout.item_currency
    }


    private var inputClickListener: ((CurrencyDetail) -> Unit)? = null
    private var inputChangeListener: ((String) -> Unit)? = null

    private var tvCode: TextView = itemView.findViewById(R.id.tv_code)
    private var tvDisplayName: TextView = itemView.findViewById(R.id.tv_display_name)
    private var etCurrentValue: EditText = itemView.findViewById(R.id.et_currency_value)
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
        setupUx(currency, isBase)
    }


    private fun setupUi(currency: CurrencyDetail, base: Boolean) {
        val code = currency.code
        tvDisplayName.text = currency.displayName
        tvCode.text = code
        ivCountry.setImageResource(CurrencyImage.from(code))
        updateCalculatedValue(currency.calculatedValue, base)
    }

    private fun setupUx(currency: CurrencyDetail, base: Boolean) {
        etCurrentValue.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus) {
                return@setOnFocusChangeListener
            }
            inputClickListener?.invoke(currency)
        }
    }


    fun updateCalculatedValue(value: Float, isBase: Boolean) {
        etCurrentValue.setEditTextEnabled(isBase || etCurrentValue.isFocused || value > 0.00)
        if (etCurrentValue.isFocused && etCurrentValue.text.isNotEmpty()) {
            return
        }
        val result = formatCurrency(value.toString())
        etCurrentValue.setText(result)
    }


    private fun formatCurrency(value: String): String {
        val bigDecimal = BigDecimal(value)
        val df = DecimalFormat("#,##0.00")
        return df.format(bigDecimal).replace(".00", "")
    }

    fun onAttach() {
        etCurrentValue.addTextChangedListener(textWatcher)
        textWatcherDisposable = textWatcher.observeTextChanges()
            .filter { etCurrentValue.isFocused }
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




