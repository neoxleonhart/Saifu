package cl.neoxcore.saifu.presentation.address

import cl.neoxcore.saifu.presentation.mvi.MviUiState

sealed class AddressUiState : MviUiState {
    object DefaultUiState : AddressUiState()
    object LoadingUiState : AddressUiState()
    data class ErrorUiState(val error: Throwable) : AddressUiState()
    data class DisplayAddressUiState(val address: String) : AddressUiState()
    object SaveUiState : AddressUiState()
}
