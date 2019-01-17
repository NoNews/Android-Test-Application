package ru.alexbykov.revoluttest.currencies.data.storage


import androidx.room.*
import ru.alexbykov.revoluttest.currencies.data.storage.entity.Currency
import ru.alexbykov.revoluttest.currencies.data.storage.entity.CurrencyMeta

@Dao
interface CurrencyStorage {

    @Query("SELECT * FROM table_currency")
    fun getCurrencies(): List<Currency>

    @Query("SELECT * FROM table_currency_meta")
    fun getMeta(): CurrencyMeta?

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

    @Transaction
    fun updateAndGetMeta(new: CurrencyMeta): CurrencyMeta {
        val current = getMeta()
        if (current != null) {
            deleteMeta(current)
        }
        insertMeta(new)
        return getMeta()!!
    }


    @Insert
    fun insertMeta(meta: CurrencyMeta)

    @Insert
    fun insertCurrencies(currencies: List<Currency>)


    @Update
    fun updateCurrencies(currencies: List<Currency>)


    @Delete
    fun deleteMeta(meta: CurrencyMeta)

}