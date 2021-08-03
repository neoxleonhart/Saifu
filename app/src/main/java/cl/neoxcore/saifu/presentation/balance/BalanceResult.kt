package cl.neoxcore.saifu.presentation.balance

import cl.neoxcore.saifu.presentation.model.UiBalance
import cl.neoxcore.saifu.presentation.mvi.MviResult

internal sealed class BalanceResult : MviResult {

    sealed class GetBalanceResult : BalanceResult() {
        object InProgress : GetBalanceResult()
        data class Success(val balance: UiBalance) : GetBalanceResult()
        data class Error(val error: Throwable) : GetBalanceResult()
        data class ErrorWithCache(val error: Throwable, val balance: UiBalance) : GetBalanceResult()
    }
}
