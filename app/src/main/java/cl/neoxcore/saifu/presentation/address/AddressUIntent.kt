package cl.neoxcore.saifu.presentation.address

import cl.neoxcore.saifu.presentation.mvi.MviUserIntent

sealed class AddressUIntent : MviUserIntent {
    object GenerateNewAddressUIntent : AddressUIntent()
    data class SaveAddressUIntent(val address: String) : AddressUIntent()
}
