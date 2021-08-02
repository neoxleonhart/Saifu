package cl.neoxcore.saifu.ui.di

import cl.neoxcore.saifu.presentation.mvi.execution.AppExecutionThread
import cl.neoxcore.saifu.presentation.mvi.execution.ExecutionThread
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class ExecutionThreadModule {

    @Reusable
    @Provides
    fun provideExecutionThread(): ExecutionThread {
        return AppExecutionThread()
    }
}
