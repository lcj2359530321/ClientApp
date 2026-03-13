package com.quick.app.core.data.repository

import com.quick.app.core.model.Content
import com.quick.app.core.model.response.NetworkPageData
import com.quick.app.core.model.response.NetworkResponse
import com.quick.app.core.network.datasource.MyNetworkDatasource
import com.quick.app.util.Constant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

/**
 * 持有数据源抽象接口
 */
class ContentRepository @Inject constructor(
    private val network: MyNetworkDatasource,
) {
    fun contents(
        last: String? = null,
        categoryId: String? = null,
        userId: String? = null,
        size: Int = 10,
        style: Int? = Constant.VALUE10,
        current: String? = null,
    ): Flow<NetworkResponse<NetworkPageData<Content>>> = flow {
        emit(
            network.contents(last, categoryId, userId, size, style, current)
        )
    }.flowOn(Dispatchers.IO)

}