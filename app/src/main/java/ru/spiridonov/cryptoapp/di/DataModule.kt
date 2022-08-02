package ru.spiridonov.cryptoapp.di

import android.app.Application
import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.spiridonov.cryptoapp.data.database.AppDatabase
import ru.spiridonov.cryptoapp.data.database.CoinInfoDao
import ru.spiridonov.cryptoapp.data.network.ApiFactory
import ru.spiridonov.cryptoapp.data.network.ApiService
import ru.spiridonov.cryptoapp.data.repository.CoinRepositoryImpl
import ru.spiridonov.cryptoapp.domain.CoinRepository

@Module
interface DataModule {

    @Binds
    @ApplicationScope
    fun bindCoinRepository(impl: CoinRepositoryImpl): CoinRepository

    companion object {

        @Provides
        @ApplicationScope
        fun provideCoinInfoDao(
            application: Application
        ): CoinInfoDao {
            return AppDatabase.getInstance(application).coinPriceInfoDao()
        }

        @Provides
        @ApplicationScope
        fun provideApiService():ApiService{
            return ApiFactory.apiService
        }
    }
}
