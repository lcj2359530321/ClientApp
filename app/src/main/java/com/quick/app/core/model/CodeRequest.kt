package com.quick.app.core.model

import kotlinx.serialization.Serializable

/**
 * 验证码请求参数
 *
 *
 * 可以复用User模型
 */
@Serializable
data class CodeRequest(
    val phone: String? = null,
    val email: String? = null,

    /**
     * 如果发送频繁了，也可以发送验证码时，传递图形验证码
     */
    val code: String? = null,
) {

}