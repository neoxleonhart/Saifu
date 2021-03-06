package cl.neoxcore.saifu.data.cache

import cl.neoxcore.saifu.data.cache.database.DatabaseBuilder
import cl.neoxcore.saifu.data.cache.model.CacheBalance
import cl.neoxcore.saifu.data.cache.model.CacheTransaction
import cl.neoxcore.saifu.data.cache.preferences.CachePreferences
import cl.neoxcore.saifu.data.source.Cache
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.take
import javax.inject.Inject

class CacheImpl @Inject constructor(
    private val preferences: CachePreferences,
    private val databaseBuilder: DatabaseBuilder
) : Cache {
    override suspend fun storeCacheAddress(value: String) {
        preferences.storeCacheAddress(value)
    }

    override suspend fun storeCacheBalance(value: CacheBalance) {
        preferences.storeCacheBalance(value)
    }

    override fun getCacheAddress(): Flow<String> {
        return preferences.getCacheAddress.take(1)
    }

    override fun getCacheBalance(): Flow<CacheBalance> {
        return preferences.getCacheBalance.take(1)
    }

    override suspend fun storeCacheTransactions(values: List<CacheTransaction>) {
        databaseBuilder.transactionDao().insertAll(values)
    }

    override fun getCacheTransactions(): Flow<List<CacheTransaction>> = flow {
        val transactions = databaseBuilder.transactionDao().getAll()
        emit(transactions)
    }
}
