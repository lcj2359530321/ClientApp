package com.quick.app.core.model

import kotlinx.serialization.Serializable

@Serializable
data class Content(
    val id: String? = null,

    /**
     * 创建时间
     */
    val created: String? = null,

    /**
     * 更新时间
     */
    val updated: String? = null,

    val title: String? = null,
    val content: String? = null,
    val uri: String? = null,

    /**
     * 省
     * 主要用来在前段显示
     */
    val province: String? = null,

    /**
     * 省编码
     * 主要用来在后端计算，因为名称可能重复，但编码是唯一
     */
    val provinceCode: String? = null,

    /**
     * 市
     */
    val city: String? = null,

    /**
     * 市编码
     */
    val cityCode: String? = null,

    /**
     * 区
     */
    val area: String? = null,

    /**
     * 区编码
     */
    val areaCode: String? = null,

    /**
     * 位置名称，也就是地图sdk返回的poi名称，例如：山西大学，天府广场
     */
    val position: String? = null,

    /**
     * 详细地址，从路开始，例如：山西大学的详细地址是，坞城南路92号
     */
    val address: String? = null,

    /**
     * 经度
     */
    val longitude: Double? = null,

    /**
     * 纬度
     */
    val latitude: Double? = null,

    val user: User? = null,
    val style: Int = 0,
    val width: Int = 0,
    val height: Int = 0,
    val duration: Int = 0,

    val commentsCount: Long = 0,
    val collectsCount: Long = 0,
    val clicksCount: Long = 0,

    val likesCount: Long = 0,
    val medias: List<String>? = null,

    /**
     * 是否点赞
     * 有值表示点赞
     * null表示没点赞
     */
    val likeId: String? = null,
) {

    /**
     * 是否是竖屏视频
     *
     * @return
     */
    fun isPortraitVideo(): Boolean {
        return width < height
    }

    fun isLike(): Boolean {
        return likeId != null
    }
}