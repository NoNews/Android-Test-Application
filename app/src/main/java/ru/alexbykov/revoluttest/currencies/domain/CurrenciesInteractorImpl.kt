package ru.alexbykov.revoluttest.currencies.domain

import ru.alexbykov.revoluttest.currencies.presentation.CurrenciesInteractor
import javax.inject.Inject

class CurrenciesInteractorImpl
@Inject internal constructor(val currenciesRepository: CurrenciesRepository) : CurrenciesInteractor {

}