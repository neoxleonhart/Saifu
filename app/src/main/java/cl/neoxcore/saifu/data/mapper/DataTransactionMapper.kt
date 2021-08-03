package cl.neoxcore.saifu.data.mapper

import cl.neoxcore.saifu.data.cache.model.CacheTransaction
import cl.neoxcore.saifu.data.remote.model.RemoteFullAddress
import cl.neoxcore.saifu.data.remote.model.RemoteTransaction
import cl.neoxcore.saifu.domain.model.Transaction
import javax.inject.Inject

class DataTransactionMapper @Inject constructor() {
    fun RemoteFullAddress.toDomain(): List<Transaction> {
        return txs?.toDomain() ?: emptyList()
    }

    private fun List<RemoteTransaction>.toDomain(): List<Transaction> {
        return this.map { it.toDomain() }
    }

    private fun RemoteTransaction.toDomain() = Transaction(
        id = hash.orEmpty(),
        date = received.orEmpty(),
        total = total ?: 0
    )

    fun RemoteFullAddress.toCache(): List<CacheTransaction> {
        return txs?.toCache() ?: emptyList()
    }

    private fun List<RemoteTransaction>.toCache(): List<CacheTransaction> {
        return this.map { it.toCache() }
    }

    private fun RemoteTransaction.toCache() = CacheTransaction(
        id = hash.orEmpty(),
        date = received.orEmpty(),
        total = total ?: 0
    )

    fun List<CacheTransaction>.cacheToDomain(): List<Transaction> {
        return this.map { it.toDomain() }
    }

    private fun CacheTransaction.toDomain() = Transaction(
        id = id,
        date = date,
        total = total
    )
}
