package com.quick.app.util

import android.graphics.Paint
import kotlin.math.ceil

object TextUtil {
    /**
     * 获取文本的宽度
     *
     * @param paint
     * @param data
     * @return
     */
    fun getTextWidth(paint: Paint, data: String?): Float {
        return paint.measureText(data)
    }

    /**
     * 获取文本的高度
     *
     * @param paint
     * @return
     */
    fun getTextHeight(paint: Paint): Float {
        val fontMetrics = paint.getFontMetrics()
        return ceil((fontMetrics.descent - fontMetrics.ascent).toDouble()).toFloat()
    }
}
