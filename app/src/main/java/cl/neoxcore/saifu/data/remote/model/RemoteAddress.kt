package cl.neoxcore.saifu.data.remote.model

import com.google.gson.annotations.SerializedName

data class RemoteAddress(
    @SerializedName("private") val private: String?,
    @SerializedName("public") val public: String?,
    @SerializedName("address") val address: String?,
    @SerializedName("wif") val wif: String?
)
