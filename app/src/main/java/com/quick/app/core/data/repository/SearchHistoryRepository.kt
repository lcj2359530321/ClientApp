package com.quick.app.core.data.repository

import com.quick.app.core.database.dao.SearchHistoryDao
import com.quick.app.core.database.model.SearchHistoryEntity
import com.quick.app.util.Constant
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class SearchHistoryRepository @Inject constructor(
    private val searchHistoryDao: SearchHistoryDao,
) {
    fun getAll(
        app: Int = Constant.VALUE0,
    ): Flow<List<SearchHistoryEntity>> = searchHistoryDao.getAllFlow(
        app = app,
    )

    suspend fun insert(data: SearchHistoryEntity) = searchHistoryDao.insert(data)

    suspend fun delete(data: SearchHistoryEntity) = searchHistoryDao.delete(data)

    suspend fun deleteAll() = searchHistoryDao.deleteAll()
}