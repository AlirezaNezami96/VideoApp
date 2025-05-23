package alireza.nezami.data.repository

import alireza.nezami.database.dao.BookmarkDao
import alireza.nezami.model.entity.BookmarkEntity
import javax.inject.Inject

class BookmarkRepositoryImpl @Inject constructor(
        private val bookmarkDao: BookmarkDao
) : BookmarkRepository {
    override suspend fun getAllBookmarks(): List<BookmarkEntity> = bookmarkDao.getAllBookmarks()

    override suspend fun addBookmark(bookmark: BookmarkEntity) =
        bookmarkDao.insertBookmark(bookmark)

    override suspend fun removeBookmark(id: Int) = bookmarkDao.deleteBookmark(id)
}