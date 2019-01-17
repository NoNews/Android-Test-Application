package ru.alexbykov.revoluttest.currencies.data.storage


import androidx.room.*
import ru.alexbykov.revoluttest.currencies.data.storage.entity.Currency
import ru.alexbykov.revoluttest.currencies.data.storage.entity.CurrencyMeta

@Dao
interface CurrencyStorage {

    @Query("SELECT * FROM table_currency WHERE name NOT LIKE :baseCurrency")
    fun getCurrencies(baseCurrency: String): List<Currency>

    @Query("SELECT * FROM table_currency_meta")
    fun getMeta(): CurrencyMeta?

    @Transaction
    fun updateAndGetCurrencies(new: List<Currency>,
                               baseCurrency: String): List<Currency> {
        val current = getCurrencies(baseCurrency)
        if (current.isEmpty()) {
            insertCurrencies(new)
            return getCurrencies(baseCurrency)
        }
        updateCurrencies(new)
        return getCurrencies(baseCurrency)
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