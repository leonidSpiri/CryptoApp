package ru.spiridonov.cryptoapp.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import ru.spiridonov.cryptoapp.presentation.CoinApp
import ru.spiridonov.cryptoapp.presentation.CoinDetailFragment
import ru.spiridonov.cryptoapp.presentation.CoinPriceListActivity

@ApplicationScope
@Component(
    modules = [
        DataModule::class,
        ViewModelModule::class,
        WorkerModule::class
    ]
)
interface ApplicationComponent {

    fun inject(activity: CoinPriceListActivity)

    fun inject(fragment: CoinDetailFragment)

    fun inject(application: CoinApp)

    @Component.Factory
    interface Factory {

        fun create(
            @BindsInstance application: Application
        ): ApplicationComponent
    }
}
