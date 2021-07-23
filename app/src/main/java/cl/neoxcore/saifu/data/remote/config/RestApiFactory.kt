package cl.neoxcore.saifu.data.remote.config

import cl.neoxcore.saifu.data.remote.retrofit.WebService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RestApiFactory {
    private lateinit var service: WebService

    companion object {
        private const val API_URL = "https://api.blockcypher.com/v1/btc/test3"
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(API_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun createService() {
        service = retrofit.create(WebService::class.java)
    }

    fun makeRestApi(): WebService {
        createService()
        return service
    }
}
