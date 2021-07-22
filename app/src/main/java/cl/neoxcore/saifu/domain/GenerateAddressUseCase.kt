package cl.neoxcore.saifu.domain

import cl.neoxcore.saifu.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GenerateAddressUseCase @Inject constructor(private val repository: Repository) {
    fun execute(): Flow<String> = repository.generateAddress()
}
