package alireza.nezami.data.repository

import alireza.nezami.database.dao.BookmarkDao
import alireza.nezami.domain.repository.BookmarkRepository
import alireza.nezami.model.entity.BookmarkEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class BookmarkRepositoryImpl @Inject constructor(
        private val bookmarkDao: BookmarkDao
) : BookmarkRepository {
    override suspend fun getAllBookmarks(): Flow<List<BookmarkEntity>> = bookmarkDao.getAllBookmarks()

    override suspend fun addBookmark(bookmark: BookmarkEntity): Flow<Unit> = flow {
        bookmarkDao.insertBookmark(bookmark)
        emit(Unit)
    }

    override suspend fun removeBookmark(id: Int): Flow<Unit> = flow {
        bookmarkDao.deleteBookmark(id)
        emit(Unit)
    }
}