package cl.neoxcore.saifu.presentation.transaction

import cl.neoxcore.saifu.presentation.mvi.MviReducer
import cl.neoxcore.saifu.presentation.mvi.UnsupportedReduceException
import cl.neoxcore.saifu.presentation.transaction.TransactionResult.GetTransactionResult.Error
import cl.neoxcore.saifu.presentation.transaction.TransactionResult.GetTransactionResult.ErrorWithCache
import cl.neoxcore.saifu.presentation.transaction.TransactionResult.GetTransactionResult.InProgress
import cl.neoxcore.saifu.presentation.transaction.TransactionResult.GetTransactionResult.Success
import cl.neoxcore.saifu.presentation.transaction.TransactionUiState.DefaultUiState
import cl.neoxcore.saifu.presentation.transaction.TransactionUiState.DisplayTransactionUiState
import cl.neoxcore.saifu.presentation.transaction.TransactionUiState.ErrorUiState
import cl.neoxcore.saifu.presentation.transaction.TransactionUiState.ErrorWithCacheUiState
import cl.neoxcore.saifu.presentation.transaction.TransactionUiState.LoadingUiState
import javax.inject.Inject

internal class TransactionReducer @Inject constructor() :
    MviReducer<TransactionUiState, TransactionResult> {

    override infix fun TransactionUiState.reduceWith(result: TransactionResult): TransactionUiState {
        return when (val previousState = this) {
            is DefaultUiState -> previousState reduceWith result
            is LoadingUiState -> previousState reduceWith result
            is DisplayTransactionUiState -> previousState reduceWith result
            is ErrorUiState -> previousState reduceWith result
            is ErrorWithCacheUiState -> previousState reduceWith result
        }
    }

    private infix fun DefaultUiState.reduceWith(result: TransactionResult): TransactionUiState {
        return when (result) {
            is InProgress -> LoadingUiState
            else -> throw UnsupportedReduceException(this, result)
        }
    }

    private infix fun LoadingUiState.reduceWith(result: TransactionResult): TransactionUiState {
        return when (result) {
            is Success -> DisplayTransactionUiState(result.transactions)
            is ErrorWithCache -> ErrorWithCacheUiState(result.error, result.transactions)
            is Error -> ErrorUiState(result.error)
            else -> throw UnsupportedReduceException(this, result)
        }
    }

    private infix fun DisplayTransactionUiState.reduceWith(result: TransactionResult): TransactionUiState {
        return when (result) {
            is InProgress -> LoadingUiState
            else -> throw UnsupportedReduceException(this, result)
        }
    }

    private infix fun ErrorUiState.reduceWith(result: TransactionResult): TransactionUiState {
        return when (result) {
            is InProgress -> LoadingUiState
            else -> throw UnsupportedReduceException(this, result)
        }
    }

    private infix fun ErrorWithCacheUiState.reduceWith(result: TransactionResult): TransactionUiState {
        return when (result) {
            is InProgress -> LoadingUiState
            else -> throw UnsupportedReduceException(this, result)
        }
    }
}
