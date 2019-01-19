package ru.alexbykov.revoluttest.common.presentation

import android.view.View
import android.widget.EditText
import androidx.core.content.ContextCompat


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

