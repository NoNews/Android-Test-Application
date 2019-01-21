package ru.alexbykov.revoluttest.business

import com.nhaarman.mockitokotlin2.verify
import io.reactivex.Completable
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
    private val stubCurrencyCode = stubCurrency.code

    @Before
    fun setUp() {
        interactor = CurrenciesInteractorImpl(repository)
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
    fun changeBaseCurrencyValueTest() {
        val newCurrencyCount = "100"
        val newCurrencyCountDouble = newCurrencyCount.toDouble()

        //provide 2 currencies based ruble
        val newCurrencyInfoFromRepository = StubProvider.provideCurrencyInfoBasedRuble(newCurrencyCountDouble)
        `when`(repository.changeBaseCurrencyValue(Mockito.anyDouble())).thenReturn(
            Single.just(newCurrencyInfoFromRepository)
        )

        val businessResponse = interactor.changeBaseCurrencyValue(newCurrencyCount)
            .test()
            .assertValue { it.currencies.size == 2 }
            .assertValue { it.baseCurrency == StubProvider.RUB_CODE }
            .assertValue { it.lastUpdateTime == StubProvider.STUB_DATE }
            .assertNoErrors()
            .values()[0]

        val currencies = businessResponse.currencies

        val eurCurrency = currencies.first()

        with(eurCurrency) {
            assertEquals(displayName, StubProvider.EUR_NAME)
            assertEquals(code, StubProvider.EUR_CODE)
            assertEquals(exchangeRate, StubProvider.EUR_EXCHANGE_RATE)
            assertEquals(newCurrencyCountDouble * StubProvider.EUR_EXCHANGE_RATE, calculatedValue)
        }

        val usdCurrency = currencies.last()


        with(usdCurrency) {
            assertEquals(displayName, StubProvider.USD_NAME)
            assertEquals(code, StubProvider.USD_CODE)
            assertEquals(exchangeRate, StubProvider.USD_EXCHANGE_RATE)
            assertEquals(newCurrencyCountDouble * StubProvider.USD_EXCHANGE_RATE, calculatedValue)
        }
    }


}