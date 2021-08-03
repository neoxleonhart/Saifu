package cl.neoxcore.saifu.presentation.balance

import cl.neoxcore.saifu.presentation.mvi.MviUserIntent

internal sealed class BalanceUIntent : MviUserIntent {

    object LoadBalanceUIntent : BalanceUIntent()
}
