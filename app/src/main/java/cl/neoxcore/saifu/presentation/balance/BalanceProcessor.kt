package cl.neoxcore.saifu.presentation.balance

import cl.neoxcore.saifu.domain.GetBalanceUseCase
import cl.neoxcore.saifu.domain.GetCacheBalanceUseCase
import cl.neoxcore.saifu.presentation.balance.BalanceAction.GetBalanceAction
import cl.neoxcore.saifu.presentation.balance.BalanceResult.GetBalanceResult
import cl.neoxcore.saifu.presentation.balance.BalanceResult.GetBalanceResult.Error
import cl.neoxcore.saifu.presentation.balance.BalanceResult.GetBalanceResult.ErrorWithCache
import cl.neoxcore.saifu.presentation.balance.BalanceResult.GetBalanceResult.InProgress
import cl.neoxcore.saifu.presentation.balance.BalanceResult.GetBalanceResult.Success
import cl.neoxcore.saifu.presentation.mapper.UiBalanceMapper
import cl.neoxcore.saifu.presentation.mvi.execution.ExecutionThread
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

internal class BalanceProcessor @Inject constructor(
    private val getBalanceUseCase: GetBalanceUseCase,
    private val getCacheBalanceUseCase: GetCacheBalanceUseCase,
    private val balanceMapper: UiBalanceMapper,
    private val executionThread: ExecutionThread
) {

    fun actionProcessor(action: BalanceAction): Flow<BalanceResult> =
        when (action) {
            GetBalanceAction -> getBalanceProcessor()
        }

    @Suppress("TooGenericExceptionCaught", "USELESS_CAST")
    private fun getBalanceProcessor(): Flow<GetBalanceResult> =
        getBalanceUseCase
            .execute()
            .map { balance ->
                Success(with(balanceMapper) { balance.toUi() })
                        as GetBalanceResult
            }.onStart {
                emit(InProgress)
            }.catch { cause ->
                try {
                    getCacheBalanceUseCase.execute().collect { balance ->
                        emit(ErrorWithCache(cause, with(balanceMapper) { balance.toUi() }))
                    }
                } catch (e: Throwable) {
                    emit(Error(e))
                }
            }.flowOn(executionThread.ioThread())
}
