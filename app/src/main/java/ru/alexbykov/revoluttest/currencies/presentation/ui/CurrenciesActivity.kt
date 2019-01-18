package ru.alexbykov.revoluttest.currencies.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.activity_main.*
import ru.alexbykov.revoluttest.R
import ru.alexbykov.revoluttest.common.di.Injector
import ru.alexbykov.revoluttest.common.presentation.MvpAppCompatActivity
import ru.alexbykov.revoluttest.common.presentation.hide
import ru.alexbykov.revoluttest.common.presentation.show
import ru.alexbykov.revoluttest.currencies.domain.entity.CurrencyDetail
import ru.alexbykov.revoluttest.currencies.presentation.mvp.CurrenciesPresenter
import ru.alexbykov.revoluttest.currencies.presentation.mvp.CurrenciesState
import ru.alexbykov.revoluttest.currencies.presentation.mvp.CurrenciesView
import ru.alexbykov.revoluttest.currencies.presentation.ui.adapter.CurrenciesAdapter
import ru.alexbykov.revoluttest.currencies.presentation.ui.adapter.CurrenciesDiffUtilItemCallback

class CurrenciesActivity : MvpAppCompatActivity(), CurrenciesView {

    companion object {
        const val LAYOUT = R.layout.activity_main
    }

    @InjectPresenter
    lateinit var currenciesPresenter: CurrenciesPresenter

    @ProvidePresenter
    fun provideCurrenciesPresenter() = Injector.currenciesComponent.currenciesPresenter


    private lateinit var currenciesAdapter: CurrenciesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(LAYOUT)
        setupUi()
        setupUx()
    }


    private fun setupUi() {
        val inflater = LayoutInflater.from(applicationContext)
        val currenciesDiffUtilItemCallback = CurrenciesDiffUtilItemCallback()
        currenciesAdapter = CurrenciesAdapter(inflater, currenciesDiffUtilItemCallback)
        rv_currencies.layoutManager = LinearLayoutManager(this)
        rv_currencies.adapter = currenciesAdapter

        val itemAnimator = rv_currencies.itemAnimator
        if (itemAnimator is SimpleItemAnimator) {
            itemAnimator.supportsChangeAnimations = false
        }
    }

    private fun setupUx() {

        currenciesAdapter.onClickInput { currenciesPresenter.onClickInput(it) }
        currenciesAdapter.onInputChanged { currenciesPresenter.onInputTextChanged(it) }
    }


    override fun showState(state: CurrenciesState) = when (state) {
        CurrenciesState.PROGRESS -> {
            progress_bar.show()
            rv_currencies.hide()
        }

        CurrenciesState.DATA -> {
            progress_bar.hide()
            rv_currencies.show()
        }

        CurrenciesState.WAITING_FOR_CONNECTION -> {
            progress_bar.hide()
            rv_currencies.hide()
        }

        CurrenciesState.NO_DATA -> {
            progress_bar.hide()
            rv_currencies.hide()
        }
    }


    override fun updateCurrencies(currencies: List<CurrencyDetail>) {
        currenciesAdapter.submitList(currencies)
    }
}



