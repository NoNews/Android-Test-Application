package ru.alexbykov.revoluttest.common.di

import dagger.Component
import ru.alexbykov.revoluttest.currencies.di.CurrenciesComponent
import javax.inject.Singleton


@Singleton
@Component(modules = [ApplicationContextModule::class, NetworkModule::class, DatabaseModule::class])
interface AppComponent {
    fun currenciesBuilder(): CurrenciesComponent.Builder

}
