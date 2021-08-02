package cl.neoxcore.saifu.data.remote.model

import com.google.gson.annotations.SerializedName

data class RemoteBalance(
    @SerializedName("address") val address: String?,
    @SerializedName("balance") val balance: Long?,
    @SerializedName("final_balance") val finalBalance: Long?,
    @SerializedName("final_n_tx") val finalNTx: Long?,
    @SerializedName("n_tx") val nTx: Long?,
    @SerializedName("total_received") val totalReceived: Long?,
    @SerializedName("total_sent") val totalSent: Long?,
    @SerializedName("unconfirmed_balance") val unconfirmedBalance: Long?,
    @SerializedName("unconfirmed_n_tx") val unconfirmedNTx: Long?
)
