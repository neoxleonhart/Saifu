package cl.neoxcore.saifu.data.remote.retrofit

import cl.neoxcore.saifu.data.remote.model.RemoteAddress
import retrofit2.http.POST

interface WebService {
    @POST("addrs")
    suspend fun generateAddress(): RemoteAddress
}
