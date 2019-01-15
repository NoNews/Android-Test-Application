package ru.alexbykov.revoluttest.currencies.di

object Injector {

    val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder().build()
    }
}


