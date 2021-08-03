package cl.neoxcore.saifu.presentation.balance

import cl.neoxcore.saifu.presentation.balance.BalanceResult.GetBalanceResult.Error
import cl.neoxcore.saifu.presentation.balance.BalanceResult.GetBalanceResult.ErrorWithCache
import cl.neoxcore.saifu.presentation.balance.BalanceResult.GetBalanceResult.InProgress
import cl.neoxcore.saifu.presentation.balance.BalanceResult.GetBalanceResult.Success
import cl.neoxcore.saifu.presentation.balance.BalanceUiState.DefaultUiState
import cl.neoxcore.saifu.presentation.balance.BalanceUiState.DisplayBalanceUiState
import cl.neoxcore.saifu.presentation.balance.BalanceUiState.ErrorUiState
import cl.neoxcore.saifu.presentation.balance.BalanceUiState.ErrorWithCacheUiState
import cl.neoxcore.saifu.presentation.balance.BalanceUiState.LoadingUiState
import cl.neoxcore.saifu.presentation.mvi.MviReducer
import cl.neoxcore.saifu.presentation.mvi.UnsupportedReduceException
import javax.inject.Inject

internal class BalanceReducer @Inject constructor() :
    MviReducer<BalanceUiState, BalanceResult> {

    override infix fun BalanceUiState.reduceWith(result: BalanceResult): BalanceUiState {
        return when (val previousState = this) {
            is DefaultUiState -> previousState reduceWith result
            is LoadingUiState -> previousState reduceWith result
            is DisplayBalanceUiState -> previousState reduceWith result
            is ErrorUiState -> previousState reduceWith result
            is ErrorWithCacheUiState -> previousState reduceWith result
        }
    }

    private infix fun DefaultUiState.reduceWith(result: BalanceResult): BalanceUiState {
        return when (result) {
            is InProgress -> LoadingUiState
            else -> throw UnsupportedReduceException(this, result)
        }
    }

    private infix fun LoadingUiState.reduceWith(result: BalanceResult): BalanceUiState {
        return when (result) {
            is Success -> DisplayBalanceUiState(result.balance)
            is ErrorWithCache -> ErrorWithCacheUiState(result.error, result.balance)
            is Error -> ErrorUiState(result.error)
            else -> throw UnsupportedReduceException(this, result)
        }
    }

    private infix fun DisplayBalanceUiState.reduceWith(result: BalanceResult): BalanceUiState {
        return when (result) {
            is InProgress -> LoadingUiState
            else -> throw UnsupportedReduceException(this, result)
        }
    }

    private infix fun ErrorUiState.reduceWith(result: BalanceResult): BalanceUiState {
        return when (result) {
            is InProgress -> LoadingUiState
            else -> throw UnsupportedReduceException(this, result)
        }
    }

    private infix fun ErrorWithCacheUiState.reduceWith(result: BalanceResult): BalanceUiState {
        return when (result) {
            is InProgress -> LoadingUiState
            else -> throw UnsupportedReduceException(this, result)
        }
    }
}
