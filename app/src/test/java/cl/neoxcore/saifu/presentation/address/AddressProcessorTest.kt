package cl.neoxcore.saifu.presentation.address

import cl.neoxcore.saifu.domain.GenerateAddressUseCase
import cl.neoxcore.saifu.domain.SaveAddressUseCase
import cl.neoxcore.saifu.factory.BaseFactory.randomString
import cl.neoxcore.saifu.presentation.address.AddressResult.GenerateAddressResult
import cl.neoxcore.saifu.presentation.address.AddressResult.SaveAddressResult
import cl.neoxcore.saifu.presentation.mvi.execution.ExecutionThreadEnvironment
import cl.neoxcore.saifu.presentation.mvi.execution.ExecutionThreadFactory
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

@FlowPreview
class AddressProcessorTest {
    private val generateUseCase = mockk<GenerateAddressUseCase>()
    private val saveUseCase = mockk<SaveAddressUseCase>()
    private val executionThread = ExecutionThreadFactory.makeExecutionThread(
        ExecutionThreadEnvironment.TESTING
    )
    private val processor = AddressProcessor(
        generateAddressUseCase = generateUseCase,
        saveAddressUseCase = saveUseCase,
        executionThread = executionThread
    )

    @Test
    fun `given GenerateAddressAction, when actionProcessor, then result InProgress`() =
        runBlocking {
            stubGenerateAddressUseCase(randomString())

            val resultFlow = processor.actionProcessor(AddressAction.GenerateAddressAction).toList()
            val mapOutput = resultFlow.first()

            assertTrue(mapOutput is GenerateAddressResult.InProgress)
        }

    @Test
    fun `given GenerateAddressAction, when actionProcessor, then result Success`() = runBlocking {
        stubGenerateAddressUseCase(randomString())

        val resultFlow = processor.actionProcessor(AddressAction.GenerateAddressAction).toList()
        val mapOutput = resultFlow.drop(1).first()

        assertTrue(mapOutput is GenerateAddressResult.Success)
    }

    @Test
    fun `given GenerateAddressAction, when actionProcessor, then result with data`() = runBlocking {
        val address = randomString()
        stubGenerateAddressUseCase(address)

        val resultFlow = processor.actionProcessor(AddressAction.GenerateAddressAction).toList()
        val mapOutput = resultFlow.drop(1).first()

        assertEquals(address, (mapOutput as GenerateAddressResult.Success).address)
    }

    @Test
    fun `given GenerateAddressAction with error, when actionProcessor, then result Error`() =
        runBlocking {
            stubGenerateAddressUseCaseError()

            val resultFlow = processor.actionProcessor(AddressAction.GenerateAddressAction).toList()
            val mapOutput = resultFlow.drop(1).first()

            assertTrue(mapOutput is GenerateAddressResult.Error)
        }

    @Test
    fun `given SaveAddressAction, when actionProcessor, then result InProgress`() =
        runBlocking {
            val address = randomString()
            stubSaveAddressUseCase(address, Unit)

            val resultFlow =
                processor.actionProcessor(AddressAction.SaveAddressAction(address)).toList()
            val mapOutput = resultFlow.first()

            assertTrue(mapOutput is SaveAddressResult.InProgress)
        }

    @Test
    fun `given SaveAddressAction, when actionProcessor, then result Success`() = runBlocking {
        val address = randomString()
        stubSaveAddressUseCase(address, Unit)

        val resultFlow =
            processor.actionProcessor(AddressAction.SaveAddressAction(address)).toList()
        val mapOutput = resultFlow.drop(1).first()

        assertTrue(mapOutput is SaveAddressResult.Success)
    }

    private fun stubGenerateAddressUseCase(address: String) {
        coEvery { generateUseCase.execute() } returns flow { emit(address) }
    }

    @Suppress("TooGenericExceptionThrown")
    private fun stubGenerateAddressUseCaseError(message: String = "") {
        coEvery { generateUseCase.execute() } returns flow { throw Throwable(message) }
    }

    private fun stubSaveAddressUseCase(address: String, unit: Unit) {
        coEvery { saveUseCase.execute(address) } returns unit
    }
}
