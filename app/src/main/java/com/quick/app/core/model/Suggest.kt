package com.quick.app.core.model

import kotlinx.serialization.Serializable

/**
 * 搜索建议模型
 */
@Serializable
data class Suggest(
    /**
     * 文章搜索建议
     */
    val contents: List<SuggestItem>? = null,

    /**
     * 用户搜索建议
     */
    val users: List<SuggestItem>? = null,

    /**
     * 商品搜索建议
     */
    val products: List<SuggestItem>? = null,

    /**
     * 歌单搜索建议
     */
    val sheets: List<SuggestItem>? = null,
) {

}