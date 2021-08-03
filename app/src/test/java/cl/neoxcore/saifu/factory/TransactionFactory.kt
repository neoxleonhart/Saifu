package cl.neoxcore.saifu.factory

import cl.neoxcore.saifu.data.cache.model.CacheTransaction
import cl.neoxcore.saifu.data.remote.model.RemoteFullAddress
import cl.neoxcore.saifu.data.remote.model.RemoteTransaction
import cl.neoxcore.saifu.domain.model.Transaction
import cl.neoxcore.saifu.factory.BaseFactory.randomLong
import cl.neoxcore.saifu.factory.BaseFactory.randomString

object TransactionFactory {
    fun makeRemoteFullAddress(count: Int) = RemoteFullAddress(
        txs = makeRemoteTransactionList(count)
    )

    private fun makeRemoteTransactionList(count: Int): List<RemoteTransaction> {
        return (0..count).map { makeRemoteTransaction() }
    }

    private fun makeRemoteTransaction() = RemoteTransaction(
        hash = randomString(),
        received = randomString(),
        total = randomLong()
    )

    fun makeCacheTransactionList(count: Int): List<CacheTransaction> {
        return (0..count).map { makeCacheTransaction() }
    }

    private fun makeCacheTransaction() = CacheTransaction(
        id = randomString(),
        date = randomString(),
        total = randomLong()
    )

    fun makeTransactionList(count: Int): List<Transaction> {
        return (0..count).map { makeTransaction() }
    }

    private fun makeTransaction() = Transaction(
        id = randomString(),
        date = randomString(),
        total = randomLong()
    )
}
