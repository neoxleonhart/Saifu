package cl.neoxcore.saifu.data.remote.model

import com.google.gson.annotations.SerializedName

data class RemoteFullAddress(
    @SerializedName("txs") val txs: List<RemoteTransaction>?
)
