package com.quick.app.util

import android.content.Context

/**
 * Android尺寸相关工具栏
 */
object DensityUtil {
    /**
     * 根据手机的分辨率从 dip 的单位 转成为 px(像素)
     *
     * @param context
     * @param data
     * @return
     */
    fun dip2px(context: Context, data: Float): Float {
        //获取手机的缩放
        val scale = context.resources.displayMetrics.density

        //px=缩放*dp
        return (data * scale + 0.5f).toInt().toFloat()
    }
}
