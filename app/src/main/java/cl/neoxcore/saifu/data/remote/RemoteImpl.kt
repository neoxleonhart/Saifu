package cl.neoxcore.saifu.data.remote

import cl.neoxcore.saifu.data.remote.model.RemoteAddress
import cl.neoxcore.saifu.data.remote.model.RemoteBalance
import cl.neoxcore.saifu.data.remote.retrofit.WebService
import cl.neoxcore.saifu.data.source.Remote
import javax.inject.Inject

class RemoteImpl @Inject constructor(private val webService: WebService) : Remote {
    override suspend fun generateAddress(): RemoteAddress = webService.generateAddress()
    override suspend fun getBalance(address: String): RemoteBalance = webService.getBalance(address)
}
