package ru.alexbykov.revoluttest.currencies.data

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import ru.alexbykov.revoluttest.common.data.network.NetworkClient
import ru.alexbykov.revoluttest.common.data.storage.DatabaseClient
import ru.alexbykov.revoluttest.currencies.data.network.entity.CurrenciesResponse
import ru.alexbykov.revoluttest.currencies.data.storage.entity.Currency
import ru.alexbykov.revoluttest.currencies.data.storage.entity.CurrencyMeta
import ru.alexbykov.revoluttest.currencies.domain.CurrenciesRepository
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class CurrenciesRepositoryImpl
@Inject
internal constructor(
    private val networkClient: NetworkClient,
    private val databaseClient: DatabaseClient
) : CurrenciesRepository {

    companion object {
        const val LONG_POLLING_INTERVAL_IN_SECONDS = 1L
    }


    @Volatile
    private var currentCurrency = "EUR"
    @Volatile
    private var currencyCount = 1.0F

    private val currencyStorage = databaseClient.currencies()


    override fun observeCurrencies(): Observable<CurrencyInfo> {
        return Observable.concat(getCurrenciesFromDatabase().subscribeOn(Schedulers.io()), getCurrenciesFromNetwork())
    }

    private fun getCurrenciesFromNetwork(): Observable<CurrencyInfo?> {
        return Observable.interval(LONG_POLLING_INTERVAL_IN_SECONDS, TimeUnit.SECONDS)
            .startWith(0L)
            .flatMapSingle { networkClient.currencyEndpoint.getLatest(currentCurrency).subscribeOn(Schedulers.io()) }
            .map { it -> convertResponseAndSave(it) }

    }

    private fun getCurrenciesFromDatabase(): Observable<CurrencyInfo?> {

        return Observable.create {

            val currenciesStorage = databaseClient.currencies()
            val metaData = currenciesStorage.getMeta()
            if (metaData == null) {
                it.onComplete()
                return@create
            }

            val currencies = currenciesStorage.getCurrencies()
            if (currencies.isEmpty()) {
                it.onComplete()
                return@create
            }


            val currencyInfo = CurrencyInfo(
                meta = metaData,
                currencies = currencies
            )
            it.onNext(currencyInfo)
            it.onComplete()
        }
    }


    private fun convertResponseAndSave(response: CurrenciesResponse): CurrencyInfo {
        val currenciesStorage = databaseClient.currencies()
        val rates = response.rates

        val baseCurrency = response.base
        val metaData = currenciesStorage.updateAndGetMeta(
            CurrencyMeta(
                baseCurrency,
                response.date,
                currencyCount
            )
        )

        val currencies = rates.map { Currency(it.key, it.value) }
        val currenciesFromDatabase = currenciesStorage
            .updateAndGetCurrencies(currencies)

        return CurrencyInfo(
            meta = metaData,
            currencies = currenciesFromDatabase
        )
    }


    override fun changeCurrency(currency: String): Single<CurrencyInfo> {
        return Single.create {
            currentCurrency = currency
            val meta = currencyStorage.getMeta()
            meta!!.baseCurrency = currency
            val updatedMeta = currencyStorage.updateAndGetMeta(meta)
            val currencies = currencyStorage.getCurrencies()
            it.onSuccess(CurrencyInfo(updatedMeta, currencies))
        }
    }

    override fun changeBaseCurrencyValue(inputValue: Float): Single<CurrencyInfo> {
        return Single.create { it ->
            currencyCount = inputValue
            val meta = currencyStorage.getMeta()
            meta!!.lastUserInput = currencyCount
            val updatedMeta = currencyStorage.updateAndGetMeta(meta)

            val currencies = currencyStorage.getCurrencies()
            it.onSuccess(CurrencyInfo(updatedMeta, currencies))
        }
    }

}
