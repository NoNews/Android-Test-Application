package ru.alexbykov.revoluttest.currencies.data.storage.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "table_currency")
data class Currency(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "value")
    val value: Float
)