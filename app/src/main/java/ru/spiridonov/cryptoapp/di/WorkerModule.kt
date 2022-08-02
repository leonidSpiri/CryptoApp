package ru.spiridonov.cryptoapp.di

import androidx.work.ListenableWorker
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import ru.spiridonov.cryptoapp.workers.ChildWorkerFactory
import ru.spiridonov.cryptoapp.workers.RefreshDataWorker

@Module
interface WorkerModule {
    @Binds
    @IntoMap
    @WorkerKey(RefreshDataWorker::class)
    fun bindRefreshDataWorkerFactory(worker: RefreshDataWorker.Factory): ChildWorkerFactory
}