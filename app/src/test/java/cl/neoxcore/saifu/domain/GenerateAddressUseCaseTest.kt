package cl.neoxcore.saifu.domain

import cl.neoxcore.saifu.domain.repository.Repository
import cl.neoxcore.saifu.factory.BaseFactory.randomString
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class GenerateAddressUseCaseTest {
    private val repository = mockk<Repository>()
    private val useCase = GenerateAddressUseCase(repository)

    @Test
    fun `when calls 'execute', the returns String`() = runBlocking {
        val domainString = randomString()
        stubRepository(domainString)

        val flow = useCase.execute()

        flow.collect { result ->
            assertEquals(domainString, result)
        }
        coVerify { repository.generateAddress() }
    }

    private fun stubRepository(domainString: String) {
        coEvery { repository.generateAddress() } returns flow { emit(domainString) }
    }
}
