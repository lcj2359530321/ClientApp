package com.quick.app.core.network.di

import com.quick.app.core.network.datasource.MyNetworkDatasource
import com.quick.app.core.network.datasource.MyRetrofitDatasource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * 渠道网络模块
 *
 * 例如：dev环境有很多本地测试数据
 * prod连接线上API
 *
 * 直接放到main目录
 * 参考：https://github.com/search?q=repo%3Aandroid%2Fnowinandroid%20FlavoredNetworkModule&type=code
 */
@Module
@InstallIn(SingletonComponent::class)
interface FlavoredNetworkModule {
    @Binds
    fun binds(impl: MyRetrofitDatasource): MyNetworkDatasource
}