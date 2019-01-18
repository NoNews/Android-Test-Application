package ru.alexbykov.revoluttest.currencies.data

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import ru.alexbykov.revoluttest.common.data.network.NetworkClient
import ru.alexbykov.revoluttest.common.data.storage.DatabaseClient
import ru.alexbykov.revoluttest.currencies.data.config.CurrenciesConfig
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
    private val databaseClient: DatabaseClient,
    private val currenciesConfig: CurrenciesConfig
) : CurrenciesRepository {


    private val currenciesStorage = databaseClient.currencies()

    override fun observeCurrencies(): Observable<CurrencyInfo> {
        return Observable.concat(getCurrenciesFromDatabase(), getCurrenciesFromNetwork())
    }

    private fun getCurrenciesFromNetwork(): Observable<CurrencyInfo?> {
        return Observable.interval(currenciesConfig.getUpdateTime(), TimeUnit.SECONDS)
            .observeOn(Schedulers.io())
            .startWith(0L)
            .map { currenciesStorage.getMeta()?.baseCurrency ?: currenciesConfig.getDeviceCurrency() }
            .flatMapSingle { networkClient.currencyEndpoint.getLatest(it) }
            .map { it -> convertResponseAndSave(it) }
    }

    private fun getCurrenciesFromDatabase(): Observable<CurrencyInfo?> {
        return Observable.create<CurrencyInfo> {
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
        currencies.add(Currency(baseCurrency, baseCurrencyFromDatabase?.value ?: Float.NEGATIVE_INFINITY))

        val currenciesFromDatabase = currenciesStorage.updateAndGetCurrencies(currencies)

        return CurrencyInfo(
            meta = newMeta,
            currencies = currenciesFromDatabase
        )
    }


    override fun changeCurrency(currency: String, baseCurrencyCount: Float): Single<CurrencyInfo> {
        return Single.create {
            val meta = currenciesStorage.getMeta()
            meta!!.baseCurrency = currency
            meta.baseCurrencyCount = baseCurrencyCount
            val updatedMeta = currenciesStorage.updateAndGetMeta(meta)
            val currencies = currenciesStorage.getCurrencies()
            it.onSuccess(CurrencyInfo(updatedMeta, currencies))
        }
    }

    override fun changeBaseCurrencyValue(baseCurrencyCount: Float): Single<CurrencyInfo> {
        return Single.create { it ->
            val meta = currenciesStorage.getMeta()
            meta!!.baseCurrencyCount = baseCurrencyCount
            val updatedMeta = currenciesStorage.updateAndGetMeta(meta)
            val currencies = currenciesStorage.getCurrencies()

            it.onSuccess(CurrencyInfo(updatedMeta, currencies))
        }
    }

}
