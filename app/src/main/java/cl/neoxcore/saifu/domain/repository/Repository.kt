package cl.neoxcore.saifu.domain.repository

import kotlinx.coroutines.flow.Flow

interface Repository {
    fun generateAddress(): Flow<String>
}
