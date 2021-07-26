package cl.neoxcore.saifu.data.source

interface Cache {
    suspend fun storeCacheAddress(value: String)
}
