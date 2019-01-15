package ru.alexbykov.revoluttest.common.di

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import ru.alexbykov.revoluttest.common.data.NetworkClient

import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideRestApi(okHttpClient: OkHttpClient): NetworkClient {
        return NetworkClient(okHttpClient)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .build()
    }
}
