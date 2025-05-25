package alireza.nezami.data.di

import alireza.nezami.data.repository.BookmarkRepositoryImpl
import alireza.nezami.data.util.LatestVideoSynchronizer
import alireza.nezami.data.util.VideoMapper
import alireza.nezami.data.util.PopularVideoSynchronizer
import alireza.nezami.database.dao.VideoDao
import alireza.nezami.domain.repository.BookmarkRepository
import alireza.nezami.network.data_source.VideoDataSource
import alireza.nezami.network.di.ApplicationScope
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

/**
 * Dagger module responsible for providing data dependencies.
 * Specifies the bindings for repository interfaces to their respective implementations.
 */
@Module
@InstallIn(SingletonComponent::class)
class DataModule private constructor() {
        companion object {
                @Provides
                @Singleton
                fun provideBookmarkRepository(repository: BookmarkRepositoryImpl): BookmarkRepository =
                        repository

                @Provides
                @Singleton
                fun providePopularVideoSynchronizer(
                        localDataSource: VideoDao,
                        remoteDataSource: VideoDataSource,
                        @ApplicationScope coroutineScope: CoroutineScope,
                        mapper: VideoMapper,
                ): PopularVideoSynchronizer =
                        PopularVideoSynchronizer(localDataSource, remoteDataSource, coroutineScope, mapper)

                @Provides
                @Singleton
                fun provideLatestVideoSynchronizer(
                        localDataSource: VideoDao,
                        remoteDataSource: VideoDataSource,
                        @ApplicationScope coroutineScope: CoroutineScope,
                        mapper: VideoMapper,
                ): LatestVideoSynchronizer =
                        LatestVideoSynchronizer(localDataSource, remoteDataSource, coroutineScope, mapper)

                @Provides
                @Singleton
                fun provideVideoMapper(): VideoMapper = VideoMapper()
        }
}