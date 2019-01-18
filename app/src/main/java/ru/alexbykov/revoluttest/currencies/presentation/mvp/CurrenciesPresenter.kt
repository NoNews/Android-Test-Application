package ru.alexbykov.revoluttest.currencies.presentation.mvp

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.alexbykov.revoluttest.currencies.domain.entity.CurrencyBusinessResponse
import ru.alexbykov.revoluttest.currencies.domain.entity.CurrencyDetail
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
        it?.printStackTrace()
        viewState.showState(CurrenciesState.WAITING_FOR_CONNECTION)
    }


    fun onClickInput(currency: CurrencyDetail) {
        val subscribe = currenciesInteractor.changeBaseCurrency(currency)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                onCurrenciesChanged(it)
            }, {
                handleCurrenciesError(it)
            })

    }

    fun onInputTextChanged(it: String) {
        val subscribe = currenciesInteractor.changeBaseCurrencyValue(it)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                onCurrenciesChanged(it)
            }, {
                handleCurrenciesError(it)
            })
    }

    private fun onCurrenciesChanged(it: CurrencyBusinessResponse) {
        viewState.showState(CurrenciesState.DATA)
        viewState.updateCurrencies(it.currencies)
    }
}

