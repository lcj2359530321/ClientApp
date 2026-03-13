package com.quick.app.core.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.quick.app.core.database.model.SongEntity
import kotlinx.coroutines.flow.Flow

/**
 * 音乐dao
 *
 * https://developer.android.com/training/data-storage/room/accessing-data?hl=zh-cn#convenience-insert
 */
@Dao
interface SongDao {
    @Query("SELECT * FROM song order by track_number asc")
    suspend fun getAllAsync(): List<SongEntity>

    @Query("SELECT * FROM song where play_list = true order by track_number asc")
    suspend fun getAllPlayListAsync(): List<SongEntity>

    @Query("SELECT * FROM song order by track_number asc")
    fun getAll(): Flow<List<SongEntity>>

    @Query("SELECT * FROM song where play_list = true order by track_number asc")
    fun getAllPlayList(): Flow<List<SongEntity>>

    @Query("SELECT * FROM song where source = '10' order by track_number asc")
    suspend fun getAllSourceLocal(): List<SongEntity>

    @Query("SELECT * FROM song WHERE title = :title")
    fun findByTitle(title: String): SongEntity

    @Query("SELECT * FROM song WHERE title = :title")
    fun findByTitleFlow(title: String): Flow<SongEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg data: SongEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(entities: List<SongEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: List<SongEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: SongEntity)

    @Update
    fun update(data: SongEntity)

    @Update
    fun updateAll(vararg data: SongEntity)

    @Query("UPDATE song SET play_list = false")
    suspend fun clearAllPlayList()

    @Query("UPDATE song SET play_list = true where source = '10'")
    suspend fun updateAllLocalMusicPlayList()

    @Delete
    suspend fun delete(data: SongEntity)

    @Query("DELETE FROM song")
    suspend fun deleteAll()

    @Query("DELETE FROM song where source = '10'")
    suspend fun deleteAllBySourceLocal()
}