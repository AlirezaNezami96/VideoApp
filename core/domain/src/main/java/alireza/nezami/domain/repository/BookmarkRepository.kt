package alireza.nezami.domain.repository

import alireza.nezami.model.entity.BookmarkEntity

interface BookmarkRepository {
    suspend fun getAllBookmarks(): List<BookmarkEntity>
    suspend fun addBookmark(bookmark: BookmarkEntity)
    suspend fun removeBookmark(id: Int)
}