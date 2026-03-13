package com.quick.app.core.data.repository

import com.quick.app.core.database.dao.SongDao
import com.quick.app.core.database.model.SongEntity
import com.quick.app.core.network.datasource.MyNetworkDatasource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SongRepository @Inject constructor(
    private val songDao: SongDao,
    private val network: MyNetworkDatasource,
) {
    //region 数据库
    fun getAllPlayList(
    ): Flow<List<SongEntity>> = songDao.getAllPlayList(
    )

    suspend fun getAllPlayListAsync(
    ): List<SongEntity> = songDao.getAllPlayListAsync(
    )

    suspend fun getAllSourceLocal(): List<SongEntity> = songDao.getAllSourceLocal(
    )

    suspend fun insert(data: SongEntity) = songDao.insert(data)

    suspend fun insertAll(vararg data: SongEntity) = songDao.insertAll(*data)

    suspend fun insertList(entities: List<SongEntity>) = songDao.insertList(entities)

    suspend fun clearAllPlayList() = songDao.clearAllPlayList()

    suspend fun updateAllLocalMusicPlayList() = songDao.updateAllLocalMusicPlayList()

    suspend fun insertAll(entities: List<SongEntity>) = songDao.insertAll(entities)

    suspend fun delete(data: SongEntity) = songDao.delete(data)

    suspend fun deleteAll() = songDao.deleteAll()


    suspend fun deleteAllBySourceLocal() = songDao.deleteAllBySourceLocal()
    //endregion

    suspend fun songDetail(
        id: String,
    ) = network.songDetail(
        id = id,
    )

}