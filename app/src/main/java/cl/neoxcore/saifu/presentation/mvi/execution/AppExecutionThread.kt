package cl.neoxcore.saifu.presentation.mvi.execution

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class AppExecutionThread : ExecutionThread {
    override fun ioThread(): CoroutineDispatcher = Dispatchers.IO

    override fun uiThread(): CoroutineDispatcher = Dispatchers.Main
}
