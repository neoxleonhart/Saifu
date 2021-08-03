package cl.neoxcore.saifu.data.mapper

import cl.neoxcore.saifu.data.cache.model.CacheTransaction
import cl.neoxcore.saifu.data.remote.model.RemoteFullAddress
import cl.neoxcore.saifu.domain.model.Transaction
import cl.neoxcore.saifu.factory.TransactionFactory.makeCacheTransactionList
import cl.neoxcore.saifu.factory.TransactionFactory.makeRemoteFullAddress
import org.junit.Assert.assertEquals
import org.junit.Test

class DataTransactionMapperTest {
    private val mapper = DataTransactionMapper()

    @Test
    fun `given RemoteFullAddress, when toDomain, then ListTransaction`() {
        val remote = makeRemoteFullAddress(3)

        val domain = with(mapper) { remote.toDomain() }

        assetFullAddressEquals(remote, domain)
    }

    private fun assetFullAddressEquals(remote: RemoteFullAddress, domain: List<Transaction>) {
        remote.txs?.zip(domain)?.map {
            assertEquals("id", it.first.hash, it.second.id)
            assertEquals("date", it.first.received, it.second.date)
            assertEquals("total", it.first.total, it.second.total)
        }
    }

    @Test
    fun `given RemoteFullAddress, when toCache, then ListCacheTransaction`() {
        val remote = makeRemoteFullAddress(3)

        val cache = with(mapper) { remote.toCache() }

        assetFullAddressCacheEquals(remote, cache)
    }

    private fun assetFullAddressCacheEquals(
        remote: RemoteFullAddress,
        cache: List<CacheTransaction>
    ) {
        remote.txs?.zip(cache)?.map {
            assertEquals("id", it.first.hash, it.second.id)
            assertEquals("date", it.first.received, it.second.date)
            assertEquals("total", it.first.total, it.second.total)
        }
    }

    @Test
    fun `given ListCacheTransaction, when cacheToDomain, then ListTransaction`() {
        val cache = makeCacheTransactionList(3)

        val domain = with(mapper) { cache.cacheToDomain() }

        assetCacheDomainEquals(cache, domain)
    }

    private fun assetCacheDomainEquals(cache: List<CacheTransaction>, domain: List<Transaction>) {
        cache.zip(domain).map {
            assertEquals("id", it.first.id, it.second.id)
            assertEquals("date", it.first.date, it.second.date)
            assertEquals("total", it.first.total, it.second.total)
        }
    }
}
