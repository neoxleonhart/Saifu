package cl.neoxcore.saifu.presentation.address

import cl.neoxcore.saifu.presentation.mvi.MviResult

sealed class AddressResult : MviResult {
    sealed class GenerateAddressResult : AddressResult() {
        object InProgress : GenerateAddressResult()
        data class Success(val address: String) : GenerateAddressResult()
        data class Error(val error: Throwable) : GenerateAddressResult()
    }

    sealed class SaveAddressResult : AddressResult() {
        object InProgress : SaveAddressResult()
        object Success : SaveAddressResult()
        data class Error(val error: Throwable) : SaveAddressResult()
    }

    sealed class GetCacheAddressResult : AddressResult() {
        object InProgress : GetCacheAddressResult()
        data class Success(val address: String) : GetCacheAddressResult()
        data class Error(val error: Throwable) : GetCacheAddressResult()
    }
}
