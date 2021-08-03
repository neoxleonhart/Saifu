package cl.neoxcore.saifu.presentation.mapper

import cl.neoxcore.saifu.domain.model.Transaction
import cl.neoxcore.saifu.factory.TransactionFactory.makeTransactionList
import cl.neoxcore.saifu.presentation.model.UiTransaction
import org.junit.Assert
import org.junit.Test

class UiTransactionMapperTest {
    private val mapper = UiTransactionMapper()

    @Test
    fun `given Balance, when toUi, then uiBalance`() {
        val transactions = makeTransactionList(3)

        val uiTransactions = with(mapper) { transactions.toUi() }

        assetTransactionsEquals(transactions, uiTransactions)
    }

    private fun assetTransactionsEquals(
        transactions: List<Transaction>,
        uiTransactions: List<UiTransaction>
    ) {
        transactions.zip(uiTransactions).map {
            Assert.assertEquals("id", it.first.id, it.second.id)
            Assert.assertEquals("date", it.first.date, it.second.date)
            Assert.assertEquals("total", it.first.total, it.second.total)
        }
    }
}
