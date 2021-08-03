package cl.neoxcore.saifu.domain

import cl.neoxcore.saifu.domain.repository.Repository
import cl.neoxcore.saifu.factory.BaseFactory.randomString
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class GetCacheAddressUseCaseTest {
    private val repository = mockk<Repository>()
    private val useCase = GetCacheAddressUseCase(repository)

    @Test
    fun `when calls 'execute', the returns String`() = runBlocking {
        val domainString = randomString()
        stubRepository(domainString)

        val flow = useCase.execute()

        flow.collect { result ->
            Assert.assertEquals(domainString, result)
        }
        coVerify { repository.getCachedAddress() }
    }

    private fun stubRepository(domainString: String) {
        coEvery { repository.getCachedAddress() } returns flow { emit(domainString) }
    }
}
