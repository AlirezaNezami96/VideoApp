package alireza.nezami.data.repository

import alireza.nezami.database.entity.BookmarkEntity
import kotlinx.coroutines.flow.Flow

interface BookmarkRepository {
    suspend fun getAllBookmarks(): List<BookmarkEntity>
    suspend fun addBookmark(bookmark: BookmarkEntity)
    suspend fun removeBookmark(id: Int)
}