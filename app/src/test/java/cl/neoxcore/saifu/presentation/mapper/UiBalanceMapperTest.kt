package cl.neoxcore.saifu.presentation.mapper

import cl.neoxcore.saifu.factory.BalanceFactory.makeBalance
import org.junit.Assert
import org.junit.Test

class UiBalanceMapperTest {
    private val mapper = UiBalanceMapper()

    @Test
    fun `given Balance, when toUi, then uiBalance`() {
        val balance = makeBalance()

        val uiBalance = with(mapper) { balance.toUi() }

        Assert.assertEquals("address", uiBalance.address, balance.address)
        Assert.assertEquals("balance", uiBalance.balance, balance.balance)
        Assert.assertEquals("finalBalance", uiBalance.finalBalance, balance.finalBalance)
        Assert.assertEquals(
            "unconfirmedBalance",
            uiBalance.unconfirmedBalance,
            balance.unconfirmedBalance
        )
    }
}
