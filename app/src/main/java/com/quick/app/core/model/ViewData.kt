package com.quick.app.core.model

import com.quick.app.util.Constant
import kotlinx.serialization.Serializable

/**
 * 页面每个item数据
 *
 * 主要是实现服务端可以配置页面数据，以及顺序
 */
@Serializable
data class ViewData(
    /**
     * 类型
     *
     * 不同的类型，不同的字段有值
     *
     * 10：ads
     * 20：buttons
     * 30:larges
     * 40:hots
     */
    val style: Int = Constant.VALUE10,

    val ads: List<Ad>? = null,
    val buttons: List<ButtonViewData>? = null,
//    val larges: List<CropData>? = null,
//    val hots: List<Product>? = null,
    val sheets: List<Sheet>? = null,
    val songs: List<Song>? = null,
)