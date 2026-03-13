package com.quick.app.core.model

import kotlinx.serialization.Serializable

/**
 * 动态模型
 */
@Serializable
data class Feed(
    /**
     * 动态内容
     */
    val content: String? = null,

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

    /**
     * 用户id
     */
    val userId: String? = null,

    /**
     * 用户
     */
    val user: User? = null,
    val feedId: String? = null,
    val commentId: String? = null,

    /**
     * 图片列表
     */
    val medias: List<MediaResource>? = null,

    /**
     * 点赞的用户
     */
    val likes: List<User>? = null,

    /**
     * 评论
     */
    val comments: List<Comment>? = null,

    val id: String? = null,

    /**
     * 创建时间
     */
    val created: String? = null,

    /**
     * 更新时间
     */
    val updated: String? = null,
) {

}
