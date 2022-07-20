package ru.spiridonov.cryptoapp.presentation

import android.app.Application
import ru.spiridonov.cryptoapp.di.DaggerApplicationComponent

class CoinApp : Application() {

    val component by lazy {
        DaggerApplicationComponent.factory().create(this)
    }
}
