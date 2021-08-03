package cl.neoxcore.saifu.presentation.transaction

import cl.neoxcore.saifu.domain.GetCacheTransactionsUseCase
import cl.neoxcore.saifu.domain.GetTransactionsUseCase
import cl.neoxcore.saifu.domain.model.Transaction
import cl.neoxcore.saifu.factory.TransactionFactory.makeTransactionList
import cl.neoxcore.saifu.factory.TransactionFactory.makeUiTransactionList
import cl.neoxcore.saifu.presentation.mapper.UiTransactionMapper
import cl.neoxcore.saifu.presentation.model.UiTransaction
import cl.neoxcore.saifu.presentation.mvi.execution.ExecutionThreadEnvironment
import cl.neoxcore.saifu.presentation.mvi.execution.ExecutionThreadFactory
import cl.neoxcore.saifu.presentation.transaction.TransactionAction.GetTransactionAction
import cl.neoxcore.saifu.presentation.transaction.TransactionResult.GetTransactionResult.ErrorWithCache
import cl.neoxcore.saifu.presentation.transaction.TransactionResult.GetTransactionResult.InProgress
import cl.neoxcore.saifu.presentation.transaction.TransactionResult.GetTransactionResult.Success
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

@FlowPreview
class TransactionProcessorTest {
    private val getTransactionsUseCase = mockk<GetTransactionsUseCase>()
    private val getCacheTransactionsUseCase = mockk<GetCacheTransactionsUseCase>()
    private val transactionMapper = mockk<UiTransactionMapper>()
    private val executionThread = ExecutionThreadFactory.makeExecutionThread(
        ExecutionThreadEnvironment.TESTING
    )
    private val processor = TransactionProcessor(
        getTransactionsUseCase = getTransactionsUseCase,
        getCacheTransactionsUseCase = getCacheTransactionsUseCase,
        transactionMapper = transactionMapper,
        executionThread = executionThread
    )

    @Test
    fun `given GetBalanceAction, when actionProcessor, then result InProgress`() =
        runBlocking {
            val transactions = makeTransactionList(3)
            stubGetTransactionsUseCase(transactions)
            stubGetCacheTransactionsUseCase(transactions)

            val resultFlow = processor.actionProcessor(GetTransactionAction).toList()
            val mapOutput = resultFlow.first()

            Assert.assertTrue(mapOutput is InProgress)
        }

    @Test
    fun `given GetBalanceAction, when actionProcessor, then result Success`() = runBlocking {
        val transactions = makeTransactionList(3)
        val uiTransactions = makeUiTransactionList(3)
        stubTransactionMapper(transactions, uiTransactions)
        stubGetTransactionsUseCase(transactions)
        stubGetCacheTransactionsUseCase(transactions)

        val resultFlow = processor.actionProcessor(GetTransactionAction).toList()
        val mapOutput = resultFlow.drop(1).first()

        Assert.assertTrue(mapOutput is Success)
    }

    @Test
    fun `given GetBalanceAction, when actionProcessor, then result with data`() = runBlocking {
        val transactions = makeTransactionList(3)
        val uiTransactions = makeUiTransactionList(3)
        stubTransactionMapper(transactions, uiTransactions)
        stubGetTransactionsUseCase(transactions)
        stubGetCacheTransactionsUseCase(transactions)

        val resultFlow = processor.actionProcessor(GetTransactionAction).toList()
        val mapOutput = resultFlow.drop(1).first()

        Assert.assertEquals(uiTransactions, (mapOutput as Success).transactions)
    }

    @Test
    fun `given GetBalanceAction with error, when actionProcessor, then result ErrorWithCache`() =
        runBlocking {
            val transactions = makeTransactionList(3)
            val uiTransactions = makeUiTransactionList(3)
            stubTransactionMapper(transactions, uiTransactions)
            stubGetTransactionsUseCaseError()
            stubGetCacheTransactionsUseCase(transactions)

            val resultFlow = processor.actionProcessor(GetTransactionAction).toList()
            val mapOutput = resultFlow.drop(1).first()

            Assert.assertTrue(mapOutput is ErrorWithCache)
        }

    private fun stubGetTransactionsUseCase(transactions: List<Transaction>) {
        coEvery { getTransactionsUseCase.execute() } returns flow { emit(transactions) }
    }

    @Suppress("TooGenericExceptionThrown")
    private fun stubGetTransactionsUseCaseError(message: String = "") {
        coEvery { getTransactionsUseCase.execute() } returns flow { throw Throwable(message) }
    }

    private fun stubGetCacheTransactionsUseCase(transactions: List<Transaction>) {
        coEvery { getCacheTransactionsUseCase.execute() } returns flow { emit(transactions) }
    }

    private fun stubTransactionMapper(
        transactions: List<Transaction>,
        uiTransactions: List<UiTransaction>
    ) {
        every { with(transactionMapper) { transactions.toUi() } } returns uiTransactions
    }
}
