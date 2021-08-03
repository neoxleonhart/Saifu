package cl.neoxcore.saifu.data.remote.model

import com.google.gson.annotations.SerializedName

data class RemoteTransaction(
    @SerializedName("hash") val hash: String?,
    @SerializedName("received") val received: String?,
    @SerializedName("total") val total: Long?
)
