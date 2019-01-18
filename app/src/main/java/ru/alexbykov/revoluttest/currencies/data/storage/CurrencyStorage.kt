package ru.alexbykov.revoluttest.currencies.data.storage


import androidx.annotation.WorkerThread
import androidx.room.*
import io.reactivex.Observable
import ru.alexbykov.revoluttest.currencies.data.storage.entity.Currency
import ru.alexbykov.revoluttest.currencies.data.storage.entity.CurrencyMeta

@Dao
interface CurrencyStorage {

    @WorkerThread
    @Query("SELECT * FROM table_currency")
    fun getCurrencies(): List<Currency>

    @WorkerThread
    @Query("SELECT * FROM table_currency_meta")
    fun getMeta(): CurrencyMeta?

    @WorkerThread
    @Transaction
    fun updateAndGetCurrencies(new: List<Currency>): List<Currency> {
        val current = getCurrencies()
        if (current.isEmpty()) {
            insertCurrencies(new)
            return getCurrencies()
        }
        updateCurrencies(new)
        return getCurrencies()
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