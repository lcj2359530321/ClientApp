package com.quick.app.core.exception

import com.quick.app.core.model.response.NetworkResponse

/**
 * 全局通用异常
 */
class CommonException(
    /**
     * 网络响应
     */
    val networkResponse: NetworkResponse<*>? = null,

    val throwable: Throwable? = null,

    var tipString: String? = null,
    var tipIcon: Int? = null,
) : RuntimeException()