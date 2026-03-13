package com.quick.app.core.model

import com.google.common.base.Strings
import kotlinx.serialization.Serializable

/**
 * 评论模型
 */
@Serializable
data class Comment(
    /**
     * 评论内容
     */
    val content: String? = null,

    /**
     * 点赞数
     */
    val likesCount: Long = 0,
    val commentsCount: Long = 0,

    /**
     * 是否点赞
     * 有值表示点赞
     * null表示没点赞
     */
    val likeId: String? = null,

    /**
     * 被回复的评论
     */
    val parent: Comment? = null,

    /**
     * 被回复评论的id
     * 只有发布评论时候才可能有值
     */
    val parentId: String? = null,

    /**
     * 歌单id
     * 只有发布歌单评论时才有值
     */
    val sheetId: String? = null,

    /**
     * 动态id
     * 只有发布动态评论时才有值
     */
    val feedId: String? = null,

    val orderId: String? = null,
    val productId: String? = null,

    /**
     * 评论发布人
     */
    val user: User? = null,

    val score: Float? = null,

    /**
     * 图片列表
     */
    val medias: List<MediaResource>? = null,

//    val stock: Stock? = null


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
    /**
     * 是否点赞了
     *
     * @return
     */
    val isLiked: Boolean
        get() = !Strings.isNullOrEmpty(likeId)
}
