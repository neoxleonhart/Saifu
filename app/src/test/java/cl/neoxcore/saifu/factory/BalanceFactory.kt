package cl.neoxcore.saifu.factory

import cl.neoxcore.saifu.data.cache.model.CacheBalance
import cl.neoxcore.saifu.data.remote.model.RemoteBalance
import cl.neoxcore.saifu.domain.model.Balance
import cl.neoxcore.saifu.factory.BaseFactory.randomLong
import cl.neoxcore.saifu.factory.BaseFactory.randomString
import cl.neoxcore.saifu.presentation.model.UiBalance

object BalanceFactory {
    fun makeRemoteBalance() = RemoteBalance(
        address = randomString(),
        balance = randomLong(),
        finalBalance = randomLong(),
        finalNTx = randomLong(),
        nTx = randomLong(),
        totalReceived = randomLong(),
        totalSent = randomLong(),
        unconfirmedBalance = randomLong(),
        unconfirmedNTx = randomLong()
    )

    fun makeBalance() = Balance(
        address = randomString(),
        balance = randomLong(),
        finalBalance = randomLong(),
        unconfirmedBalance = randomLong()
    )

    fun makeCacheBalance() = CacheBalance(
        address = randomString(),
        balance = randomLong(),
        finalBalance = randomLong(),
        unconfirmedBalance = randomLong()
    )

    fun makeUiBalance() = UiBalance(
        address = randomString(),
        balance = randomLong(),
        finalBalance = randomLong(),
        unconfirmedBalance = randomLong()
    )
}
