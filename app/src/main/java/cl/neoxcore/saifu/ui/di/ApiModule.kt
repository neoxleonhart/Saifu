package cl.neoxcore.saifu.ui.di

import cl.neoxcore.saifu.data.remote.retrofit.WebService
import dagger.Module
import dagger.Provides
import dagger.Reusable
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {
    @Reusable
    @Provides
    fun provideDogApi(retrofit: Retrofit): WebService {
        return retrofit.create(WebService::class.java)
    }
}
