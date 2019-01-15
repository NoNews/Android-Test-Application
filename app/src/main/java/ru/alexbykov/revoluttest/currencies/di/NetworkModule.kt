package ru.alexbykov.revoluttest.currencies.di

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import ru.alexbykov.revoluttest.currencies.data.RestApi

import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideRestApi(okHttpClient: OkHttpClient): RestApi {
        return RestApi(okHttpClient)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .build()
    }
}
