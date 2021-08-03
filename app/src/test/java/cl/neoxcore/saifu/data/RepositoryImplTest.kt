package cl.neoxcore.saifu.data

import cl.neoxcore.saifu.data.cache.model.CacheBalance
import cl.neoxcore.saifu.data.cache.model.CacheTransaction
import cl.neoxcore.saifu.data.mapper.DataBalanceMapper
import cl.neoxcore.saifu.data.mapper.DataTransactionMapper
import cl.neoxcore.saifu.data.remote.model.RemoteAddress
import cl.neoxcore.saifu.data.remote.model.RemoteBalance
import cl.neoxcore.saifu.data.remote.model.RemoteFullAddress
import cl.neoxcore.saifu.data.source.Cache
import cl.neoxcore.saifu.data.source.Remote
import cl.neoxcore.saifu.domain.model.Balance
import cl.neoxcore.saifu.domain.model.Transaction
import cl.neoxcore.saifu.factory.AddressFactory.makeRemoteAddress
import cl.neoxcore.saifu.factory.BalanceFactory.makeBalance
import cl.neoxcore.saifu.factory.BalanceFactory.makeCacheBalance
import cl.neoxcore.saifu.factory.BalanceFactory.makeRemoteBalance
import cl.neoxcore.saifu.factory.BaseFactory.randomString
import cl.neoxcore.saifu.factory.TransactionFactory.makeCacheTransactionList
import cl.neoxcore.saifu.factory.TransactionFactory.makeRemoteFullAddress
import cl.neoxcore.saifu.factory.TransactionFactory.makeTransactionList
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
    private val transactionMapper = mockk<DataTransactionMapper>()
    private val repository = RepositoryImpl(remote, cache, balanceMapper, transactionMapper)

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

    @Test
    fun `given Address, when getCachedAddress, then return data`() = runBlocking {
        val address = randomString()
        stubCacheGetAddress(address)

        val flow = repository.getCachedAddress()

        flow.collect {
            assertEquals(address, it)
        }
    }

    @Test
    fun `given RemoteBalance, when getTransactions, then return data`() = runBlocking {
        val address = randomString()
        val remoteFullAddress = makeRemoteFullAddress(3)
        val transactions = makeTransactionList(3)
        val cacheTransactions = makeCacheTransactionList(3)
        stubRemoteGetTransactions(address, remoteFullAddress)
        stubCacheGetAddress(address)
        stubTransactionMapperToDomain(remoteFullAddress, transactions)
        stubTransactionMapperToCache(remoteFullAddress, cacheTransactions)
        stubCacheStoreTransactions(cacheTransactions, Unit)

        val flow = repository.getTransactions()

        flow.collect {
            assertEquals(transactions, it)
        }
    }

    @Test
    fun `given CacheTransactions, when getCacheTransactions, then return data`() = runBlocking {
        val cacheTransactions = makeCacheTransactionList(3)
        val transactions = makeTransactionList(3)
        stubCacheTransactionMapperToDomain(cacheTransactions, transactions)
        stubCacheGetTransactions(cacheTransactions)

        val flow = repository.getCacheTransactions()

        flow.collect {
            assertEquals(transactions, it)
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

    private fun stubRemoteGetTransactions(address: String, remoteFullAddress: RemoteFullAddress) {
        coEvery { remote.getTransactions(address) } returns remoteFullAddress
    }

    private fun stubCacheStoreTransactions(cacheTransactions: List<CacheTransaction>, unit: Unit) {
        coEvery { cache.storeCacheTransactions(cacheTransactions) } returns unit
    }

    private fun stubCacheGetTransactions(cacheTransactions: List<CacheTransaction>) {
        coEvery { cache.getCacheTransactions() } returns flow { emit(cacheTransactions) }
    }

    private fun stubCacheTransactionMapperToDomain(
        cacheTransactions: List<CacheTransaction>,
        transactions: List<Transaction>
    ) {
        every { with(transactionMapper) { cacheTransactions.cacheToDomain() } } returns transactions
    }

    private fun stubTransactionMapperToDomain(
        remoteFullAddress: RemoteFullAddress,
        transactions: List<Transaction>
    ) {
        every { with(transactionMapper) { remoteFullAddress.toDomain() } } returns transactions
    }

    private fun stubTransactionMapperToCache(
        remoteFullAddress: RemoteFullAddress,
        cacheTransactions: List<CacheTransaction>
    ) {
        every { with(transactionMapper) { remoteFullAddress.toCache() } } returns cacheTransactions
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
