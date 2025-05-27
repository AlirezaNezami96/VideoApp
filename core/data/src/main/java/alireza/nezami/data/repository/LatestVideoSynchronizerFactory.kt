package alireza.nezami.data.repository

import alireza.nezami.data.util.LatestVideoSynchronizer
import alireza.nezami.data.util.VideoMapper
import alireza.nezami.database.dao.BookmarkDao
import alireza.nezami.database.dao.VideoDao
import alireza.nezami.network.data_source.VideoDataSource
import alireza.nezami.network.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LatestVideoSynchronizerFactory @Inject constructor(
        private val localVideoDataSource: VideoDao,
        private val localBookmarkDataSource: BookmarkDao,
        private val remoteDataSource: VideoDataSource,
        private val mapper: VideoMapper,
        @ApplicationScope private val externalScope: CoroutineScope
) {
    fun create(): LatestVideoSynchronizer {
        return LatestVideoSynchronizer(
            localVideoDataSource, localBookmarkDataSource, remoteDataSource, externalScope, mapper
        )
    }
}