package cl.neoxcore.saifu.data.remote

import cl.neoxcore.saifu.data.remote.model.RemoteAddress
import cl.neoxcore.saifu.data.remote.model.RemoteBalance
import cl.neoxcore.saifu.data.remote.model.RemoteFullAddress
import cl.neoxcore.saifu.data.remote.retrofit.WebService
import cl.neoxcore.saifu.factory.AddressFactory.makeRemoteAddress
import cl.neoxcore.saifu.factory.BalanceFactory.makeRemoteBalance
import cl.neoxcore.saifu.factory.BaseFactory.randomString
import cl.neoxcore.saifu.factory.TransactionFactory.makeRemoteFullAddress
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

    @Test
    fun `given RemoteBalance with address, when getBalance, the return data`() = runBlocking {
        val remoteBalance = makeRemoteBalance()
        val address = randomString()
        stubWebServiceGetBalance(address, remoteBalance)

        val result = remote.getBalance(address)

        assertEquals(remoteBalance, result)
    }

    private fun stubWebServiceGetBalance(address: String, remoteBalance: RemoteBalance) {
        coEvery { webService.getBalance(address) } returns remoteBalance
    }

    @Test
    fun `given RemoteFullAddress with address, when getTransactions, the return data`() = runBlocking {
        val remoteFullAddress = makeRemoteFullAddress(3)
        val address = randomString()
        stubWebServiceGetTransactions(address, remoteFullAddress)

        val result = remote.getTransactions(address)

        assertEquals(remoteFullAddress, result)
    }

    private fun stubWebServiceGetTransactions(address: String, remoteFullAddress: RemoteFullAddress) {
        coEvery { webService.getTransactions(address) } returns remoteFullAddress
    }
}
