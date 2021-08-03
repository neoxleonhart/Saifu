package cl.neoxcore.saifu.presentation.balance

import cl.neoxcore.saifu.presentation.mvi.MviAction

internal sealed class BalanceAction : MviAction {
    object GetBalanceAction : BalanceAction()
}
