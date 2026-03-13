package com.quick.app.core.model

import kotlinx.serialization.Serializable

/**
 * 资源
 * 将资源放到单独的对象中
 * 好处是后面还可扩展更多的字段
 * 例如：资源类型；资源大小；资源备注
 */
@Serializable
data class MediaResource(

    /**
     * 类型，0：图片；10：视频
     */
    val style: Int = 0,

    /**
     * 相对地址
     */
    val uri: String,
) {

}
