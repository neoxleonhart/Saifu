package cl.neoxcore.saifu.domain

import cl.neoxcore.saifu.BaseFactory
import cl.neoxcore.saifu.domain.repository.Repository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class GenerateAddressUseCaseTest {
    private val repository = mockk<Repository>()
    private val useCase = GenerateAddressUseCase(repository)

    @Test
    fun `when calls 'execute', the returns String`() = runBlocking {
        val domainString = BaseFactory.randomString()
        stubRepository(domainString)

        val flow = useCase.execute()

        flow.collect { result ->
            Assert.assertEquals(domainString, result)
        }
        coVerify { repository.generateAddress() }
    }

    private fun stubRepository(domainString: String) {
        coEvery { repository.generateAddress() } returns flow { emit(domainString) }
    }
}
