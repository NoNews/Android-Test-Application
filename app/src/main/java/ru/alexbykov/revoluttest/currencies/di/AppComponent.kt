package ru.alexbykov.revoluttest.currencies.di

import dagger.Component
import ru.alexbykov.revoluttest.currencies.presentation.mvp.CurrenciesPresenter

@Component(modules = [NetworkModule::class])
interface AppComponent {

    val currenciesPresenter: CurrenciesPresenter
}
