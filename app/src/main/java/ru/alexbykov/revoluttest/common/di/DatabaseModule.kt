package ru.alexbykov.revoluttest.common.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import ru.alexbykov.revoluttest.common.data.storage.DatabaseClient
import javax.inject.Singleton


@Module
class DatabaseModule {
    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "currencies_database"
    }


    @Provides
    @Singleton
    fun provideDatabaseClient(applicationContext: Context): DatabaseClient {
        return Room.databaseBuilder(
            applicationContext,
            DatabaseClient::class.java, DATABASE_NAME
        ).build()
    }


}