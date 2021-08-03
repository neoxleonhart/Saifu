package cl.neoxcore.saifu.domain

import cl.neoxcore.saifu.domain.model.Balance
import cl.neoxcore.saifu.domain.repository.Repository
import cl.neoxcore.saifu.factory.BalanceFactory.makeBalance
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class GetBalanceUseCaseTest {
    private val repository = mockk<Repository>()
    private val useCase = GetBalanceUseCase(repository)

    @Test
    fun `when calls 'execute', the returns String`() = runBlocking {
        val balance = makeBalance()
        stubRepository(balance)

        val flow = useCase.execute()

        flow.collect { result ->
            assertEquals(balance, result)
        }
        coVerify { repository.getBalance() }
    }

    private fun stubRepository(balance: Balance) {
        coEvery { repository.getBalance() } returns flow { emit(balance) }
    }
}
