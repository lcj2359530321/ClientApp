package com.quick.app.feature.lyric

import android.graphics.Color
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.sp
import com.quick.app.feature.lyricparser.LyricLine
import com.quick.app.util.TextUtil
import kotlin.math.abs

val backgroundTextPaint = Paint().asFrameworkPaint().apply {
    isDither = true // 防抖动
    isAntiAlias = true // 抗锯齿
    color = android.graphics.Color.LTGRAY
}

val foregroundTextPaint = Paint().asFrameworkPaint().apply {
    isDither = true // 防抖动
    isAntiAlias = true // 抗锯齿
    color = Color.parseColor("#ff3a3a")
}

@Composable
fun LyricLineView(
    data: LyricLine,
    selected: Boolean,
    accurate: Boolean,
    lyricCurrentWordIndex: Int,
    wordPlayedTime: Long,
    modifier: Modifier = Modifier
) {
    //设置画笔字体大小
    backgroundTextPaint.textSize = with(LocalDensity.current) {
        16.sp.toPx()
    }
    foregroundTextPaint.textSize = with(LocalDensity.current) {
        16.sp.toPx()
    }

    Canvas(modifier = modifier) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        //获取歌词的宽高
        val textWidth = TextUtil.getTextWidth(backgroundTextPaint, data.data)
        val textHeight = TextUtil.getTextHeight(backgroundTextPaint)

        //水平居中坐标
        val centerX: Float = (canvasWidth - textWidth) / 2

        //垂直居中坐标
        val fontMetrics = backgroundTextPaint.fontMetrics
        val centerY: Float = (canvasHeight - textHeight) / 2 + abs(fontMetrics.top)

        //绘制背景文字
        drawContext.canvas.nativeCanvas.drawText(data.data, centerX, centerY, backgroundTextPaint)

        if (selected) {
            //选中
            if (accurate) {
                //精确到字
                var lineLyricPlayedWidth: Float = 0f

                if (lyricCurrentWordIndex == -1) {
                    //该行已经播放完了
                    lineLyricPlayedWidth = textWidth
                } else {
                    //歌词所有字
                    val lyricWords = data.words

                    //歌词所有字对应时间
                    val wordDurations = data.wordDurations

                    //获取当前时间前面的文字
                    val beforeText = getBeforeText(data, lyricCurrentWordIndex)
                    val beforeTextWidth = TextUtil.getTextWidth(foregroundTextPaint, beforeText)

                    //当前字
                    val currentWord = lyricWords[lyricCurrentWordIndex]

                    //获取当前字宽度
                    val currentWordTextWidth =
                        TextUtil.getTextWidth(foregroundTextPaint, currentWord)

                    //当前字已经演唱的宽度
                    val currentWordPlayedWidth: Float =
                        currentWordTextWidth / wordDurations[lyricCurrentWordIndex] * wordPlayedTime

                    //这一行已经演唱的宽度
                    lineLyricPlayedWidth = beforeTextWidth + currentWordPlayedWidth

                    //裁剪矩形
                    //用来绘制已经唱的歌词
                    drawContext.canvas.nativeCanvas.clipRect(
                        centerX,
                        0F,
                        centerX + lineLyricPlayedWidth,
                        canvasHeight
                    )
                }


            }

            //绘制高亮
            drawContext.canvas.nativeCanvas.drawText(
                data.data,
                centerX,
                centerY,
                foregroundTextPaint
            )
        }
    }
}

/**
 * 获取该索引前面字符串
 *
 * @param data 当前歌词行数据
 * @param index 当前播放到第几个字
 * @return 前面已经播放过的字符串
 */
private fun getBeforeText(data: LyricLine, index: Int): String {
    // take(index) 会截取前 index 个元素
    // joinToString("") 会将它们无缝拼接成一个完整的字符串
    return data.words.take(index).joinToString("")
}