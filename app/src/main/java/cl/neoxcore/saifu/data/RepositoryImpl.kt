package cl.neoxcore.saifu.data

import cl.neoxcore.saifu.data.source.Cache
import cl.neoxcore.saifu.data.source.Remote
import cl.neoxcore.saifu.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RepositoryImpl @Inject constructor(private val remote: Remote, private val cache: Cache) : Repository {
    override fun generateAddress(): Flow<String> = flow {
        val address = remote.generateAddress().address.orEmpty()
        emit(address)
    }

    override suspend fun saveAddress(address: String) {
        cache.storeCacheAddress(address)
    }
}
