package com.quick.app.core.data.repository

import com.quick.app.core.model.BaseId
import com.quick.app.core.model.Feed
import com.quick.app.core.model.response.NetworkPageData
import com.quick.app.core.model.response.NetworkResponse
import com.quick.app.core.network.datasource.MyNetworkDatasource
import com.quick.app.util.Constant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class FeedRepository @Inject constructor(
    private val network: MyNetworkDatasource,
) {
    fun feeds(
        page: Int = 1,
        app: Int = 0,
        query: String? = null,
    ): Flow<NetworkResponse<NetworkPageData<Feed>>> = flow {
        val p = mutableMapOf(
            "page" to page.toString(),
            "app" to app.toString(),
        ).apply {
            query?.let {
                put(Constant.QUERY, it)
            }
        }
        emit(
            network.feeds(p)
        )
    }.flowOn(Dispatchers.IO)

    fun createFeed(data: Feed): Flow<NetworkResponse<BaseId>> = flow {
        emit(
            network.createFeed(data)
        )
    }.flowOn(Dispatchers.IO)
}