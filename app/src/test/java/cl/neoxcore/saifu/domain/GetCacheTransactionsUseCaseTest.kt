package cl.neoxcore.saifu.domain

import cl.neoxcore.saifu.domain.model.Transaction
import cl.neoxcore.saifu.domain.repository.Repository
import cl.neoxcore.saifu.factory.TransactionFactory.makeTransactionList
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class GetCacheTransactionsUseCaseTest {
    private val repository = mockk<Repository>()
    private val useCase = GetCacheTransactionsUseCase(repository)

    @Test
    fun `when calls 'execute', the returns String`() = runBlocking {
        val transactions = makeTransactionList(3)
        stubRepository(transactions)

        val flow = useCase.execute()

        flow.collect { result ->
            assertEquals(transactions, result)
        }
        coVerify { repository.getCacheTransactions() }
    }

    private fun stubRepository(transactions: List<Transaction>) {
        coEvery { repository.getCacheTransactions() } returns flow { emit(transactions) }
    }
}
