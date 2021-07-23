package cl.neoxcore.saifu.data.source

import cl.neoxcore.saifu.data.remote.model.RemoteAddress

interface Remote {
    suspend fun generateAddress(): RemoteAddress
}
