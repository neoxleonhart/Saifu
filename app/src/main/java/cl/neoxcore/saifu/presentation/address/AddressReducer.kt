package cl.neoxcore.saifu.presentation.address

import cl.neoxcore.saifu.presentation.address.AddressResult.GenerateAddressResult
import cl.neoxcore.saifu.presentation.address.AddressResult.SaveAddressResult
import cl.neoxcore.saifu.presentation.address.AddressUiState.DefaultUiState
import cl.neoxcore.saifu.presentation.address.AddressUiState.DisplayAddressUiState
import cl.neoxcore.saifu.presentation.address.AddressUiState.ErrorUiState
import cl.neoxcore.saifu.presentation.address.AddressUiState.LoadingUiState
import cl.neoxcore.saifu.presentation.address.AddressUiState.SaveUiState
import cl.neoxcore.saifu.presentation.mvi.MviReducer
import cl.neoxcore.saifu.presentation.mvi.UnsupportedReduceException
import javax.inject.Inject

class AddressReducer @Inject constructor() : MviReducer<AddressUiState, AddressResult> {
    override fun AddressUiState.reduceWith(result: AddressResult): AddressUiState {
        return when (val previousState = this) {
            is DefaultUiState -> previousState reduceWith result
            is DisplayAddressUiState -> previousState reduceWith result
            is LoadingUiState -> previousState reduceWith result
            is ErrorUiState -> previousState reduceWith result
            is SaveUiState -> previousState reduceWith result
        }
    }

    private infix fun DefaultUiState.reduceWith(result: AddressResult): AddressUiState {
        return when (result) {
            is GenerateAddressResult.InProgress -> LoadingUiState
            is SaveAddressResult.InProgress -> LoadingUiState
            else -> throw UnsupportedReduceException(this, result)
        }
    }

    private infix fun LoadingUiState.reduceWith(result: AddressResult): AddressUiState {
        return when (result) {
            is GenerateAddressResult.Success -> DisplayAddressUiState(result.address)
            is SaveAddressResult.Success -> SaveUiState
            is GenerateAddressResult.Error -> ErrorUiState(result.error)
            is SaveAddressResult.Error -> ErrorUiState(result.error)
            else -> throw UnsupportedReduceException(this, result)
        }
    }

    private infix fun ErrorUiState.reduceWith(result: AddressResult): AddressUiState {
        return when (result) {
            is GenerateAddressResult.InProgress -> LoadingUiState
            is SaveAddressResult.InProgress -> LoadingUiState
            is GenerateAddressResult.Error -> ErrorUiState(result.error)
            is SaveAddressResult.Error -> ErrorUiState(result.error)
            else -> throw UnsupportedReduceException(this, result)
        }
    }

    private infix fun DisplayAddressUiState.reduceWith(result: AddressResult): AddressUiState {
        return when (result) {
            is GenerateAddressResult.InProgress -> LoadingUiState
            is SaveAddressResult.InProgress -> LoadingUiState
            is GenerateAddressResult.Error -> ErrorUiState(result.error)
            is SaveAddressResult.Error -> ErrorUiState(result.error)
            else -> throw UnsupportedReduceException(this, result)
        }
    }

    private infix fun SaveUiState.reduceWith(result: AddressResult): AddressUiState =
        throw UnsupportedReduceException(this, result)
}
