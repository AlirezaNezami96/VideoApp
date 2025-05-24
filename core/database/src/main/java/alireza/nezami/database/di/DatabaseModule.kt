package alireza.nezami.database.di

import alireza.nezami.database.database.VideoAppDatabase
import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule private constructor() {
    companion object {
        @Provides
        @Singleton
        fun providesDatabase(
            @ApplicationContext context: Context,
        ): VideoAppDatabase = Room.databaseBuilder(
            context,
            VideoAppDatabase::class.java,
            "video-app-database",
        ).build()
    }
}
