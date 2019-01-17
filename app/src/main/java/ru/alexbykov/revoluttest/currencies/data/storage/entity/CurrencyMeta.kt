package ru.alexbykov.revoluttest.currencies.data.storage.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "table_currency_meta")
data class CurrencyMeta(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "default_currency_name")
    val defaultCurrencyName: String,

    @ColumnInfo(name = "update_time")
    val updateTime: String,

    @ColumnInfo(name = "last_user_input")
    val lastUserInput: Double

)
