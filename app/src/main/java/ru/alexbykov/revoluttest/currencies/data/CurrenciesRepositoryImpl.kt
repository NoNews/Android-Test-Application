package ru.alexbykov.revoluttest.currencies.data

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import ru.alexbykov.revoluttest.common.data.network.NetworkClient
import ru.alexbykov.revoluttest.currencies.domain.CurrenciesRepository
import javax.inject.Inject


class CurrenciesRepositoryImpl
@Inject
internal constructor(val networkClient: NetworkClient) : CurrenciesRepository {


    private val currentCurrency = "EUR"


    override fun observeCurrencies(): Observable<CurrenciesResponse> {


        return networkClient.currencyEndpoint.getLatest(currentCurrency)
            .subscribeOn(Schedulers.io())
            .toObservable()
    }


    override fun changeCurrency(currency: String) {

    }

}
