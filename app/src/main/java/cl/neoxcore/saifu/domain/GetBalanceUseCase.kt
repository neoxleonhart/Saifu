package cl.neoxcore.saifu.domain

import cl.neoxcore.saifu.domain.model.Balance
import cl.neoxcore.saifu.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetBalanceUseCase @Inject constructor(private val repository: Repository) {
    fun execute(): Flow<Balance> = repository.getBalance()
}
