package ru.alexbykov.revoluttest.common.presentation

import android.view.View


fun View.hide() {
    visibility = View.GONE
}

fun View.show() {
    visibility = View.VISIBLE
}
