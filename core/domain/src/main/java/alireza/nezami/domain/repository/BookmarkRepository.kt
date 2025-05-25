package alireza.nezami.domain.repository

import alireza.nezami.model.entity.BookmarkEntity
import kotlinx.coroutines.flow.Flow

interface BookmarkRepository {
    suspend fun getAllBookmarks(): Flow<List<BookmarkEntity>>
    suspend fun addBookmark(bookmark: BookmarkEntity): Flow<Unit>
    suspend fun removeBookmark(id: Int): Flow<Unit>
}