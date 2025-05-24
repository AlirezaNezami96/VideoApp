package alireza.nezami.data.di

import alireza.nezami.data.repository.BookmarkRepository
import alireza.nezami.data.repository.BookmarkRepositoryImpl
import alireza.nezami.data.repository.VideoRepository
import alireza.nezami.data.repository.VideoRepositoryImpl
import alireza.nezami.data.util.ConnectivityManagerNetworkMonitor
import alireza.nezami.data.util.NetworkMonitor
import alireza.nezami.data.util.VideoMapper
import alireza.nezami.data.util.VideoSynchronizer
import alireza.nezami.database.dao.VideoDao
import alireza.nezami.network.data_source.VideoDataSource
import alireza.nezami.network.di.ApplicationScope
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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
        /**
         * Provides the [BookmarkRepository] implementation by using the [BookmarkRepositoryImpl].
         *
         * @param repository The [BookmarkRepositoryImpl] instance.
         * @return The [BookmarkRepository] implementation.
         */
        @Provides
        @Singleton
        fun provideBookmarkRepository(repository: BookmarkRepositoryImpl): BookmarkRepository {
            return repository
        }

        /**
         * Provides the [VideoRepository] implementation by using the [VideoRepositoryImpl].
         *
         * @param videoSynchronizer The [VideoSynchronizer] instance.
         * @param remoteDataSource The [VideoDataSource] instance.
         * @return The [VideoRepository] implementation.
         */
        @Provides
        @Singleton
        fun provideVideoRepository(
                videoSynchronizer: VideoSynchronizer,
                remoteDataSource: VideoDataSource
        ): VideoRepository {
            return VideoRepositoryImpl(
                videoSynchronizer,
                remoteDataSource
            )
        }

        /**
         * Provides the [NetworkMonitor] implementation using [ConnectivityManagerNetworkMonitor].
         *
         * @return The [NetworkMonitor] instance.
         */
        @Provides
        fun provideNetworkMonitor(@ApplicationContext context: Context): NetworkMonitor {
            return ConnectivityManagerNetworkMonitor(context)
        }

        @Provides
        @Singleton
        fun provideVideoSynchronizer(
            localDataSource: VideoDao,
            remoteDataSource: VideoDataSource,
            @ApplicationScope coroutineScope: CoroutineScope,
            mapper: VideoMapper,
        ): VideoSynchronizer {
            return VideoSynchronizer(localDataSource, remoteDataSource, coroutineScope, mapper)
        }

        @Provides
        @Singleton
        fun provideVideoMapper(): VideoMapper {
            return VideoMapper()
        }
    }
}
