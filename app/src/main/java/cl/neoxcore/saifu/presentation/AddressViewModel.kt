package cl.neoxcore.saifu.presentation

import androidx.lifecycle.ViewModel
import cl.neoxcore.saifu.presentation.address.AddressAction
import cl.neoxcore.saifu.presentation.address.AddressAction.GenerateAddressAction
import cl.neoxcore.saifu.presentation.address.AddressAction.SaveAddressAction
import cl.neoxcore.saifu.presentation.address.AddressProcessor
import cl.neoxcore.saifu.presentation.address.AddressReducer
import cl.neoxcore.saifu.presentation.address.AddressUIntent
import cl.neoxcore.saifu.presentation.address.AddressUIntent.GenerateNewAddressUIntent
import cl.neoxcore.saifu.presentation.address.AddressUIntent.SaveAddressUIntent
import cl.neoxcore.saifu.presentation.address.AddressUiState
import cl.neoxcore.saifu.presentation.address.AddressUiState.DefaultUiState
import cl.neoxcore.saifu.presentation.mvi.MviPresentation
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.scan
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
class AddressViewModel @Inject constructor(
    private val reducer: AddressReducer,
    private val processor: AddressProcessor
) : ViewModel(), MviPresentation<AddressUIntent, AddressUiState> {

    private val defaultUiState: AddressUiState = DefaultUiState

    override fun processUserIntentsAndObserveUiStates(userIntents: Flow<AddressUIntent>): Flow<AddressUiState> =
        userIntents
            .buffer()
            .flatMapMerge { userIntent ->
                processor.actionProcessor(userIntent.toAction())
            }.scan(defaultUiState) { previousUiState, result ->
                with(reducer) { previousUiState reduceWith result }
            }

    private fun AddressUIntent.toAction(): AddressAction {
        return when (this) {
            GenerateNewAddressUIntent -> GenerateAddressAction
            is SaveAddressUIntent -> SaveAddressAction(address)
        }
    }
}
