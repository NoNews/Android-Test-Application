package ru.alexbykov.revoluttest.common.di

import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import ru.alexbykov.revoluttest.common.data.network.InternetInfoProvider
import ru.alexbykov.revoluttest.common.data.network.InternetInfoProviderImpl
import ru.alexbykov.revoluttest.common.data.network.NetworkClient
import ru.alexbykov.revoluttest.common.data.network.NetworkClientImpl
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
class NetworkModule {

    companion object {
        private const val TIMEOUT_IN_SECONDS = 2L
    }

    @Provides
    @Singleton
    fun provideRestApi(okHttpClient: OkHttpClient): NetworkClient = NetworkClientImpl(okHttpClient)

    @Provides
    @Singleton
    fun internetInfoProvider(): InternetInfoProvider = InternetInfoProviderImpl()

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .retryOnConnectionFailure(false)
            .callTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
            .connectTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
            .build()
    }
}
