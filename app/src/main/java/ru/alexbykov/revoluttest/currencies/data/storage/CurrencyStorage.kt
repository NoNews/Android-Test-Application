package ru.alexbykov.revoluttest.currencies.data.storage


import androidx.annotation.WorkerThread
import androidx.room.*
import io.reactivex.Observable
import ru.alexbykov.revoluttest.currencies.data.storage.entity.Currency
import ru.alexbykov.revoluttest.currencies.data.storage.entity.CurrencyMeta

@Dao
interface CurrencyStorage {

    @WorkerThread
    @Query("SELECT * FROM table_currency WHERE name NOT LIKE :baseCurrency")
    fun getCurrencies(baseCurrency: String): List<Currency>

    @WorkerThread
    @Query("SELECT * FROM table_currency_meta")
    fun getMeta(): CurrencyMeta?

    @WorkerThread
    @Transaction
    fun updateAndGetCurrencies(
        new: List<Currency>,
        baseCurrency: String
    ): List<Currency> {
        val current = getCurrencies(baseCurrency)
        if (current.isEmpty()) {
            insertCurrencies(new)
            return getCurrencies(baseCurrency)
        }
        updateCurrencies(new)
        return getCurrencies(baseCurrency)
    }

    @WorkerThread
    @Transaction
    fun updateAndGetMeta(new: CurrencyMeta): CurrencyMeta {
        val current = getMeta()
        if (current != null) {
            deleteMeta(current)
        }
        insertMeta(new)
        return getMeta()!!
    }

    @WorkerThread
    @Insert
    fun insertMeta(meta: CurrencyMeta)

    @WorkerThread
    @Insert
    fun insertCurrencies(currencies: List<Currency>)

    @WorkerThread
    @Update
    fun updateCurrencies(currencies: List<Currency>)

    @WorkerThread
    @Delete
    fun deleteMeta(meta: CurrencyMeta)

    @Query("SELECT * FROM table_currency_meta")
    fun observeMeta(): Observable<CurrencyMeta>

}