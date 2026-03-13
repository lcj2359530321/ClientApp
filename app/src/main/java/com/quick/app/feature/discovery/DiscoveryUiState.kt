package com.quick.app.feature.discovery

import com.quick.app.core.exception.CommonException
import com.quick.app.core.model.ViewData

data class DiscoveryUiState(
    val topDatum: List<ViewData> = listOf(),

    /**
     * 刷新状态
     *
     * 0:空闲中
     *
     * 1：下拉刷新中
     * 2：下拉刷新失败
     * 3：下拉刷新成功
     */
    val refreshState: Int = 0,

    val exception: CommonException? = null,
) {
}