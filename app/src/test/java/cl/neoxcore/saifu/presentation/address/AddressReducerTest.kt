package cl.neoxcore.saifu.presentation.address

import cl.neoxcore.saifu.factory.BaseFactory.randomString
import cl.neoxcore.saifu.presentation.address.AddressResult.GenerateAddressResult
import cl.neoxcore.saifu.presentation.address.AddressResult.SaveAddressResult
import cl.neoxcore.saifu.presentation.address.AddressUiState.DefaultUiState
import cl.neoxcore.saifu.presentation.address.AddressUiState.DisplayAddressUiState
import cl.neoxcore.saifu.presentation.address.AddressUiState.ErrorUiState
import cl.neoxcore.saifu.presentation.address.AddressUiState.LoadingUiState
import cl.neoxcore.saifu.presentation.address.AddressUiState.SaveUiState
import cl.neoxcore.saifu.presentation.mvi.UnsupportedReduceException
import org.junit.Test

class AddressReducerTest {
    private val reducer = AddressReducer()

    @Test
    fun `given DefaultUiState with GenerateAddressResult-InProgress, when reduceWith, then returns LoadingUiState`() {
        val previousUiState = DefaultUiState
        val result = GenerateAddressResult.InProgress

        val newUiState = with(reducer) { previousUiState reduceWith result }

        assert(newUiState is LoadingUiState)
    }

    @Test
    fun `given DefaultUiState with SaveAddressResult-InProgress, when reduceWith, then returns LoadingUiState`() {
        val previousUiState = DefaultUiState
        val result = SaveAddressResult.InProgress

        val newUiState = with(reducer) { previousUiState reduceWith result }

        assert(newUiState is LoadingUiState)
    }

    @Test(expected = UnsupportedReduceException::class)
    fun `given DefaultUiState with SaveAddressResult-Success, when reduceWith, then throw Exception`() {
        val previousUiState = DefaultUiState
        val result = SaveAddressResult.Success

        with(reducer) { previousUiState reduceWith result }
    }

    @Test
    fun `given LoadingUiState with GenerateAddress-Success, when reduceWith, then returns DisplayAddressUiState`() {
        val previousUiState = LoadingUiState
        val result = GenerateAddressResult.Success(randomString())

        val newUiState = with(reducer) { previousUiState reduceWith result }

        assert(newUiState is DisplayAddressUiState)
    }

    @Test
    fun `given LoadingUiState with SaveAddressResult-Success, when reduceWith, then returns SaveUiState`() {
        val previousUiState = LoadingUiState
        val result = SaveAddressResult.Success

        val newUiState = with(reducer) { previousUiState reduceWith result }

        assert(newUiState is SaveUiState)
    }

    @Test
    fun `given LoadingUiState with GenerateAddress-Error, when reduceWith, then returns ErrorUiState`() {
        val previousUiState = LoadingUiState
        val result = GenerateAddressResult.Error(Throwable("error"))

        val newUiState = with(reducer) { previousUiState reduceWith result }

        assert(newUiState is ErrorUiState)
    }

    @Test
    fun `given LoadingUiState with SaveAddressResult-Error, when reduceWith, then returns ErrorUiState`() {
        val previousUiState = LoadingUiState
        val result = SaveAddressResult.Error(Throwable("error"))

        val newUiState = with(reducer) { previousUiState reduceWith result }

        assert(newUiState is ErrorUiState)
    }

    @Test(expected = UnsupportedReduceException::class)
    fun `given LoadingUiState with SaveAddressResult-InProgress, when reduceWith, then throw Exception`() {
        val previousUiState = LoadingUiState
        val result = SaveAddressResult.InProgress

        with(reducer) { previousUiState reduceWith result }
    }

    @Test
    fun `given ErrorUiState with GenerateAddress-InProgress, when reduceWith, then returns LoadingUiState`() {
        val previousUiState = ErrorUiState(Throwable())
        val result = GenerateAddressResult.InProgress

        val newUiState = with(reducer) { previousUiState reduceWith result }

        assert(newUiState is LoadingUiState)
    }

    @Test
    fun `given ErrorUiState with SaveAddressResult-InProgress, when reduceWith, then returns LoadingUiState`() {
        val previousUiState = ErrorUiState(Throwable())
        val result = SaveAddressResult.InProgress

        val newUiState = with(reducer) { previousUiState reduceWith result }

        assert(newUiState is LoadingUiState)
    }

    @Test
    fun `given ErrorUiState with GenerateAddress-Error, when reduceWith, then returns ErrorUiState`() {
        val previousUiState = ErrorUiState(Throwable())
        val result = GenerateAddressResult.Error(Throwable("error"))

        val newUiState = with(reducer) { previousUiState reduceWith result }

        assert(newUiState is ErrorUiState)
    }

    @Test
    fun `given ErrorUiState with SaveAddressResult-Error, when reduceWith, then returns ErrorUiState`() {
        val previousUiState = ErrorUiState(Throwable())
        val result = SaveAddressResult.Error(Throwable("error"))

        val newUiState = with(reducer) { previousUiState reduceWith result }

        assert(newUiState is ErrorUiState)
    }

    @Test(expected = UnsupportedReduceException::class)
    fun `given ErrorUiState with SaveAddressResult-InProgress, when reduceWith, then throw Exception`() {
        val previousUiState = ErrorUiState(Throwable())
        val result = SaveAddressResult.Success

        with(reducer) { previousUiState reduceWith result }
    }

    @Test
    fun `given DisplayAddressUiState with GenerateAddress-InProgress, when reduceWith, then returns LoadingUiState`() {
        val previousUiState = DisplayAddressUiState(randomString())
        val result = GenerateAddressResult.InProgress

        val newUiState = with(reducer) { previousUiState reduceWith result }

        assert(newUiState is LoadingUiState)
    }

    @Test
    fun `given DisplayAddressUiState with SaveAddress-InProgress, when reduceWith, then returns LoadingUiState`() {
        val previousUiState = DisplayAddressUiState(randomString())
        val result = SaveAddressResult.InProgress

        val newUiState = with(reducer) { previousUiState reduceWith result }

        assert(newUiState is LoadingUiState)
    }

    @Test
    fun `given DisplayAddressUiState with GenerateAddress-Error, when reduceWith, then returns ErrorUiState`() {
        val previousUiState = DisplayAddressUiState(randomString())
        val result = GenerateAddressResult.Error(Throwable("error"))

        val newUiState = with(reducer) { previousUiState reduceWith result }

        assert(newUiState is ErrorUiState)
    }

    @Test
    fun `given DisplayAddressUiState with SaveAddressResult-Error, when reduceWith, then returns ErrorUiState`() {
        val previousUiState = DisplayAddressUiState(randomString())
        val result = SaveAddressResult.Error(Throwable("error"))

        val newUiState = with(reducer) { previousUiState reduceWith result }

        assert(newUiState is ErrorUiState)
    }

    @Test(expected = UnsupportedReduceException::class)
    fun `given DisplayAddressUiState with SaveAddressResult-InProgress, when reduceWith, then throw Exception`() {
        val previousUiState = DisplayAddressUiState(randomString())
        val result = SaveAddressResult.Success

        with(reducer) { previousUiState reduceWith result }
    }
}
