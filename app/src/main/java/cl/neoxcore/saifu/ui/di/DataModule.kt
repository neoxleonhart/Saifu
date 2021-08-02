package cl.neoxcore.saifu.ui.di

import cl.neoxcore.saifu.data.RepositoryImpl
import cl.neoxcore.saifu.data.cache.CacheImpl
import cl.neoxcore.saifu.data.remote.RemoteImpl
import cl.neoxcore.saifu.data.source.Cache
import cl.neoxcore.saifu.data.source.Remote
import cl.neoxcore.saifu.domain.repository.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Singleton
    @Provides
    fun provideRepository(data: RepositoryImpl): Repository {
        return data
    }

    @Singleton
    @Provides
    fun provideRemote(dataSourceRemote: RemoteImpl): Remote {
        return dataSourceRemote
    }

    @Singleton
    @Provides
    fun provideCache(dataSourceCache: CacheImpl): Cache {
        return dataSourceCache
    }
}
