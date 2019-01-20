package ru.alexbykov.revoluttest.common.presentation

import android.view.View
import android.widget.EditText
import androidx.core.content.ContextCompat
import java.math.BigDecimal
import java.text.DecimalFormat


fun View.hide() {
    visibility = View.GONE
}

fun View.show() {
    visibility = View.VISIBLE
}


fun EditText.setEditTextEnabled(enabled: Boolean) {
    isEnabled = enabled
    if (isEnabled) {
        setTextColor(ContextCompat.getColor(context, android.R.color.black))
    } else {
        setTextColor(ContextCompat.getColor(context, android.R.color.darker_gray))
    }
}


fun EditText.setTextCurrency(text: Double) {
    val bigDecimal = BigDecimal(text)
    val df = DecimalFormat("#,##0.00000")
    setText(df.format(bigDecimal).replace(".00000", ""))
}

