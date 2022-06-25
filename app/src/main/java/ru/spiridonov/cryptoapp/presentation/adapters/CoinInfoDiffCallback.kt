package ru.spiridonov.cryptoapp.presentation.adapters

import androidx.recyclerview.widget.DiffUtil
import ru.spiridonov.cryptoapp.domain.CoinInfo

object CoinInfoDiffCallback : DiffUtil.ItemCallback<CoinInfo>() {
    override fun areItemsTheSame(oldItem: CoinInfo, newItem: CoinInfo) =
        oldItem.fromSymbol == newItem.fromSymbol

    override fun areContentsTheSame(oldItem: CoinInfo, newItem: CoinInfo) = oldItem == newItem
}