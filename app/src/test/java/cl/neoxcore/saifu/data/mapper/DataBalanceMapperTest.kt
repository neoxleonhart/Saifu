package cl.neoxcore.saifu.data.mapper

import cl.neoxcore.saifu.factory.BalanceFactory.makeCacheBalance
import cl.neoxcore.saifu.factory.BalanceFactory.makeRemoteBalance
import org.junit.Assert.assertEquals
import org.junit.Test

class DataBalanceMapperTest {
    private val mapper = DataBalanceMapper()

    @Test
    fun `given RemoteBalance, when toDomain, then Balance`() {
        val remote = makeRemoteBalance()

        val balance = with(mapper) { remote.toDomain() }

        assertEquals("address", remote.address, balance.address)
        assertEquals("balance", remote.balance, balance.balance)
        assertEquals("finalBalance", remote.finalBalance, balance.finalBalance)
        assertEquals("unconfirmedBalance", remote.unconfirmedBalance, balance.unconfirmedBalance)
    }

    @Test
    fun `given RemoteBalance, when toCache, then CacheBalance`() {
        val remote = makeRemoteBalance()

        val cacheBalance = with(mapper) { remote.toCache() }

        assertEquals("address", remote.address, cacheBalance.address)
        assertEquals("balance", remote.balance, cacheBalance.balance)
        assertEquals("finalBalance", remote.finalBalance, cacheBalance.finalBalance)
        assertEquals("unconfirmedBalance", remote.unconfirmedBalance, cacheBalance.unconfirmedBalance)
    }

    @Test
    fun `given CacheBalance, when toDomain, then Balance`() {
        val cacheBalance = makeCacheBalance()

        val balance = with(mapper) { cacheBalance.toDomain() }

        assertEquals("address", cacheBalance.address, balance.address)
        assertEquals("balance", cacheBalance.balance, balance.balance)
        assertEquals("finalBalance", cacheBalance.finalBalance, balance.finalBalance)
        assertEquals("unconfirmedBalance", cacheBalance.unconfirmedBalance, balance.unconfirmedBalance)
    }
}
