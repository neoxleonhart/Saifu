package cl.neoxcore.saifu.data.cache

import cl.neoxcore.saifu.data.cache.preferences.CachePreferences
import cl.neoxcore.saifu.data.source.Cache
import javax.inject.Inject

class CacheImpl @Inject constructor(private val preferences: CachePreferences) : Cache {
    override suspend fun storeCacheAddress(value: String) {
        preferences.storeCacheAddress(value)
    }
}
