package ru.alexbykov.revoluttest.common.data.storage

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.alexbykov.revoluttest.currencies.data.storage.CurrencyStorage
import ru.alexbykov.revoluttest.currencies.data.storage.entity.Currency
import ru.alexbykov.revoluttest.currencies.data.storage.entity.CurrencyMeta

@Database(entities = [CurrencyMeta::class, Currency::class], version = 1,exportSchema = false)
abstract class DatabaseClient : RoomDatabase() {

    abstract fun currencies(): CurrencyStorage

}