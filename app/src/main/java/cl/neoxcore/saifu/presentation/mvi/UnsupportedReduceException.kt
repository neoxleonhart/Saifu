package cl.neoxcore.saifu.presentation.mvi

class UnsupportedReduceException(state: MviUiState, result: MviResult) :
    RuntimeException("Cannot reduce state: $state with result: $result")
