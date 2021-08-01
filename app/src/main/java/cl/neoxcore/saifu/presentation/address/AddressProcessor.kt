package cl.neoxcore.saifu.presentation.address

import cl.neoxcore.saifu.domain.GenerateAddressUseCase
import cl.neoxcore.saifu.domain.SaveAddressUseCase
import cl.neoxcore.saifu.presentation.address.AddressAction.GenerateAddressAction
import cl.neoxcore.saifu.presentation.address.AddressAction.SaveAddressAction
import cl.neoxcore.saifu.presentation.address.AddressResult.GenerateAddressResult
import cl.neoxcore.saifu.presentation.address.AddressResult.SaveAddressResult
import cl.neoxcore.saifu.presentation.mvi.execution.ExecutionThread
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class AddressProcessor @Inject constructor(
    private val generateAddressUseCase: GenerateAddressUseCase,
    private val saveAddressUseCase: SaveAddressUseCase,
    private val executionThread: ExecutionThread
) {
    fun actionProcessor(action: AddressAction): Flow<AddressResult> = when (action) {
        GenerateAddressAction -> generateAddressProcessor()
        is SaveAddressAction -> saveAddressProcessor(action.address)
    }

    @Suppress("USELESS_CAST")
    private fun generateAddressProcessor(): Flow<AddressResult> =
        generateAddressUseCase.execute()
            .map {
                GenerateAddressResult.Success(it) as GenerateAddressResult
            }.onStart {
                emit(GenerateAddressResult.InProgress)
            }.catch {
                emit(GenerateAddressResult.Error(it))
            }.flowOn(executionThread.ioThread())

    private fun saveAddressProcessor(address: String) = flow<AddressResult> {
        saveAddressUseCase.execute(address)
        emit(SaveAddressResult.Success)
    }.onStart {
        emit(SaveAddressResult.InProgress)
    }.catch {
        emit(SaveAddressResult.Error(it))
    }.flowOn(executionThread.ioThread())
}
