package cl.neoxcore.saifu.presentation.mapper

import cl.neoxcore.saifu.domain.model.Transaction
import cl.neoxcore.saifu.presentation.model.UiTransaction
import javax.inject.Inject

class UiTransactionMapper @Inject constructor() {

    fun List<Transaction>.toUi(): List<UiTransaction> {
        return this.map { it.toUi() }
    }

    private fun Transaction.toUi() = UiTransaction(
        id = id,
        date = date,
        total = total
    )
}
