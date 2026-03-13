package com.quick.app.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.quick.app.core.database.model.SearchHistoryEntity
import kotlinx.coroutines.flow.Flow

/**
 * 搜索历史dao
 * https://developer.android.com/training/data-storage/room/accessing-data?hl=zh-cn#convenience-insert
 */
@Dao
interface SearchHistoryDao {
    @Query("SELECT * FROM search_history where app = :app order by created desc")
    suspend fun getAll(app: Int): List<SearchHistoryEntity>

    @Query("SELECT * FROM search_history where app = :app order by created desc")
    fun getAllFlow(app: Int = 0): Flow<List<SearchHistoryEntity>>

    @Query("SELECT * FROM search_history WHERE title = :title")
    fun findByTitle(title: String): SearchHistoryEntity

    @Query("SELECT * FROM search_history WHERE title = :title")
    fun findByTitleFlow(title: String): Flow<SearchHistoryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg data: SearchHistoryEntity)

    @Upsert
    suspend fun insertAll(entities: List<SearchHistoryEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: SearchHistoryEntity)

    @Update
    fun update(data: SearchHistoryEntity)

    @Update
    fun updateAll(vararg data: SearchHistoryEntity)

    @Delete
    suspend fun delete(data: SearchHistoryEntity)

    @Query("DELETE FROM search_history")
    suspend fun deleteAll()
}