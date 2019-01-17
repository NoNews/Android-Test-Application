package ru.alexbykov.revoluttest.common.di

import android.app.Application
import ru.alexbykov.revoluttest.currencies.di.CurrenciesComponent

object Injector {

    private var appComponent: AppComponent? = null

    fun initAppComponent(app: Application) {
        appComponent = DaggerAppComponent.builder()
            .applicationContextModule(ApplicationContextModule(app))
            .build()
    }

    val currenciesComponent: CurrenciesComponent by lazy {
        if (appComponent == null) {
            throw IllegalStateException("Must initialize AppComponent")
        }
        appComponent!!.currenciesBuilder().build()
    }
}


