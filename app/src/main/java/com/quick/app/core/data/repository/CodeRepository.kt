package com.quick.app.core.data.repository

import com.quick.app.core.model.BaseModel
import com.quick.app.core.model.CodeRequest
import com.quick.app.core.model.response.NetworkResponse
import com.quick.app.core.network.datasource.MyNetworkDatasource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class CodeRepository @Inject constructor(
    private val network: MyNetworkDatasource,
) {
    fun sendCode(data: CodeRequest): Flow<NetworkResponse<BaseModel>> = flow {
        emit(
            network.sendCode(data)
        )
    }.flowOn(Dispatchers.IO)

    fun checkCode(data: CodeRequest): Flow<NetworkResponse<BaseModel>> = flow {
        emit(
            network.checkCode(data)
        )
    }.flowOn(Dispatchers.IO)
}