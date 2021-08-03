package cl.neoxcore.saifu.presentation

import androidx.lifecycle.ViewModel
import cl.neoxcore.saifu.presentation.mvi.MviPresentation
import cl.neoxcore.saifu.presentation.transaction.TransactionAction
import cl.neoxcore.saifu.presentation.transaction.TransactionAction.GetTransactionAction
import cl.neoxcore.saifu.presentation.transaction.TransactionProcessor
import cl.neoxcore.saifu.presentation.transaction.TransactionReducer
import cl.neoxcore.saifu.presentation.transaction.TransactionUIntent
import cl.neoxcore.saifu.presentation.transaction.TransactionUIntent.LoadTransactionUIntent
import cl.neoxcore.saifu.presentation.transaction.TransactionUiState
import cl.neoxcore.saifu.presentation.transaction.TransactionUiState.DefaultUiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.scan
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
internal class TransactionViewModel @Inject constructor(
    private val reducer: TransactionReducer,
    private val processor: TransactionProcessor
) : ViewModel(), MviPresentation<TransactionUIntent, TransactionUiState> {

    private val defaultUiState: TransactionUiState = DefaultUiState

    override fun processUserIntentsAndObserveUiStates(userIntents: Flow<TransactionUIntent>):
            Flow<TransactionUiState> =
        userIntents
            .buffer()
            .flatMapMerge { userIntent ->
                processor.actionProcessor(userIntent.toAction())
            }
            .scan(defaultUiState) { previousUiState, result ->
                with(reducer) { previousUiState reduceWith result }
            }

    private fun TransactionUIntent.toAction(): TransactionAction = when (this) {
        LoadTransactionUIntent -> GetTransactionAction
    }
}
