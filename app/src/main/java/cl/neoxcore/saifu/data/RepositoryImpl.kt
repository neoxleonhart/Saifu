package cl.neoxcore.saifu.data

import cl.neoxcore.saifu.data.mapper.DataBalanceMapper
import cl.neoxcore.saifu.data.source.Cache
import cl.neoxcore.saifu.data.source.Remote
import cl.neoxcore.saifu.domain.model.Balance
import cl.neoxcore.saifu.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val remote: Remote,
    private val cache: Cache,
    private val balanceMapper: DataBalanceMapper
) : Repository {
    override fun generateAddress(): Flow<String> = flow {
        val address = remote.generateAddress().address.orEmpty()
        emit(address)
    }

    override suspend fun saveAddress(address: String) {
        cache.storeCacheAddress(address)
    }

    override fun getBalance(): Flow<Balance> = flow {
        cache.getCacheAddress().collect {
            val balance = remote.getBalance(it)
            emit(with(balanceMapper) { balance.toDomain() })
            cache.storeCacheBalance(with(balanceMapper) { balance.toCache() })
        }
    }

    override fun getCachedBalance(): Flow<Balance> = flow {
        cache.getCacheBalance().collect {
            emit(with(balanceMapper) { it.toDomain() })
        }
    }
}
