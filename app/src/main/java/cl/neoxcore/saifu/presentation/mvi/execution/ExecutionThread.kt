package cl.neoxcore.saifu.presentation.mvi.execution

import kotlinx.coroutines.CoroutineDispatcher

interface ExecutionThread {
    fun ioThread(): CoroutineDispatcher
    fun uiThread(): CoroutineDispatcher
}
