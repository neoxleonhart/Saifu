package cl.neoxcore.saifu.presentation.transaction

import cl.neoxcore.saifu.domain.GetCacheTransactionsUseCase
import cl.neoxcore.saifu.domain.GetTransactionsUseCase
import cl.neoxcore.saifu.presentation.mapper.UiTransactionMapper
import cl.neoxcore.saifu.presentation.mvi.execution.ExecutionThread
import cl.neoxcore.saifu.presentation.transaction.TransactionAction.GetTransactionAction
import cl.neoxcore.saifu.presentation.transaction.TransactionResult.GetTransactionResult
import cl.neoxcore.saifu.presentation.transaction.TransactionResult.GetTransactionResult.Error
import cl.neoxcore.saifu.presentation.transaction.TransactionResult.GetTransactionResult.ErrorWithCache
import cl.neoxcore.saifu.presentation.transaction.TransactionResult.GetTransactionResult.InProgress
import cl.neoxcore.saifu.presentation.transaction.TransactionResult.GetTransactionResult.Success
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

internal class TransactionProcessor @Inject constructor(
    private val getTransactionsUseCase: GetTransactionsUseCase,
    private val getCacheTransactionsUseCase: GetCacheTransactionsUseCase,
    private val transactionMapper: UiTransactionMapper,
    private val executionThread: ExecutionThread
) {

    fun actionProcessor(action: TransactionAction): Flow<TransactionResult> =
        when (action) {
            GetTransactionAction -> getTransactionProcessor()
        }

    @Suppress("TooGenericExceptionCaught", "USELESS_CAST")
    private fun getTransactionProcessor(): Flow<GetTransactionResult> =
        getTransactionsUseCase
            .execute()
            .map { transactions ->
                with(transactionMapper) {
                    Success(transactions.toUi())
                } as GetTransactionResult
            }.onStart {
                emit(InProgress)
            }.catch { cause ->
                try {
                    getCacheTransactionsUseCase.execute().collect { balance ->
                        emit(ErrorWithCache(cause, with(transactionMapper) { balance.toUi() }))
                    }
                } catch (e: Throwable) {
                    emit(Error(e))
                }
            }.flowOn(executionThread.ioThread())
}
