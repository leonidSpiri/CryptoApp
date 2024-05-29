package ru.spiridonov.cryptoapp.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import ru.spiridonov.cryptoapp.data.database.CoinInfoDao
import ru.spiridonov.cryptoapp.data.mapper.CoinMapper
import ru.spiridonov.cryptoapp.domain.CoinInfo
import ru.spiridonov.cryptoapp.domain.CoinRepository
import ru.spiridonov.cryptoapp.workers.RefreshDataWorker
import javax.inject.Inject

class CoinRepositoryImpl @Inject constructor(
    private val application: Application,
    private val mapper: CoinMapper,
    private val coinInfoDao: CoinInfoDao
) : CoinRepository {

    override fun getCoinInfoList(): LiveData<List<CoinInfo>> =
        coinInfoDao.getPriceList().map { listCoinInfo ->
            listCoinInfo.map { coinInfo ->
                mapper.mapDbModelToEntity(coinInfo)
            }
        }


    override fun getCoinInfo(fromSymbol: String): LiveData<CoinInfo> =
        coinInfoDao.getPriceInfoAboutCoin(fromSymbol).map {
            mapper.mapDbModelToEntity(it)
        }

    override fun loadData() {
        val workManager = WorkManager.getInstance(application)
        workManager.enqueueUniqueWork(
            RefreshDataWorker.WORK_NAME,
            ExistingWorkPolicy.REPLACE,
            RefreshDataWorker.makeRequest()
        )
    }
}
