package cl.neoxcore.saifu.presentation.balance

import cl.neoxcore.saifu.factory.BalanceFactory.makeUiBalance
import cl.neoxcore.saifu.presentation.balance.BalanceResult.GetBalanceResult
import cl.neoxcore.saifu.presentation.balance.BalanceResult.GetBalanceResult.Error
import cl.neoxcore.saifu.presentation.balance.BalanceResult.GetBalanceResult.InProgress
import cl.neoxcore.saifu.presentation.balance.BalanceResult.GetBalanceResult.Success
import cl.neoxcore.saifu.presentation.balance.BalanceUiState.DefaultUiState
import cl.neoxcore.saifu.presentation.balance.BalanceUiState.DisplayBalanceUiState
import cl.neoxcore.saifu.presentation.balance.BalanceUiState.ErrorUiState
import cl.neoxcore.saifu.presentation.balance.BalanceUiState.ErrorWithCacheUiState
import cl.neoxcore.saifu.presentation.balance.BalanceUiState.LoadingUiState
import cl.neoxcore.saifu.presentation.mvi.UnsupportedReduceException
import org.junit.Test

class BalanceReducerTest {
    private val reducer = BalanceReducer()

    @Test
    fun `given DefaultUiState with GetBalanceResult-InProgress, when reduceWith, then returns LoadingUiState`() {
        val previousUiState = DefaultUiState
        val result = InProgress

        val newUiState = with(reducer) { previousUiState reduceWith result }

        assert(newUiState is LoadingUiState)
    }

    @Test(expected = UnsupportedReduceException::class)
    fun `given DefaultUiState with GetBalanceResult-Success, when reduceWith, then throw Exception`() {
        val previousUiState = DefaultUiState
        val uiBalance = makeUiBalance()
        val result = Success(uiBalance)

        with(reducer) { previousUiState reduceWith result }
    }

    @Test
    fun `given LoadingUiState with GetBalanceResult-Success, when reduceWith, then returns DisplayBalanceUiState`() {
        val previousUiState = LoadingUiState
        val uiBalance = makeUiBalance()
        val result = Success(uiBalance)

        val newUiState = with(reducer) { previousUiState reduceWith result }

        assert(newUiState is DisplayBalanceUiState)
    }

    @Test
    fun `given LoadingUiState with ErrorWithCache, when reduceWith, then returns ErrorWithCacheUiState`() {
        val previousUiState = LoadingUiState
        val uiBalance = makeUiBalance()
        val result = GetBalanceResult.ErrorWithCache(Throwable("error"), uiBalance)

        val newUiState = with(reducer) { previousUiState reduceWith result }

        assert(newUiState is ErrorWithCacheUiState)
    }

    @Test
    fun `given LoadingUiState with GetBalanceResult-Error, when reduceWith, then returns ErrorUiState`() {
        val previousUiState = LoadingUiState
        val result = Error(Throwable("error"))

        val newUiState = with(reducer) { previousUiState reduceWith result }

        assert(newUiState is ErrorUiState)
    }

    @Test
    fun `given ErrorUiState with GetBalanceResult-InProgress, when reduceWith, then returns LoadingUiState`() {
        val previousUiState = ErrorUiState(Throwable())
        val result = InProgress

        val newUiState = with(reducer) { previousUiState reduceWith result }

        assert(newUiState is LoadingUiState)
    }

    @Test
    fun `given DisplayBalanceUiState with GetBalanceResult-InProgress, when reduceWith, then returns LoadingUiState`() {
        val uiBalance = makeUiBalance()
        val previousUiState = DisplayBalanceUiState(uiBalance)
        val result = InProgress

        val newUiState = with(reducer) { previousUiState reduceWith result }

        assert(newUiState is LoadingUiState)
    }
}
