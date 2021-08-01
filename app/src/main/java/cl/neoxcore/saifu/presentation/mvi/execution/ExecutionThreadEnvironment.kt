package cl.neoxcore.saifu.presentation.mvi.execution

sealed class ExecutionThreadEnvironment {
    object APPLICATION : ExecutionThreadEnvironment()
    object TESTING : ExecutionThreadEnvironment()
}
