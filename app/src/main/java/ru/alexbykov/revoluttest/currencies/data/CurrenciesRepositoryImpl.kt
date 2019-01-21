package ru.alexbykov.revoluttest.currencies.data

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import ru.alexbykov.revoluttest.common.data.network.InternetInfoProvider
import ru.alexbykov.revoluttest.common.data.network.NetworkClient
import ru.alexbykov.revoluttest.common.data.storage.DatabaseClient
import ru.alexbykov.revoluttest.currencies.data.config.CurrenciesConfig
import ru.alexbykov.revoluttest.currencies.data.network.entity.CurrenciesResponse
import ru.alexbykov.revoluttest.currencies.data.storage.entity.Currency
import ru.alexbykov.revoluttest.currencies.data.storage.entity.CurrencyMeta
import ru.alexbykov.revoluttest.currencies.domain.CurrenciesRepository
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class CurrenciesRepositoryImpl
@Inject
internal constructor(
    private val networkClient: NetworkClient,
    private val databaseClient: DatabaseClient,
    private val currenciesConfig: CurrenciesConfig,
    private val internetProvider: InternetInfoProvider
) : CurrenciesRepository {


    override fun observeCurrencies(): Observable<CurrencyInfo> {
        return Observable.concat(getCurrenciesFromDatabase(), getCurrenciesFromNetwork())
    }

    private fun getCurrenciesFromNetwork(): Observable<CurrencyInfo?> {
        return Observable.interval(currenciesConfig.getUpdateTime(), TimeUnit.SECONDS)
            .startWith(0)
            .observeOn(Schedulers.io())
            .map { databaseClient.currencies().getMeta()?.baseCurrency ?: currenciesConfig.getDeviceCurrency() }
            .flatMapSingle { networkClient.getCurrencyEndpoint().getLatest(it) }
            .retry()
            .map { it -> convertResponseAndSave(it) }
    }

    private fun getCurrenciesFromDatabase(): Observable<CurrencyInfo?> {
        return Observable.create<CurrencyInfo> {
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
        }.subscribeOn(Schedulers.io())
    }


    private fun convertResponseAndSave(response: CurrenciesResponse): CurrencyInfo {
        val currenciesStorage = databaseClient.currencies()
        val rates = response.rates
        val baseCurrency = response.base

        val currentMeta = currenciesStorage.getMeta()
        val baseCurrencyCount = currentMeta?.baseCurrencyCount ?: currenciesConfig.getBaseCurrencyCount()

        val newMeta = currenciesStorage.updateAndGetMeta(
            CurrencyMeta(
                baseCurrency,
                response.date,
                baseCurrencyCount
            )
        )

        val currencies = rates.map { Currency(it.key, it.value) }.toMutableList()
        val baseCurrencyFromDatabase = currenciesStorage.getCurrency(baseCurrency)
        currencies.add(Currency(baseCurrency, baseCurrencyFromDatabase?.value ?: Double.NEGATIVE_INFINITY))

        val currenciesFromDatabase = currenciesStorage.updateAndGetCurrencies(currencies)

        return CurrencyInfo(
            meta = newMeta,
            currencies = currenciesFromDatabase
        )
    }


    override fun changeCurrency(currencyName: String): Completable {

        return Completable.create {
            val currenciesStorage = databaseClient.currencies()
            val meta = currenciesStorage.getMeta()
            val currency = currenciesStorage.getCurrency(currencyName)
            val oldCount = meta!!.baseCurrencyCount
            meta.baseCurrency = currency!!.name
            meta.baseCurrencyCount = currency.value * oldCount
            currenciesStorage.updateMeta(meta)
            it.onComplete()
        }.subscribeOn(Schedulers.io())
    }

    override fun changeBaseCurrencyValue(baseCurrencyCount: Double): Single<CurrencyInfo> {
        return Single.create<CurrencyInfo> {
            val currenciesStorage = databaseClient.currencies()
            val meta = currenciesStorage.getMeta()
            meta!!.baseCurrencyCount = baseCurrencyCount
            val updatedMeta = currenciesStorage.updateAndGetMeta(meta)
            if (internetProvider.isInternetAvailable) {
                val currencies = currenciesStorage.getCurrencies()
                it.onSuccess(CurrencyInfo(updatedMeta, currencies))
            } else {
                it.onError(SocketTimeoutException())
            }
        }.subscribeOn(Schedulers.io())
    }

}
