package cl.neoxcore.saifu.data.remote.retrofit

import cl.neoxcore.saifu.data.remote.model.RemoteAddress
import cl.neoxcore.saifu.data.remote.model.RemoteBalance
import cl.neoxcore.saifu.data.remote.model.RemoteFullAddress
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface WebService {
    @POST("addrs")
    suspend fun generateAddress(): RemoteAddress

    @GET("addrs/{address}/balance")
    suspend fun getBalance(@Path("address") address: String): RemoteBalance

    @GET("addrs/{address}/full")
    suspend fun getTransactions(@Path("address") address: String): RemoteFullAddress
}
