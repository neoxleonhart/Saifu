package cl.neoxcore.saifu.data

import cl.neoxcore.saifu.data.cache.model.CacheBalance
import cl.neoxcore.saifu.data.mapper.DataBalanceMapper
import cl.neoxcore.saifu.data.remote.model.RemoteAddress
import cl.neoxcore.saifu.data.remote.model.RemoteBalance
import cl.neoxcore.saifu.data.source.Cache
import cl.neoxcore.saifu.data.source.Remote
import cl.neoxcore.saifu.domain.model.Balance
import cl.neoxcore.saifu.factory.AddressFactory.makeRemoteAddress
import cl.neoxcore.saifu.factory.BalanceFactory.makeBalance
import cl.neoxcore.saifu.factory.BalanceFactory.makeCacheBalance
import cl.neoxcore.saifu.factory.BalanceFactory.makeRemoteBalance
import cl.neoxcore.saifu.factory.BaseFactory.randomString
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class RepositoryImplTest {
    private val remote = mockk<Remote>()
    private val cache = mockk<Cache>()
    private val balanceMapper = mockk<DataBalanceMapper>()
    private val repository = RepositoryImpl(remote, cache, balanceMapper)

    @Test
    fun `given RemoteAddress, when generateAddress, then return data`() = runBlocking {
        val remoteAddress = makeRemoteAddress()
        stubRemoteGenerateAddress(remoteAddress)

        val flow = repository.generateAddress()

        flow.collect {
            assertEquals(remoteAddress.address, it)
        }
    }

    @Test
    fun `given RemoteBalance, when getBalance, then return data`() = runBlocking {
        val address = randomString()
        val remoteBalance = makeRemoteBalance()
        val balance = makeBalance()
        val cacheBalance = makeCacheBalance()
        stubRemoteGetBalance(address, remoteBalance)
        stubCacheGetAddress(address)
        stubBalanceMapperToDomain(remoteBalance, balance)
        stubBalanceMapperToCache(remoteBalance, cacheBalance)
        stubCacheStoreBalance(cacheBalance, Unit)

        val flow = repository.getBalance()

        flow.collect {
            assertEquals(balance, it)
        }
    }

    @Test
    fun `given CacheBalance, when getCachedBalance, then return data`() = runBlocking {
        val cacheBalance = makeCacheBalance()
        val balance = makeBalance()
        stubCacheBalanceMapperToDomain(cacheBalance, balance)
        stubCacheGetBalance(cacheBalance)

        val flow = repository.getCachedBalance()

        flow.collect {
            assertEquals(balance, it)
        }
    }

    private fun stubRemoteGenerateAddress(remoteAddress: RemoteAddress) {
        coEvery { remote.generateAddress() } returns remoteAddress
    }

    private fun stubCacheGetBalance(cacheBalance: CacheBalance) {
        coEvery { cache.getCacheBalance() } returns flow { emit(cacheBalance) }
    }

    private fun stubCacheStoreBalance(cacheBalance: CacheBalance, unit: Unit) {
        coEvery { cache.storeCacheBalance(cacheBalance) } returns unit
    }

    private fun stubCacheGetAddress(address: String) {
        coEvery { cache.getCacheAddress() } returns flow { emit(address) }
    }

    private fun stubRemoteGetBalance(address: String, remoteBalance: RemoteBalance) {
        coEvery { remote.getBalance(address) } returns remoteBalance
    }

    private fun stubBalanceMapperToDomain(remoteBalance: RemoteBalance, balance: Balance) {
        every { with(balanceMapper) { remoteBalance.toDomain() } } returns balance
    }

    private fun stubBalanceMapperToCache(remoteBalance: RemoteBalance, cacheBalance: CacheBalance) {
        every { with(balanceMapper) { remoteBalance.toCache() } } returns cacheBalance
    }

    private fun stubCacheBalanceMapperToDomain(cacheBalance: CacheBalance, balance: Balance) {
        every { with(balanceMapper) { cacheBalance.toDomain() } } returns balance
    }
}
