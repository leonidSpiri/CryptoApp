package ru.spiridonov.cryptoapp.workers

import android.content.Context
import androidx.work.*
import kotlinx.coroutines.delay
import ru.spiridonov.cryptoapp.data.database.CoinInfoDao
import ru.spiridonov.cryptoapp.data.mapper.CoinMapper
import ru.spiridonov.cryptoapp.data.network.ApiService
import ru.spiridonov.cryptoapp.domain.GetCoinInfoUseCase
import javax.inject.Inject

class RefreshDataWorker(
    context: Context,
    workerParameters: WorkerParameters,
    private val coinInfoDao: CoinInfoDao,
    private val apiService: ApiService,
    private val mapper: CoinMapper,
    private val getCoinInfoUseCase: GetCoinInfoUseCase
) : CoroutineWorker
    (context, workerParameters) {

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

    class Factory @Inject constructor(
        private val coinInfoDao: CoinInfoDao,
        private val apiService: ApiService,
        private val mapper: CoinMapper,
        private val getCoinInfoUseCase: GetCoinInfoUseCase
    ) : ChildWorkerFactory {
        override fun create(
            context: Context,
            workerParameters: WorkerParameters
        ): ListenableWorker {
            return RefreshDataWorker(
                context,
                workerParameters,
                coinInfoDao,
                apiService,
                mapper,
                getCoinInfoUseCase
            )
        }

    }
}