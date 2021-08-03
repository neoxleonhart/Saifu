package cl.neoxcore.saifu.presentation.transaction

import cl.neoxcore.saifu.presentation.mvi.MviUserIntent

internal sealed class TransactionUIntent : MviUserIntent {

    object LoadTransactionUIntent : TransactionUIntent()
}
