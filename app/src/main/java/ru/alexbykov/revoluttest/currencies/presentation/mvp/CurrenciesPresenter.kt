package ru.alexbykov.revoluttest.currencies.presentation.mvp

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import ru.alexbykov.revoluttest.currencies.presentation.CurrenciesInteractor
import javax.inject.Inject


@InjectViewState
class CurrenciesPresenter
@Inject internal constructor(val currenciesInteractor: CurrenciesInteractor) : MvpPresenter<CurrenciesView>() {

    override fun onFirstViewAttach() {
        viewState.showState(CurrenciesState.PROGRESS)
    }
}
