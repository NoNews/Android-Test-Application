package ru.alexbykov.revoluttest.common.di

import ru.alexbykov.revoluttest.currencies.di.CurrenciesComponent

object Injector {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder().build()
    }

    val currenciesComponent: CurrenciesComponent by lazy {
        appComponent.currenciesBuilder().build()
    }
}


