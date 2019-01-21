package ru.alexbykov.revoluttest.currencies.presentation.mvp

import com.arellomobile.mvp.InjectViewState
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import ru.alexbykov.revoluttest.common.presentation.BaseMvpPresenter
import ru.alexbykov.revoluttest.currencies.domain.entity.CurrencyBusinessResponse
import ru.alexbykov.revoluttest.currencies.domain.entity.CurrencyDetail
import ru.alexbykov.revoluttest.currencies.presentation.CurrenciesInteractor
import javax.inject.Inject


@InjectViewState
class CurrenciesPresenter
@Inject internal constructor(private val currenciesInteractor: CurrenciesInteractor) :
    BaseMvpPresenter<CurrenciesView>() {

    private var currenciesDisposable: Disposable? = null
    private var hasData = false

    private var currentBaseCurrency: String? = null

    override fun onFirstViewAttach() {
        viewState.showState(CurrenciesState.PROGRESS)
    }

    override fun onAttach() {
        currenciesDisposable = currenciesInteractor.observeCurrencies()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                hasData = true
                onCurrenciesChanged(it)
            }
    }

    fun onClickInput(currency: CurrencyDetail) {
        if (currency.code == currentBaseCurrency) {
            return
        }
        disposeCurrencies()
        currenciesDisposable = currenciesInteractor.changeBaseCurrency(currency)
            .andThen(currenciesInteractor.observeCurrencies())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { onCurrenciesChanged(it) }
    }


    fun onInputTextChanged(it: String) {
        val disposable = currenciesInteractor.changeBaseCurrencyValue(it)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ onCurrenciesChanged(it) }, { it.printStackTrace() })
        disposeOnDetach(disposable)
    }

    private fun onCurrenciesChanged(it: CurrencyBusinessResponse) {
        currentBaseCurrency = it.baseCurrency
        viewState.showState(CurrenciesState.DATA)
        viewState.updateCurrencies(it.currencies)
    }

    private fun disposeCurrencies() {
        currenciesDisposable?.dispose()
        currenciesDisposable = null
    }

    override fun onDetach() {
        disposeCurrencies()
    }
}

