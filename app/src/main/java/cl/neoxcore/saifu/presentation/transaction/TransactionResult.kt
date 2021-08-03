package cl.neoxcore.saifu.presentation.transaction

import cl.neoxcore.saifu.presentation.model.UiTransaction
import cl.neoxcore.saifu.presentation.mvi.MviResult

internal sealed class TransactionResult : MviResult {

    sealed class GetTransactionResult : TransactionResult() {

        object InProgress : GetTransactionResult()
        data class Success(val transactions: List<UiTransaction>) : GetTransactionResult()
        data class Error(val error: Throwable) : GetTransactionResult()
        data class ErrorWithCache(
            val error: Throwable,
            val transactions: List<UiTransaction>
        ) : GetTransactionResult()
    }
}
