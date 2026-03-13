package com.quick.app.core.model

import kotlinx.serialization.Serializable


/**
 * 搜索建议项
 */
@Serializable
data class SuggestItem(
    val id: String = "",
    /**
     * 标题
     */
    var title: String = "",
) {

}