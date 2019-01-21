package ru.alexbykov.revoluttest.business

import com.nhaarman.mockitokotlin2.verify
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import ru.alexbykov.revoluttest.StubProvider
import ru.alexbykov.revoluttest.TrampolineSchedulerRule
import ru.alexbykov.revoluttest.currencies.domain.CurrenciesInteractorImpl
import ru.alexbykov.revoluttest.currencies.domain.CurrenciesRepository
import ru.alexbykov.revoluttest.currencies.domain.entity.CurrencyBusinessResponse
import ru.alexbykov.revoluttest.currencies.presentation.CurrenciesInteractor


@RunWith(MockitoJUnitRunner::class)
class CurrenciesInteractorTest {


    @Rule
    @JvmField
    val rule = TrampolineSchedulerRule()


    @Mock
    private lateinit var repository: CurrenciesRepository
    private lateinit var interactor: CurrenciesInteractor

    private val stubCurrency = StubProvider.stubCurrencyDetail()

    @Before
    fun setUp() {
        interactor = CurrenciesInteractorImpl(repository)
    }


    @Test
    fun observeCurrenciesTest() {
        val expectedCurrency = 100.0
        val newCurrencyInfoFromRepository = StubProvider.provideCurrencyInfoBasedRuble(expectedCurrency)
        `when`(repository.observeCurrencies()).thenReturn(Observable.just(newCurrencyInfoFromRepository))
        interactor.observeCurrencies()
        testCurrenciesResponse(interactor.observeCurrencies(), expectedCurrency)
    }


    @Test
    fun changeBaseCurrencyTest() {
        val code = stubCurrency.code
        `when`(repository.changeCurrency(code)).thenReturn(Completable.complete())

        interactor.changeBaseCurrency(stubCurrency)
            .test()
            .assertComplete()

        verify(repository).changeCurrency(code)
    }


    @Test
    fun changeBaseCurrencyValueValidTest() {
        updateBaseCurrencyValueTest("100", 100.0)
    }

    @Test
    fun changeBaseCurrencyValueNoValidTest() {
        updateBaseCurrencyValueTest("", 0.0)
        updateBaseCurrencyValueTest("0", 0.0)
    }


    private fun updateBaseCurrencyValueTest(newValue: String, valueAfterValidate: Double) {


        //provide 2 currencies based ruble
        val newCurrencyInfoFromRepository = StubProvider.provideCurrencyInfoBasedRuble(valueAfterValidate)
        `when`(repository.changeBaseCurrencyValue(Mockito.anyDouble())).thenReturn(
            Single.just(newCurrencyInfoFromRepository)
        )
        testCurrenciesResponse(interactor.changeBaseCurrencyValue(newValue).toObservable(), valueAfterValidate)
    }

    private fun testCurrenciesResponse(source: Observable<CurrencyBusinessResponse>, expectedCurrency: Double) {
        val response = source.test()
            .assertValue { it.currencies.size == 2 }
            .assertValue { it.baseCurrency == StubProvider.RUB_CODE }
            .assertValue { it.lastUpdateTime == StubProvider.STUB_DATE }
            .assertNoErrors()
            .values()[0]


        val currencies = response.currencies
        val eurCurrency = currencies.first()

        with(eurCurrency) {
            assertEquals(displayName, StubProvider.EUR_NAME)
            assertEquals(code, StubProvider.EUR_CODE)
            assertEquals(exchangeRate, StubProvider.EUR_EXCHANGE_RATE)
            assertEquals(expectedCurrency * StubProvider.EUR_EXCHANGE_RATE, calculatedValue)
        }

        val usdCurrency = currencies.last()


        with(usdCurrency) {
            assertEquals(displayName, StubProvider.USD_NAME)
            assertEquals(code, StubProvider.USD_CODE)
            assertEquals(exchangeRate, StubProvider.USD_EXCHANGE_RATE)
            assertEquals(expectedCurrency * StubProvider.USD_EXCHANGE_RATE, calculatedValue)
        }

    }

}