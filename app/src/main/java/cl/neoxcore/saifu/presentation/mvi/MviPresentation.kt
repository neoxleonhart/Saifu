package cl.neoxcore.saifu.presentation.mvi

import kotlinx.coroutines.flow.Flow

interface MviPresentation<TUserIntent : MviUserIntent, TUiState : MviUiState> {
    fun processUserIntentsAndObserveUiStates(userIntents: Flow<TUserIntent>): Flow<TUiState>
}
