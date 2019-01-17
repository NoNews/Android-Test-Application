package ru.alexbykov.revoluttest.currencies.data

import io.reactivex.Observable
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


    private var currentCurrency = "EUR"
    private var currencyCount = 1.0


    override fun observeCurrencies(): Observable<CurrencyInfo> {
        return Observable.concat(getCurrenciesFromDatabase().subscribeOn(Schedulers.io()), getCurrenciesFromNetwork())
    }

    private fun getCurrenciesFromNetwork(): Observable<CurrencyInfo?> {
        return Observable.interval(1, TimeUnit.SECONDS)
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

            val currencies = currenciesStorage.getCurrencies(metaData.defaultCurrencyName)
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

        val baseCurrency = response.base
        val metaData = currenciesStorage.updateAndGetMeta(
            CurrencyMeta(
                baseCurrency,
                response.date,
                currencyCount
            )
        )

        val currencies = response.rates
            .map { Currency(it.key, it.value * currencyCount) }
            .toList()

        val currenciesFromDatabase = currenciesStorage
            .updateAndGetCurrencies(currencies,baseCurrency)

        return CurrencyInfo(
            meta = metaData,
            currencies = currenciesFromDatabase
        )
    }


    override fun changeCurrency(currency: String) {
        currentCurrency = currency
    }

    override fun changeCurrencyCount(count: Double) {
        currencyCount = count
    }
}
