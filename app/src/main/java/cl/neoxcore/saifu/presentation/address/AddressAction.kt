package cl.neoxcore.saifu.presentation.address

import cl.neoxcore.saifu.presentation.mvi.MviAction

sealed class AddressAction : MviAction {
    object GetCacheAddressAction : AddressAction()
    object GenerateAddressAction : AddressAction()
    data class SaveAddressAction(val address: String) : AddressAction()
}
