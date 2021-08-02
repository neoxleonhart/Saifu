package cl.neoxcore.saifu.data.cache

import cl.neoxcore.saifu.data.cache.model.CacheBalance
import cl.neoxcore.saifu.data.cache.preferences.CachePreferences
import cl.neoxcore.saifu.factory.BalanceFactory.makeCacheBalance
import cl.neoxcore.saifu.factory.BaseFactory.randomString
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class CacheImplTest {
    private val preferences = mockk<CachePreferences>()
    private val cache = CacheImpl(preferences)

    @Test
    fun `given String, when getCacheAddress, then return data `() = runBlocking {
        val address = randomString()
        stubGetCacheAddress(address)

        val flow = cache.getCacheAddress()
        flow.collect { result ->
            assertEquals(address, result)
        }
    }

    private fun stubGetCacheAddress(address: String) {
        coEvery { preferences.getCacheAddress.take(1) } returns flow { emit(address) }
    }

    @Test
    fun `given CacheBalance, when getCacheBalance, then return data `() = runBlocking {
        val cacheBalance = makeCacheBalance()
        stubGetCacheBalance(cacheBalance)

        val flow = cache.getCacheBalance()
        flow.collect { result ->
            assertEquals(cacheBalance, result)
        }
    }

    private fun stubGetCacheBalance(cacheBalance: CacheBalance) {
        coEvery { preferences.getCacheBalance.take(1) } returns flow { emit(cacheBalance) }
    }
}
