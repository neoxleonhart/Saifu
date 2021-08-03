package cl.neoxcore.saifu.ui.navigator

import android.view.View
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import cl.neoxcore.saifu.ui.address.AddressFragmentDirections
import cl.neoxcore.saifu.ui.balance.BalanceFragmentDirections
import javax.inject.Inject

class Navigator @Inject constructor() {

    fun goToAddress(view: View) {
        val direction =
            AddressFragmentDirections.fromAddressToBalance()
        safeNavigation(view, direction)
    }

    fun goToTransactions(view: View) {
        val direction =
            BalanceFragmentDirections.fromBalanceToTransactions()
        safeNavigation(view, direction)
    }

    private fun safeNavigation(view: View, direction: NavDirections) {
        view.findNavController().currentDestination?.getAction(direction.actionId)
            ?.let { view.findNavController().navigate(direction) }
    }
}
