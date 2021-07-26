package cl.neoxcore.saifu.domain

import cl.neoxcore.saifu.domain.repository.Repository
import javax.inject.Inject

class SaveAddressUseCase @Inject constructor(private val repository: Repository) {
    suspend fun execute(address: String) = repository.saveAddress(address)
}
