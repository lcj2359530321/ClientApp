package com.quick.app.core.data.repository

import com.quick.app.core.model.Ad
import com.quick.app.core.model.response.NetworkPageData
import com.quick.app.core.model.response.NetworkResponse
import com.quick.app.core.network.datasource.MyNetworkDatasource
import com.quick.app.util.Constant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class AdRepository @Inject constructor(
    private val network: MyNetworkDatasource,
) {
    fun ads(
        position: Int = 10,
        style: Int? = null,
        app: Int = Constant.VALUE0
    ): Flow<NetworkResponse<NetworkPageData<Ad>>> = flow {
        emit(
            network.ads(position, style, app)
        )
    }.flowOn(Dispatchers.IO)
}