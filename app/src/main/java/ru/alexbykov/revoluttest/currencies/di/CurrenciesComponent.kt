package ru.alexbykov.revoluttest.currencies.di

import dagger.Subcomponent
import ru.alexbykov.revoluttest.currencies.presentation.mvp.CurrenciesPresenter

@CurrenciesScope
@Subcomponent(modules = [CurrenciesModule::class])
interface CurrenciesComponent {

    @Subcomponent.Builder
    interface Builder {
        fun build(): CurrenciesComponent
    }

    val currenciesPresenter: CurrenciesPresenter

}
