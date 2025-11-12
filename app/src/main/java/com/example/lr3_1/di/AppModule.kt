package com.example.lr3_1.di

import com.example.lr3_1.data.repository.CelebrityRepository
import com.example.lr3_1.data.repository.CelebrityRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCelebrityRepository(
        celebrityRepositoryImpl: CelebrityRepositoryImpl
    ): CelebrityRepository
}

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient {
        return HttpClient(Android) {
            // Здесь можно добавить конфигурацию, например, ContentNegotiation,
            // но для HTML он не нужен.
            followRedirects = true
            engine {
                connectTimeout = 10_000
                socketTimeout = 10_000
            }
        }
    }
}