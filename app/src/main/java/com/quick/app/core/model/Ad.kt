package com.quick.app.core.model

import kotlinx.serialization.Serializable

@Serializable
data class Ad(
    /**
     * 标题
     */
    val title: String? = null,

    /**
     * 图片
     */
    val icon: String,

    /**
     * 点击广告后跳转的地址
     */
    val uri: String? = null,

    /**
     * 类型，0：图片；10：视频；20：应用
     */
    val style: Int = 0,
)