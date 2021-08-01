package cl.neoxcore.saifu.presentation.mvi.execution

import cl.neoxcore.saifu.presentation.mvi.execution.ExecutionThreadEnvironment.APPLICATION
import cl.neoxcore.saifu.presentation.mvi.execution.ExecutionThreadEnvironment.TESTING

object ExecutionThreadFactory {
    fun makeExecutionThread(environment: ExecutionThreadEnvironment): ExecutionThread =
        when (environment) {
            APPLICATION -> AppExecutionThread()
            TESTING -> AppExecutionThread()
        }
}
