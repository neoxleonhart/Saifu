package cl.neoxcore.saifu.domain

import cl.neoxcore.saifu.domain.model.Transaction
import cl.neoxcore.saifu.domain.repository.Repository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTransactionsUseCase @Inject constructor(private val repository: Repository) {
    fun execute(): Flow<List<Transaction>> = repository.getTransactions()
}
