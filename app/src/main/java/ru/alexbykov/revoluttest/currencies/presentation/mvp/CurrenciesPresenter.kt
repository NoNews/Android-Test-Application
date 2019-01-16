package ru.alexbykov.revoluttest.currencies.presentation.mvp

import android.util.Log
import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import ru.alexbykov.revoluttest.currencies.data.CurrencyInfo
import ru.alexbykov.revoluttest.currencies.presentation.CurrenciesInteractor
import javax.inject.Inject


@InjectViewState
class CurrenciesPresenter
@Inject internal constructor(val currenciesInteractor: CurrenciesInteractor) : MvpPresenter<CurrenciesView>() {

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

    }

    private fun onCurrenciesChanged(it: CurrencyInfo?) {
        Log.d("CURRENCY", it.toString())
    }


}

