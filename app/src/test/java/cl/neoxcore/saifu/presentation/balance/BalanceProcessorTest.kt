package cl.neoxcore.saifu.presentation.balance

import cl.neoxcore.saifu.domain.GetBalanceUseCase
import cl.neoxcore.saifu.domain.GetCacheBalanceUseCase
import cl.neoxcore.saifu.domain.model.Balance
import cl.neoxcore.saifu.factory.BalanceFactory.makeBalance
import cl.neoxcore.saifu.factory.BalanceFactory.makeUiBalance
import cl.neoxcore.saifu.presentation.balance.BalanceAction.GetBalanceAction
import cl.neoxcore.saifu.presentation.balance.BalanceResult.GetBalanceResult.ErrorWithCache
import cl.neoxcore.saifu.presentation.balance.BalanceResult.GetBalanceResult.InProgress
import cl.neoxcore.saifu.presentation.balance.BalanceResult.GetBalanceResult.Success
import cl.neoxcore.saifu.presentation.mapper.UiBalanceMapper
import cl.neoxcore.saifu.presentation.model.UiBalance
import cl.neoxcore.saifu.presentation.mvi.execution.ExecutionThreadEnvironment
import cl.neoxcore.saifu.presentation.mvi.execution.ExecutionThreadFactory
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

@FlowPreview
class BalanceProcessorTest {
    private val getBalanceUseCase = mockk<GetBalanceUseCase>()
    private val getCacheBalanceUseCase = mockk<GetCacheBalanceUseCase>()
    private val balanceMapper = mockk<UiBalanceMapper>()
    private val executionThread = ExecutionThreadFactory.makeExecutionThread(
        ExecutionThreadEnvironment.TESTING
    )
    private val processor = BalanceProcessor(
        getBalanceUseCase = getBalanceUseCase,
        getCacheBalanceUseCase = getCacheBalanceUseCase,
        balanceMapper = balanceMapper,
        executionThread = executionThread
    )

    @Test
    fun `given GetBalanceAction, when actionProcessor, then result InProgress`() =
        runBlocking {
            val balance = makeBalance()
            stubGetBalanceUseCase(balance)
            stubGetCacheBalanceUseCase(balance)

            val resultFlow = processor.actionProcessor(GetBalanceAction).toList()
            val mapOutput = resultFlow.first()

            assertTrue(mapOutput is InProgress)
        }

    @Test
    fun `given GetBalanceAction, when actionProcessor, then result Success`() = runBlocking {
        val balance = makeBalance()
        val uiBalance = makeUiBalance()
        stubBalanceMapper(balance, uiBalance)
        stubGetBalanceUseCase(balance)
        stubGetCacheBalanceUseCase(balance)

        val resultFlow = processor.actionProcessor(GetBalanceAction).toList()
        val mapOutput = resultFlow.drop(1).first()

        assertTrue(mapOutput is Success)
    }

    @Test
    fun `given GetBalanceAction, when actionProcessor, then result with data`() = runBlocking {
        val balance = makeBalance()
        val uiBalance = makeUiBalance()
        stubBalanceMapper(balance, uiBalance)
        stubGetBalanceUseCase(balance)
        stubGetCacheBalanceUseCase(balance)

        val resultFlow = processor.actionProcessor(GetBalanceAction).toList()
        val mapOutput = resultFlow.drop(1).first()

        assertEquals(uiBalance, (mapOutput as Success).balance)
    }

    @Test
    fun `given GetBalanceAction with error, when actionProcessor, then result ErrorWithCache`() =
        runBlocking {
            val balance = makeBalance()
            val uiBalance = makeUiBalance()
            stubBalanceMapper(balance, uiBalance)
            stubGenerateAddressUseCaseError()
            stubGetCacheBalanceUseCase(balance)

            val resultFlow = processor.actionProcessor(GetBalanceAction).toList()
            val mapOutput = resultFlow.drop(1).first()

            assertTrue(mapOutput is ErrorWithCache)
        }

    private fun stubGetBalanceUseCase(balance: Balance) {
        coEvery { getBalanceUseCase.execute() } returns flow { emit(balance) }
    }

    @Suppress("TooGenericExceptionThrown")
    private fun stubGenerateAddressUseCaseError(message: String = "") {
        coEvery { getBalanceUseCase.execute() } returns flow { throw Throwable(message) }
    }

    private fun stubGetCacheBalanceUseCase(balance: Balance) {
        coEvery { getCacheBalanceUseCase.execute() } returns flow { emit(balance) }
    }

    private fun stubBalanceMapper(balance: Balance, uiBalance: UiBalance) {
        every { with(balanceMapper) { balance.toUi() } } returns uiBalance
    }
}
