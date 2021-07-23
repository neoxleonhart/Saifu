package cl.neoxcore.saifu.data.remote

import cl.neoxcore.saifu.data.remote.model.RemoteAddress
import cl.neoxcore.saifu.data.remote.retrofit.WebService
import cl.neoxcore.saifu.factory.AddressFactory.makeRemoteAddress
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class RemoteImplTest {
    private val webService = mockk<WebService>()
    private val remote = RemoteImpl(webService)

    @Test
    fun `given RemoteAddress, when generateAddress, the return data`() = runBlocking {
        val remoteAddress = makeRemoteAddress()
        stubWebServiceGenerateAddress(remoteAddress)

        val result = remote.generateAddress()

        assertEquals(remoteAddress, result)
    }

    private fun stubWebServiceGenerateAddress(remoteAddress: RemoteAddress) {
        coEvery { webService.generateAddress() } returns remoteAddress
    }
}
