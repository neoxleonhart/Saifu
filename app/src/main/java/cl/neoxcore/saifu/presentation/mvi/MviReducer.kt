package cl.neoxcore.saifu.presentation.mvi

interface MviReducer<TUiState : MviUiState, TResult : MviResult> {
    infix fun TUiState.reduceWith(result: TResult): TUiState
}
