package ru.alexbykov.revoluttest.currencies.data

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import ru.alexbykov.revoluttest.common.data.network.NetworkClient
import ru.alexbykov.revoluttest.currencies.domain.CurrenciesRepository
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class CurrenciesRepositoryImpl
@Inject
internal constructor(val networkClient: NetworkClient) : CurrenciesRepository {

    private val currentCurrency = "EUR"
    private val currencyCount = 10


    override fun observeCurrencies(): Observable<CurrencyInfo> {
        return Observable.concat(getCurrenciesFromDatabase(), getCurrenciesFromNetwork())
    }


    private fun getCurrenciesFromNetwork(): Observable<CurrencyInfo>? {

        return Observable.interval(1, TimeUnit.SECONDS)
            .startWith(0L)
            .flatMapSingle { networkClient.currencyEndpoint.getLatest(currentCurrency).subscribeOn(Schedulers.io()) }
            .map { it -> convertResponse(it) }
    }

    private fun getCurrenciesFromDatabase(): Observable<CurrencyInfo>? {

        return Observable.create {
            it.onComplete()
        }
    }


    private fun convertResponse(response: CurrenciesResponse): CurrencyInfo {

        val selectedCurrency = Currency(response.base, 1.0)
        val date = response.date

        val currencies = response.rates
            .map { Currency(it.key, it.value) }
            .toList()


        return CurrencyInfo(
            lastUpdateDate = date,
            defaultCurrency = selectedCurrency,
            currencies = currencies
        )
    }


    override fun changeCurrency(currency: String) {

    }

    override fun changeCurrencyCount(count: Double) {

    }

}
