package com.quick.app.core.data.repository

import com.quick.app.core.network.datasource.MyNetworkDatasource
import com.quick.app.util.Constant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class CommonRepository @Inject constructor(
    private val networkDatasource: MyNetworkDatasource,
) {
    suspend fun indexes(
        app: Int = Constant.VALUE30
    ) = networkDatasource.indexes(
        app = app,
    )

    suspend fun uploadFile(
        file: MultipartBody.Part,
        flavor: RequestBody,
        relative: RequestBody
    ) = flow {
        emit(
            networkDatasource.uploadFile(file, flavor, relative)
        )
    }.flowOn(Dispatchers.IO)


    suspend fun uploadFileSuspend(
        file: MultipartBody.Part,
        flavor: RequestBody,
        relative: RequestBody
    ) = networkDatasource.uploadFile(file, flavor, relative)

    suspend fun uploadFiles(
        files: List<MultipartBody.Part>,
        flavor: RequestBody,
        relative: RequestBody
    ) = flow {
        emit(
            networkDatasource.uploadFiles(files, flavor, relative)
        )
    }.flowOn(Dispatchers.IO)

    fun searchSheets(data: String) = flow {
        emit(
            networkDatasource.searchSheets(data)
        )
    }.flowOn(Dispatchers.IO)

    fun searchUsers(data: String) = flow {
        emit(
            networkDatasource.searchUsers(data)
        )
    }.flowOn(Dispatchers.IO)

    fun searchSuggest(data: String) = flow {
        emit(
            networkDatasource.searchSuggest(data)
        )
    }.flowOn(Dispatchers.IO)
}