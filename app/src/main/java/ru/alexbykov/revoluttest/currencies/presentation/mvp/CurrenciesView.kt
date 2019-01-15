package ru.alexbykov.revoluttest.currencies.presentation.mvp

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(AddToEndStrategy::class)
interface CurrenciesView : MvpView {

    fun showState(state: CurrenciesState)

    fun updateCurrencies(currencies: List<Any>)
}
