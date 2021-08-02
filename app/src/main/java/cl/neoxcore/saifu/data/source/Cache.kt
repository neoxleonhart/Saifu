package cl.neoxcore.saifu.data.source

import cl.neoxcore.saifu.data.cache.model.CacheBalance
import kotlinx.coroutines.flow.Flow

interface Cache {
    suspend fun storeCacheAddress(value: String)
    suspend fun storeCacheBalance(value: CacheBalance)
    fun getCacheAddress(): Flow<String>
    fun getCacheBalance(): Flow<CacheBalance>
}
