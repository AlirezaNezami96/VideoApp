package alireza.nezami.database.di

import alireza.nezami.database.dao.VideoDao
import alireza.nezami.database.dao.BookmarkDao
import alireza.nezami.database.database.MovieAppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DaosModule {
    @Provides
    fun providesFavoriteDao(
            database: MovieAppDatabase,
    ): VideoDao = database.videoDao()

    @Provides
    fun providesGenreDao(
            database: MovieAppDatabase,
    ): BookmarkDao = database.bookmarkDao()

}
