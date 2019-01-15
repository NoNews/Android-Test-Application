package ru.alexbykov.revoluttest.currencies.presentation.ui

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.activity_main.*
import ru.alexbykov.revoluttest.R
import ru.alexbykov.revoluttest.common.presentation.MvpAppCompatActivity
import ru.alexbykov.revoluttest.currencies.di.Injector
import ru.alexbykov.revoluttest.currencies.presentation.mvp.CurrenciesPresenter
import ru.alexbykov.revoluttest.currencies.presentation.mvp.CurrenciesState
import ru.alexbykov.revoluttest.currencies.presentation.mvp.CurrenciesView

class CurrenciesActivity : MvpAppCompatActivity(), CurrenciesView {

    companion object {
        const val LAYOUT = R.layout.activity_main
    }

    @InjectPresenter
    lateinit var currenciesPresenter: CurrenciesPresenter

    @ProvidePresenter
    fun provideCurrenciesPresenter() = Injector.appComponent.currenciesPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(LAYOUT)
        setupUi()
        setupUx()
    }


    private fun setupUi() {
        rv_currencies.layoutManager = LinearLayoutManager(this)
    }

    private fun setupUx() {
    }


    override fun showState(state: CurrenciesState) {

    }

    override fun updateCurrencies(currencies: List<Any>) {
    }
}
