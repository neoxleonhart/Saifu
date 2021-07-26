package cl.neoxcore.saifu.presentation.mvi

import kotlinx.coroutines.flow.Flow

interface MviUi<TUserIntent : MviUserIntent, in TUiState : MviUiState> {
    fun userIntents(): Flow<TUserIntent>
    fun renderUiStates(uiState: TUiState)
}
