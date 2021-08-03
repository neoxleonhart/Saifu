package cl.neoxcore.saifu.presentation.transaction

import cl.neoxcore.saifu.factory.TransactionFactory.makeUiTransactionList
import cl.neoxcore.saifu.presentation.mvi.UnsupportedReduceException
import cl.neoxcore.saifu.presentation.transaction.TransactionResult.GetTransactionResult.Error
import cl.neoxcore.saifu.presentation.transaction.TransactionResult.GetTransactionResult.ErrorWithCache
import cl.neoxcore.saifu.presentation.transaction.TransactionResult.GetTransactionResult.InProgress
import cl.neoxcore.saifu.presentation.transaction.TransactionResult.GetTransactionResult.Success
import cl.neoxcore.saifu.presentation.transaction.TransactionUiState.DefaultUiState
import cl.neoxcore.saifu.presentation.transaction.TransactionUiState.DisplayTransactionUiState
import cl.neoxcore.saifu.presentation.transaction.TransactionUiState.ErrorUiState
import cl.neoxcore.saifu.presentation.transaction.TransactionUiState.ErrorWithCacheUiState
import cl.neoxcore.saifu.presentation.transaction.TransactionUiState.LoadingUiState
import org.junit.Test

class TransactionReducerTest {
    private val reducer = TransactionReducer()

    @Test
    fun `given DefaultUiState with InProgress, when reduceWith, then returns LoadingUiState`() {
        val previousUiState = DefaultUiState
        val result = InProgress

        val newUiState = with(reducer) { previousUiState reduceWith result }

        assert(newUiState is LoadingUiState)
    }

    @Test(expected = UnsupportedReduceException::class)
    fun `given DefaultUiState with Success, when reduceWith, then throw Exception`() {
        val previousUiState = DefaultUiState
        val uiTransactions = makeUiTransactionList(3)
        val result = Success(uiTransactions)

        with(reducer) { previousUiState reduceWith result }
    }

    @Test
    fun `given LoadingUiState with Success, when reduceWith, then returns DisplayBalanceUiState`() {
        val previousUiState = LoadingUiState
        val uiTransactions = makeUiTransactionList(3)
        val result = Success(uiTransactions)

        val newUiState = with(reducer) { previousUiState reduceWith result }

        assert(newUiState is DisplayTransactionUiState)
    }

    @Test
    fun `given LoadingUiState with ErrorWithCache, when reduceWith, then returns ErrorWithCacheUiState`() {
        val previousUiState = LoadingUiState
        val uiTransactions = makeUiTransactionList(3)
        val result = ErrorWithCache(Throwable("error"), uiTransactions)

        val newUiState = with(reducer) { previousUiState reduceWith result }

        assert(newUiState is ErrorWithCacheUiState)
    }

    @Test
    fun `given LoadingUiState with Error, when reduceWith, then returns ErrorUiState`() {
        val previousUiState = LoadingUiState
        val result = Error(Throwable("error"))

        val newUiState = with(reducer) { previousUiState reduceWith result }

        assert(newUiState is ErrorUiState)
    }

    @Test
    fun `given ErrorUiState with InProgress, when reduceWith, then returns LoadingUiState`() {
        val previousUiState = ErrorUiState(Throwable())
        val result = InProgress

        val newUiState = with(reducer) { previousUiState reduceWith result }

        assert(newUiState is LoadingUiState)
    }

    @Test
    fun `given DisplayBalanceUiState with InProgress, when reduceWith, then returns LoadingUiState`() {
        val uiTransactions = makeUiTransactionList(3)
        val previousUiState = DisplayTransactionUiState(uiTransactions)
        val result = InProgress

        val newUiState = with(reducer) { previousUiState reduceWith result }

        assert(newUiState is LoadingUiState)
    }
}
