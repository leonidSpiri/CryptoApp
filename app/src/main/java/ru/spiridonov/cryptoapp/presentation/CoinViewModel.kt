package ru.spiridonov.cryptoapp.presentation

import androidx.lifecycle.ViewModel
import ru.spiridonov.cryptoapp.domain.GetCoinInfoListUseCase
import ru.spiridonov.cryptoapp.domain.GetCoinInfoUseCase
import ru.spiridonov.cryptoapp.domain.LoadDataUseCase
import javax.inject.Inject

class CoinViewModel @Inject constructor(
    private val getCoinInfoListUseCase: GetCoinInfoListUseCase,
    private val getCoinInfoUseCase: GetCoinInfoUseCase,
    private val loadDataUseCase: LoadDataUseCase,
) : ViewModel() {

    val coinInfoList = getCoinInfoListUseCase()

    fun getDetailInfo(fSym: String) = getCoinInfoUseCase(fSym)

    init {
        loadDataUseCase()
    }
}
