package cl.neoxcore.saifu.data.source

import cl.neoxcore.saifu.data.remote.model.RemoteAddress
import cl.neoxcore.saifu.data.remote.model.RemoteBalance

interface Remote {
    suspend fun generateAddress(): RemoteAddress
    suspend fun getBalance(address: String): RemoteBalance
}
