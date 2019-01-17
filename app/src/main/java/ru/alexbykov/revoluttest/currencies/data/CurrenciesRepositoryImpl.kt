package ru.alexbykov.revoluttest.currencies.data

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import ru.alexbykov.revoluttest.common.data.network.NetworkClient
import ru.alexbykov.revoluttest.currencies.data.network.entity.CurrenciesResponse
import ru.alexbykov.revoluttest.currencies.data.common.entity.Currency
import ru.alexbykov.revoluttest.currencies.data.common.entity.CurrencyInfo
import ru.alexbykov.revoluttest.currencies.domain.CurrenciesRepository
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class CurrenciesRepositoryImpl
@Inject
internal constructor(val networkClient: NetworkClient) : CurrenciesRepository {


    private var currentCurrency = "EUR"
    private var currencyCount = 1.0


    override fun observeCurrencies(): Observable<CurrencyInfo> {
        val pollingObservable = Observable.concat(getCurrenciesFromDatabase(), getCurrenciesFromNetwork())

        return pollingObservable
    }

    private fun getCurrenciesFromNetwork(): Observable<CurrencyInfo>? {
        return Observable.interval(1, TimeUnit.SECONDS)
            .startWith(0L)
            .flatMapSingle {
                networkClient.currencyEndpoint.getLatest(currentCurrency).subscribeOn(Schedulers.io())
            }
            .map { it -> convertResponseAndCache(it) }

    }

    private fun getCurrenciesFromDatabase(): Observable<CurrencyInfo>? {

        return Observable.create {
            it.onComplete()
        }
    }


    private fun convertResponseAndCache(response: CurrenciesResponse): CurrencyInfo {

        val selectedCurrency = Currency(response.base, currencyCount)
        val date = response.date

        val currencies = response.rates
            .map { Currency(it.key, it.value * currencyCount) }
            .toList()

        return CurrencyInfo(
            lastUpdateDate = date,
            defaultCurrency = selectedCurrency,
            currencies = currencies
        )
    }


    override fun changeCurrency(currency: String) {
        currentCurrency = currency
    }

    override fun changeCurrencyCount(count: Double) {
        currencyCount = count
    }
}
