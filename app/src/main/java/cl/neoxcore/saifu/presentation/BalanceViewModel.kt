package cl.neoxcore.saifu.presentation

import androidx.lifecycle.ViewModel
import cl.neoxcore.saifu.presentation.balance.BalanceAction
import cl.neoxcore.saifu.presentation.balance.BalanceAction.GetBalanceAction
import cl.neoxcore.saifu.presentation.balance.BalanceProcessor
import cl.neoxcore.saifu.presentation.balance.BalanceReducer
import cl.neoxcore.saifu.presentation.balance.BalanceUIntent
import cl.neoxcore.saifu.presentation.balance.BalanceUIntent.LoadBalanceUIntent
import cl.neoxcore.saifu.presentation.balance.BalanceUiState
import cl.neoxcore.saifu.presentation.balance.BalanceUiState.DefaultUiState
import cl.neoxcore.saifu.presentation.mvi.MviPresentation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.scan
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
@HiltViewModel
internal class BalanceViewModel @Inject constructor(
    private val reducer: BalanceReducer,
    private val processor: BalanceProcessor
) : ViewModel(), MviPresentation<BalanceUIntent, BalanceUiState> {

    private val defaultUiState: BalanceUiState = DefaultUiState

    override fun processUserIntentsAndObserveUiStates(userIntents: Flow<BalanceUIntent>):
            Flow<BalanceUiState> =
        userIntents
            .buffer()
            .flatMapMerge { userIntent ->
                processor.actionProcessor(userIntent.toAction())
            }
            .scan(defaultUiState) { previousUiState, result ->
                with(reducer) { previousUiState reduceWith result }
            }

    private fun BalanceUIntent.toAction(): BalanceAction = when (this) {
        LoadBalanceUIntent -> GetBalanceAction
    }
}
