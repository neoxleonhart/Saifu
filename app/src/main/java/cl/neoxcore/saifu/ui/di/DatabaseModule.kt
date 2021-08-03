package cl.neoxcore.saifu.ui.di

import android.content.Context
import androidx.room.Room
import cl.neoxcore.saifu.data.cache.database.DatabaseBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): DatabaseBuilder {
        return Room.databaseBuilder(
            appContext,
            DatabaseBuilder::class.java,
            "saifu_db"
        ).build()
    }
}
