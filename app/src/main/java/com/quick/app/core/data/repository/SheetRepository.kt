package com.quick.app.core.data.repository

import com.quick.app.core.model.BaseId
import com.quick.app.core.model.Sheet
import com.quick.app.core.model.response.NetworkResponse
import com.quick.app.core.network.datasource.MyNetworkDatasource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

/**
 * 歌单仓库
 */
class SheetRepository @Inject constructor(
    private val networkDataSource: MyNetworkDatasource,
) {
    suspend fun createSheet(
        data: Sheet,
    ): Flow<NetworkResponse<Sheet>> = flow {
        emit(
            networkDataSource.createSheet(data)
        )
    }.flowOn(Dispatchers.IO)

    suspend fun updateSheet(
        data: Sheet,
    ): Flow<NetworkResponse<Sheet>> = flow {
        emit(
            networkDataSource.updateSheet(data)
        )
    }.flowOn(Dispatchers.IO)

    suspend fun createSheets(
        userId: String,
    ) = networkDataSource.createSheets(userId)

    suspend fun collectSheets(
        userId: String,
    ) = networkDataSource.collectSheets(userId)

    fun sheetDetail(
        id: String,
    ): Flow<NetworkResponse<Sheet>> = flow {
        emit(
            networkDataSource.sheetDetail(id)
        )
    }.flowOn(Dispatchers.IO)

    fun collectSheet(data: BaseId) = flow {
        emit(
            networkDataSource.collectSheet(data)
        )
    }.flowOn(Dispatchers.IO)

    fun cancelCollectSheet(data: BaseId) = flow {
        emit(
            networkDataSource.cancelCollectSheet(data)
        )
    }.flowOn(Dispatchers.IO)
}