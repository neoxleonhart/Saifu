package cl.neoxcore.saifu.presentation.mapper

import cl.neoxcore.saifu.domain.model.Balance
import cl.neoxcore.saifu.presentation.model.UiBalance
import javax.inject.Inject

class UiBalanceMapper @Inject constructor() {
    fun Balance.toUi() = UiBalance(
        address = address,
        balance = balance,
        finalBalance = finalBalance,
        unconfirmedBalance = unconfirmedBalance
    )
}
