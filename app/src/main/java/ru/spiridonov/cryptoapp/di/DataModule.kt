package ru.spiridonov.cryptoapp.di

import android.app.Application
import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.spiridonov.cryptoapp.data.database.AppDatabase
import ru.spiridonov.cryptoapp.data.database.CoinInfoDao
import ru.spiridonov.cryptoapp.data.repository.CoinRepositoryImpl
import ru.spiridonov.cryptoapp.domain.CoinRepository

@Module
interface DataModule {

    @Binds
    fun bindCoinRepository(impl: CoinRepositoryImpl): CoinRepository

    companion object {

        @Provides
        fun provideCoinInfoDao(
            application: Application
        ): CoinInfoDao {
            return AppDatabase.getInstance(application).coinPriceInfoDao()
        }
    }
}
