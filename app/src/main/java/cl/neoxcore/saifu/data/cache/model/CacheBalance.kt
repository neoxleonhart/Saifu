package cl.neoxcore.saifu.data.cache.model

data class CacheBalance(
    val address: String,
    val balance: Long,
    val finalBalance: Long,
    val unconfirmedBalance: Long
)
