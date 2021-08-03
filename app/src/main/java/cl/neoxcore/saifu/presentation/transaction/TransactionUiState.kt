package cl.neoxcore.saifu.presentation.transaction

import cl.neoxcore.saifu.presentation.model.UiTransaction
import cl.neoxcore.saifu.presentation.mvi.MviUiState

internal sealed class TransactionUiState : MviUiState {

    object DefaultUiState : TransactionUiState()
    object LoadingUiState : TransactionUiState()
    data class DisplayTransactionUiState(val transactions: List<UiTransaction>) :
        TransactionUiState()

    data class ErrorUiState(val error: Throwable) : TransactionUiState()
    data class ErrorWithCacheUiState(
        val error: Throwable,
        val transactions: List<UiTransaction>
    ) : TransactionUiState()
}
