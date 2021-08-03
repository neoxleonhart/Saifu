package cl.neoxcore.saifu.presentation.transaction

import cl.neoxcore.saifu.presentation.mvi.MviAction

internal sealed class TransactionAction : MviAction {

    object GetTransactionAction : TransactionAction()
}
