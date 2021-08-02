package cl.neoxcore.saifu.data.mapper

import cl.neoxcore.saifu.data.cache.model.CacheBalance
import cl.neoxcore.saifu.data.remote.model.RemoteBalance
import cl.neoxcore.saifu.domain.model.Balance
import javax.inject.Inject

class DataBalanceMapper @Inject constructor() {
    fun RemoteBalance.toDomain() = Balance(
        address = address.orEmpty(),
        balance = balance ?: 0,
        finalBalance = finalBalance ?: 0,
        unconfirmedBalance = unconfirmedBalance ?: 0
    )

    fun RemoteBalance.toCache() = CacheBalance(
        address = address.orEmpty(),
        balance = balance ?: 0,
        finalBalance = finalBalance ?: 0,
        unconfirmedBalance = unconfirmedBalance ?: 0
    )

    fun CacheBalance.toDomain() = Balance(
        address = address,
        balance = balance,
        finalBalance = finalBalance,
        unconfirmedBalance = unconfirmedBalance
    )
}
