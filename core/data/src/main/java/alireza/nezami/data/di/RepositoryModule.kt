package alireza.nezami.data.di

import alireza.nezami.data.repository.BookmarkRepositoryImpl
import alireza.nezami.data.repository.VideoRepositoryImpl
import alireza.nezami.domain.repository.BookmarkRepository
import alireza.nezami.domain.repository.VideoRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    
    @Binds
    abstract fun bindVideoRepository(
        videoRepositoryImpl: VideoRepositoryImpl
    ): VideoRepository

    @Binds
    abstract fun provideBookmarkRepository(repository: BookmarkRepositoryImpl): BookmarkRepository
}