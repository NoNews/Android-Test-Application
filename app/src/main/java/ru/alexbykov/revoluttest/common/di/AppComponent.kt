package ru.alexbykov.revoluttest.common.di

import dagger.Component
import ru.alexbykov.revoluttest.currencies.di.CurrenciesComponent
import javax.inject.Singleton


@Singleton
@Component(modules = [NetworkModule::class])
interface AppComponent {
    fun currenciesBuilder(): CurrenciesComponent.Builder

}
