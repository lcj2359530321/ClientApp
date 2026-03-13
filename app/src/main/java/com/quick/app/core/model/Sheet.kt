package com.quick.app.core.model

import kotlinx.serialization.Serializable

@Serializable
data class Sheet(
    val id: String = "",
    val title: String = "",
    val created: String = "",
    val updated: String = "",
    val user: User? = null,
    val icon: String? = null,
    val detail: String? = null,
    val songs: List<Song>? = null,
    val clicksCount: Long = 0,
    val collectsCount: Long = 0,
    val commentsCount: Long = 0,
    val songsCount: Long = 0,
    val collectId: String = "",
) {
    val isCollected: Boolean
        get() = collectId.isNotBlank()
}

fun SHEET_EMPTY(): Sheet {
    return Sheet(
        id = "",
        created = "",
        updated = "",
        title = "",
    )
}

val SHEET = Sheet(
    id = "1",
    title = "这世上所有的歌zheshishangtestios开发",
    icon = "assets/list1.jpg",
    detail = "这是因为iOS9引入了新特性App Transport Security (ATS)，他要求App内网络请求必须使用HTTPS协议。解决方法是要么改为HTTPS，要么声明可以使用HTTP，可以声明部分使用HTTP，也可以所有；但需要说明的是如果APP内所有请求都是HTTP，那么如果要上架App Store的时候基本都会被拒。",
    clicksCount = 17007,
    collectsCount = 24,
    commentsCount = 44,
    songsCount = 0,
)