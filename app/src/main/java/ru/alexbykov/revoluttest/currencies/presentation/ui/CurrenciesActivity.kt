package ru.alexbykov.revoluttest.currencies.presentation.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import ru.alexbykov.revoluttest.R
import ru.alexbykov.revoluttest.currencies.presentation.mvp.CurrenciesState
import ru.alexbykov.revoluttest.currencies.presentation.mvp.CurrenciesView

class CurrenciesActivity : AppCompatActivity(), CurrenciesView {


    companion object {
        const val LAYOUT = R.layout.activity_main
    }

    private lateinit var currenciesAdapter: CurrenciesAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(LAYOUT)
        setupUi()
        setupUx()
    }


    private fun setupUi() {
        rv_currencies.layoutManager = LinearLayoutManager(this)
        rv_currencies.adapter = currenciesAdapter
    }

    private fun setupUx() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun showState(state: CurrenciesState) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateCurrencies(currencies: List<Any>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
