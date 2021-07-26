package cl.neoxcore.saifu.data

import cl.neoxcore.saifu.data.remote.model.RemoteAddress
import cl.neoxcore.saifu.data.source.Cache
import cl.neoxcore.saifu.data.source.Remote
import cl.neoxcore.saifu.factory.AddressFactory.makeRemoteAddress
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class RepositoryImplTest {
    private val remote = mockk<Remote>()
    private val cache = mockk<Cache>()
    private val repository = RepositoryImpl(remote, cache)

    @Test
    fun `given RemoteAddress, when generateAddress, then return data`() = runBlocking {
        val remoteAddress = makeRemoteAddress()
        stubRemoteGenerateAddress(remoteAddress)

        val flow = repository.generateAddress()

        flow.collect {
            assertEquals(remoteAddress.address, it)
        }
    }

    private fun stubRemoteGenerateAddress(remoteAddress: RemoteAddress) {
        coEvery { remote.generateAddress() } returns remoteAddress
    }
}
