package ru.alexbykov.revoluttest.data

import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import ru.alexbykov.revoluttest.StubProvider
import ru.alexbykov.revoluttest.TrampolineSchedulerRule
import ru.alexbykov.revoluttest.common.data.network.InternetInfoProvider
import ru.alexbykov.revoluttest.common.data.network.NetworkClient
import ru.alexbykov.revoluttest.common.data.storage.DatabaseClient
import ru.alexbykov.revoluttest.currencies.data.CurrenciesRepositoryImpl
import ru.alexbykov.revoluttest.currencies.data.config.CurrenciesConfig
import ru.alexbykov.revoluttest.currencies.data.storage.CurrencyStorage
import ru.alexbykov.revoluttest.currencies.domain.CurrenciesRepository
import java.net.SocketTimeoutException

@RunWith(MockitoJUnitRunner::class)
class CurrenciesRepositoryTest {

    @Rule
    @JvmField
    val rule = TrampolineSchedulerRule()

    @Mock
    private lateinit var networkClient: NetworkClient
    @Mock
    private lateinit var databaseClient: DatabaseClient
    @Mock
    private lateinit var currenciesConfig: CurrenciesConfig
    @Mock
    private lateinit var internetProvider: InternetInfoProvider

    @Mock
    lateinit var currenciesStorage: CurrencyStorage

    private lateinit var repository: CurrenciesRepository

    @Before
    fun setUp() {
        repository = CurrenciesRepositoryImpl(networkClient, databaseClient, currenciesConfig, internetProvider)
        `when`(databaseClient.currencies()).thenReturn(currenciesStorage)
    }


    @Test
    fun changeBaseCurrencyValueSuccessTest() {
        val currencies = StubProvider.provideCurrencies()
        val stubMeta = StubProvider.provideMeta()
        `when`(internetProvider.isInternetAvailable).thenReturn(true)
        `when`(databaseClient.currencies().getMeta()).thenReturn(stubMeta)
        `when`(databaseClient.currencies().updateAndGetMeta(stubMeta)).thenReturn(stubMeta)
        `when`(databaseClient.currencies().getCurrencies()).thenReturn(currencies)
        repository.changeBaseCurrencyValue(30.0)
            .test()
            .assertComplete()
            .assertNoErrors()
            .assertValue { it.currencies.size == 2 }
            .assertValue { it.meta == stubMeta }
            .values()

    }


    @Test
    fun changeBaseCurrencyValueErrorTest() {
        val stubMeta = StubProvider.provideMeta()
        `when`(internetProvider.isInternetAvailable).thenReturn(false)
        `when`(databaseClient.currencies().getMeta()).thenReturn(stubMeta)
        `when`(databaseClient.currencies().updateAndGetMeta(stubMeta)).thenReturn(stubMeta)
        repository.changeBaseCurrencyValue(30.0)
            .test()
            .assertError(SocketTimeoutException::class.java)
    }
}