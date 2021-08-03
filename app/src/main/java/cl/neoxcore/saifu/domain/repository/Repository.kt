package cl.neoxcore.saifu.domain.repository

import cl.neoxcore.saifu.domain.model.Balance
import kotlinx.coroutines.flow.Flow

interface Repository {
    fun generateAddress(): Flow<String>
    suspend fun saveAddress(address: String)
    fun getCachedAddress(): Flow<String>
    fun getBalance(): Flow<Balance>
    fun getCachedBalance(): Flow<Balance>
}
