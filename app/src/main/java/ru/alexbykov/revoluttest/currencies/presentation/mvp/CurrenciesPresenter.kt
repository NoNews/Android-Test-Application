package ru.alexbykov.revoluttest.currencies.presentation.mvp

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ru.alexbykov.revoluttest.currencies.data.storage.entity.Currency
import ru.alexbykov.revoluttest.currencies.domain.CurrencyBusinessResponse
import ru.alexbykov.revoluttest.currencies.presentation.CurrenciesInteractor
import javax.inject.Inject


@InjectViewState
class CurrenciesPresenter
@Inject internal constructor(private val currenciesInteractor: CurrenciesInteractor) : MvpPresenter<CurrenciesView>() {


    private var response: CurrencyBusinessResponse? = null

    private var currency: String? = null

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

    private fun onCurrenciesChanged(it: CurrencyBusinessResponse) {
        response = it
        viewState.showState(CurrenciesState.DATA)
        viewState.updateCurrencies(it.currencies)
    }

    fun onClickInput(currency: Currency) {
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
}

