package ru.alexbykov.revoluttest.currencies.data

import ru.alexbykov.revoluttest.common.data.NetworkClient
import ru.alexbykov.revoluttest.currencies.domain.CurrenciesRepository
import javax.inject.Inject


class CurrenciesRepositoryImpl
@Inject
internal constructor(val networkClient: NetworkClient) : CurrenciesRepository {

}
