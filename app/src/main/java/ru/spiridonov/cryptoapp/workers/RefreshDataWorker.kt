package ru.spiridonov.cryptoapp.workers

import android.content.Context
import androidx.work.*
import kotlinx.coroutines.delay
import ru.spiridonov.cryptoapp.data.database.AppDatabase
import ru.spiridonov.cryptoapp.data.mapper.CoinMapper
import ru.spiridonov.cryptoapp.data.network.ApiFactory

class RefreshDataWorker(context: Context, workerParameters: WorkerParameters) : CoroutineWorker
    (context, workerParameters) {
    private val coinInfoDao = AppDatabase.getInstance(context).coinPriceInfoDao()
    private val apiService = ApiFactory.apiService
    private val mapper = CoinMapper()

    override suspend fun doWork(): Result {
        while (true) {
            try {
                val topCoins = apiService.getTopCoinsInfo(limit = 50)
                val fSyms = mapper.mapNamesListToString(topCoins)
                val jsonContainer = apiService.getFullPriceList(fSyms = fSyms)
                val coinInfoDtoList = mapper.mapJsonContainerToListCoinInfo(jsonContainer)
                val dbModelList = coinInfoDtoList.map { mapper.mapDtoToDbModel(it) }
                coinInfoDao.insertPriceList(dbModelList)
            } catch (e: Exception) {
            }
            delay(10000)
        }
    }

    companion object {
        const val WORK_NAME = "RefreshDataWorker"
        fun makeRequest() =
            OneTimeWorkRequestBuilder<RefreshDataWorker>().setConstraints(makeConstraints()).build()

        private fun makeConstraints() =
            Constraints.Builder()
                .setRequiresBatteryNotLow(true)
                .setRequiredNetworkType(NetworkType.UNMETERED)
                .build()
    }
}