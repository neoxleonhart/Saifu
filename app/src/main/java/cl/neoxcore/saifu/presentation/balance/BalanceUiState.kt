package cl.neoxcore.saifu.presentation.balance

import cl.neoxcore.saifu.presentation.model.UiBalance
import cl.neoxcore.saifu.presentation.mvi.MviUiState

internal sealed class BalanceUiState : MviUiState {

    object DefaultUiState : BalanceUiState()
    object LoadingUiState : BalanceUiState()
    data class DisplayBalanceUiState(val balance: UiBalance) : BalanceUiState()
    data class ErrorWithCacheUiState(val error: Throwable, val balance: UiBalance) :
        BalanceUiState()

    data class ErrorUiState(val error: Throwable) : BalanceUiState()
}
