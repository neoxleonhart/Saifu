package cl.neoxcore.saifu.ui.navigator

import android.view.View
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import cl.neoxcore.saifu.ui.address.AddressFragmentDirections
import javax.inject.Inject

class Navigator @Inject constructor() {

    fun goToCharacterList(view: View) {
        val direction =
            AddressFragmentDirections.fromAddressToBalance()
        safeNavigation(view, direction)
    }

    private fun safeNavigation(view: View, direction: NavDirections) {
        view.findNavController().currentDestination?.getAction(direction.actionId)
            ?.let { view.findNavController().navigate(direction) }
    }
}
