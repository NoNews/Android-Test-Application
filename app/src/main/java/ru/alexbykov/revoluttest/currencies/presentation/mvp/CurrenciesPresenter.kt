package ru.alexbykov.revoluttest.currencies.presentation.mvp

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import ru.alexbykov.revoluttest.currencies.data.CurrencyInfo
import ru.alexbykov.revoluttest.currencies.presentation.CurrenciesInteractor
import javax.inject.Inject


@InjectViewState
class CurrenciesPresenter
@Inject internal constructor(private val currenciesInteractor: CurrenciesInteractor) : MvpPresenter<CurrenciesView>() {

    override fun onFirstViewAttach() {
        viewState.showState(CurrenciesState.PROGRESS)


        var disposable = currenciesInteractor.observeCurrencies()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                onCurrenciesChanged(it)
            }, {
                handleCurrenciesError(it)
            })


    }

    private fun handleCurrenciesError(it: Throwable?) {
        viewState.showState(CurrenciesState.WAITING_FOR_CONNECTION)
    }

    private fun onCurrenciesChanged(it: CurrencyInfo?) {
        viewState.showState(CurrenciesState.DATA)
        viewState.updateCurrencies(it?.currencies!!)
    }


}

