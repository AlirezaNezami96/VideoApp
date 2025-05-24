package alireza.nezami.database.di

import alireza.nezami.database.dao.VideoDao
import alireza.nezami.database.dao.BookmarkDao
import alireza.nezami.database.database.VideoAppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DaosModule {
    @Provides
    fun provideVideosDao(
            database: VideoAppDatabase,
    ): VideoDao = database.videoDao()

    @Provides
    fun providesBookmarkDao(
            database: VideoAppDatabase,
    ): BookmarkDao = database.bookmarkDao()

}
