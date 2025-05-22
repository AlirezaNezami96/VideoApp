package alireza.nezami.database.di

import alireza.nezami.database.database.AppDatabase
import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext, AppDatabase::class.java, "app_database"
        ).build()
    }

    @Singleton
    @Provides
    fun provideVideoDao(appDatabase: AppDatabase) = appDatabase.videoDao()

    @Singleton
    @Provides
    fun provideBookmarkDao(appDatabase: AppDatabase) = appDatabase.bookmarkDao()
}