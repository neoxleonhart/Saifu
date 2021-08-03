package cl.neoxcore.saifu.data

import cl.neoxcore.saifu.data.mapper.DataBalanceMapper
import cl.neoxcore.saifu.data.mapper.DataTransactionMapper
import cl.neoxcore.saifu.data.source.Cache
import cl.neoxcore.saifu.data.source.Remote
import cl.neoxcore.saifu.domain.model.Balance
import cl.neoxcore.saifu.domain.model.Transaction
import cl.neoxcore.saifu.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val remote: Remote,
    private val cache: Cache,
    private val balanceMapper: DataBalanceMapper,
    private val transactionMapper: DataTransactionMapper
) : Repository {
    override fun generateAddress(): Flow<String> = flow {
        val address = remote.generateAddress().address.orEmpty()
        emit(address)
    }

    override suspend fun saveAddress(address: String) {
        cache.storeCacheAddress(address)
    }

    override fun getCachedAddress(): Flow<String> {
        return cache.getCacheAddress()
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

    override fun getTransactions(): Flow<List<Transaction>> = flow {
        cache.getCacheAddress().collect {
            val transactions = remote.getTransactions(it)
            emit(with(transactionMapper) { transactions.toDomain() })
            cache.storeCacheTransactions(with(transactionMapper) { transactions.toCache() })
        }
    }

    override fun getCacheTransactions(): Flow<List<Transaction>> = flow {
        cache.getCacheTransactions().collect {
            emit(with(transactionMapper) { it.cacheToDomain() })
        }
    }
}
