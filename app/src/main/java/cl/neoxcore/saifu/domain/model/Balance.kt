package cl.neoxcore.saifu.domain.model

class Balance(
    val address: String,
    val balance: Long,
    val finalBalance: Long,
    val unconfirmedBalance: Long
)
