package com.quick.app.core.model

import kotlinx.serialization.Serializable

/**
 * 微信登录请求
 *
 * https://developers.weixin.qq.com/doc/oplatform/Mobile_App/WeChat_Login/Development_Guide.html
 */
@Serializable
data class WechatLoginRequest(
    val code: String,
    val state: String,
    val lang: String,
    val country: String,
) {
}