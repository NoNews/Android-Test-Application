package ru.alexbykov.revoluttest.currencies.presentation.mvp

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import javax.inject.Inject


@InjectViewState
class CurrenciesPresenter
@Inject internal constructor() : MvpPresenter<CurrenciesView>() {


    override fun onFirstViewAttach() {

    }

}
