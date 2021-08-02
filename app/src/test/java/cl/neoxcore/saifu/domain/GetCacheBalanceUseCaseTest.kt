package cl.neoxcore.saifu.domain

import cl.neoxcore.saifu.domain.model.Balance
import cl.neoxcore.saifu.domain.repository.Repository
import cl.neoxcore.saifu.factory.BalanceFactory
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class GetCacheBalanceUseCaseTest {
    private val repository = mockk<Repository>()
    private val useCase = GetCacheBalanceUseCase(repository)

    @Test
    fun `when calls 'execute', the returns String`() = runBlocking {
        val balance = BalanceFactory.makeBalance()
        stubRepository(balance)

        val flow = useCase.execute()

        flow.collect { result ->
            Assert.assertEquals(balance, result)
        }
        coVerify { repository.getCachedBalance() }
    }

    private fun stubRepository(balance: Balance) {
        coEvery { repository.getCachedBalance() } returns flow { emit(balance) }
    }
}
