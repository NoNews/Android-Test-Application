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

    companion object {
        const val DEFAULT_CURRENCY = "EUR"
    }

    @Volatile
    private var currencyCount = 1.0F

    private val currencyStorage = databaseClient.currencies()


    override fun observeCurrencies(): Observable<CurrencyInfo> {
        return Observable.concat(getCurrenciesFromDatabase(), getCurrenciesFromNetwork())
    }

    private fun getCurrenciesFromNetwork(): Observable<CurrencyInfo?> {
        return Observable.interval(currenciesConfig.getUpdateTime(), TimeUnit.SECONDS)
            .observeOn(Schedulers.io())
            .startWith(0L)
            .map { currencyStorage.getMeta()?.baseCurrency ?: DEFAULT_CURRENCY}
            .flatMapSingle { networkClient.currencyEndpoint.getLatest(it) }
            .map { it -> convertResponseAndSave(it) }


    }


    private fun getCurrenciesFromDatabase(): Observable<CurrencyInfo?> {
        val observable = Observable.create<CurrencyInfo> {
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

        return observable
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
