package ru.alexbykov.revoluttest.mvp

import com.nhaarman.mockitokotlin2.never
import com.nhaarman.mockitokotlin2.times
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import junit.framework.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import ru.alexbykov.revoluttest.TrampolineSchedulerRule
import ru.alexbykov.revoluttest.currencies.domain.entity.CurrencyBusinessResponse
import ru.alexbykov.revoluttest.currencies.domain.entity.CurrencyDetail
import ru.alexbykov.revoluttest.currencies.presentation.CurrenciesInteractor
import ru.alexbykov.revoluttest.currencies.presentation.mvp.CurrenciesPresenter
import ru.alexbykov.revoluttest.currencies.presentation.mvp.CurrenciesState
import ru.alexbykov.revoluttest.currencies.presentation.mvp.CurrenciesView
import ru.alexbykov.revoluttest.currencies.presentation.mvp.`CurrenciesView$$State`
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class CurrenciesPresenterTest {


    @Rule
    @JvmField
    val rule = TrampolineSchedulerRule()

    @Mock
    private lateinit var interactor: CurrenciesInteractor

    @Mock
    private lateinit var view: CurrenciesView
    @Mock
    private lateinit var viewState: `CurrenciesView$$State`

    private val stubRusResponse = CurrencyBusinessResponse("RUB", "2017-12-12", Collections.emptyList())
    private val stubEurResponse = CurrencyBusinessResponse("EUR", "2017-12-12", Collections.emptyList())

    private val stubCurrency = CurrencyDetail("RUB", "RUS", 4444.0, 101001.0)


    private lateinit var presenter: CurrenciesPresenter

    @Before
    fun setUp() {
        presenter = CurrenciesPresenter(interactor)
        presenter.setViewState(viewState)
    }

    @Test
    fun onFirstViewAttachedStateProgressTest() {
        mockObserveCurrencies(false)
        presenter.attachView(view)

        verify(viewState).showState(CurrenciesState.PROGRESS)
        verify(viewState, never()).showState(CurrenciesState.DATA)
    }


    @Test
    fun observeCurrencyTest() {
        mockObserveCurrencies(true)
        presenter.attachView(view)

        verify(viewState).showState(CurrenciesState.DATA)
        verify(viewState).updateCurrencies(stubRusResponse.currencies)
    }


    @Test
    fun changeBaseCurrencyTheSameCurrencyTest() {
        mockObserveCurrencies(true)

        presenter.attachView(view)

        presenter.onClickInput(stubCurrency)

        //only on attach view, not in chang
        verify(interactor, never()).changeBaseCurrency(stubCurrency)
        //only on attach view, not in change
        verify(viewState, times(1)).showState(CurrenciesState.DATA)
        verify(viewState, times(1)).updateCurrencies(Mockito.any())
    }


    @Test
    fun changeBaseCurrencyNotTheSameCurrencyTest() {
        mockObserveCurrencies(true)
        `when`(interactor.changeBaseCurrency(stubCurrency)).thenReturn(Completable.complete())
        `when`(interactor.observeCurrencies()).thenReturn(Observable.just(stubEurResponse))
        val currencyDetail = CurrencyDetail("EUR", "Euro", 100.9, 100.0)

        presenter.attachView(view)
        presenter.onClickInput(stubCurrency)


        //first time -- on attach, second -- when changed
        verify(viewState, times(2)).showState(CurrenciesState.DATA)
        verify(viewState, times(2)).updateCurrencies(stubEurResponse.currencies)
    }


    @Test
    fun onInputTextChangedTest() {
        mockObserveCurrencies(false)
        `when`(interactor.changeBaseCurrencyValue("RUB")).thenReturn(Single.just(stubEurResponse))

        presenter.attachView(view)
        presenter.onInputTextChanged("RUB")

        //first time -- on attach, second -- when changed
        verify(viewState, times(1)).showState(CurrenciesState.DATA)
        verify(viewState, times(1)).updateCurrencies(stubEurResponse.currencies)
    }


    @Test
    fun hasNoActiveDisposablesTest() {
        mockObserveCurrencies(true)
        presenter.detachView(view)
        assertTrue(presenter.hasNoActiveDisposables())
    }


    private fun mockObserveCurrencies(mustEmit: Boolean) {
        val observable =
            if (mustEmit) {
                Observable.just(stubRusResponse)
            } else {
                Observable.never()
            }

        `when`(interactor.observeCurrencies()).thenReturn(observable)
    }


}