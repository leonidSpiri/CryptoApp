package ru.spiridonov.cryptoapp.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import kotlinx.coroutines.delay
import ru.spiridonov.cryptoapp.data.database.AppDatabase
import ru.spiridonov.cryptoapp.data.mapper.CoinMapper
import ru.spiridonov.cryptoapp.data.network.ApiFactory
import ru.spiridonov.cryptoapp.domain.CoinInfo
import ru.spiridonov.cryptoapp.domain.CoinRepository
import ru.spiridonov.cryptoapp.workers.RefreshDataWorker

class CoinRepositoryImpl(
    private val application: Application
) : CoinRepository {

    private val coinInfoDao = AppDatabase.getInstance(application).coinPriceInfoDao()
    private val mapper = CoinMapper()

    override fun getCoinInfoList(): LiveData<List<CoinInfo>> {
        return Transformations.map(coinInfoDao.getPriceList()) {
            it.map {
                mapper.mapDbModelToEntity(it)
            }
        }
    }

    override fun getCoinInfo(fromSymbol: String): LiveData<CoinInfo> {
        return Transformations.map(coinInfoDao.getPriceInfoAboutCoin(fromSymbol)) {
            mapper.mapDbModelToEntity(it)
        }
    }

    override fun loadData() {
        val workManager = WorkManager.getInstance(application)
        workManager.enqueueUniqueWork(RefreshDataWorker.WORK_NAME,
            ExistingWorkPolicy.REPLACE,
            RefreshDataWorker.makeRequest()
        )
    }
}
