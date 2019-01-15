package ru.alexbykov.revoluttest.currencies.di

import dagger.Binds
import dagger.Module
import ru.alexbykov.revoluttest.currencies.data.CurrenciesRepositoryImpl
import ru.alexbykov.revoluttest.currencies.domain.CurrenciesInteractorImpl
import ru.alexbykov.revoluttest.currencies.domain.CurrenciesRepository
import ru.alexbykov.revoluttest.currencies.presentation.CurrenciesInteractor

@Module
abstract class CurrenciesModule {

    @Binds
    abstract fun provideCurrenciesInteractor(currenciesInteractorImpl: CurrenciesInteractorImpl): CurrenciesInteractor


    @Binds
    abstract fun provideCurrenciesRepository(currenciesRepositoryImpl: CurrenciesRepositoryImpl): CurrenciesRepository
}