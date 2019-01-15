package ru.alexbykov.revoluttest.currencies.presentation.ui

import android.view.View
import androidx.recyclerview.widget.RecyclerView

class CurrenciesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private lateinit var any: Any

    fun setupUi(any: Any) {
        this.any =any
    }
}