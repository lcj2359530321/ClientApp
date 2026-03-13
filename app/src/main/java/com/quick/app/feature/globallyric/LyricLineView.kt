package com.quick.app.feature.globallyric

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.quick.app.R
import com.quick.app.feature.lyricparser.LyricLine
import com.quick.app.util.DensityUtil
import com.quick.app.util.TextUtil
import kotlin.math.abs

/**
 * 一行歌词控件
 *
 *
 * TODO: 歌词超出一行，滚动显示
 */
class LyricLineView : View {
    /**
     * 当前歌词行
     */
    private var data: LyricLine? = null

    /**
     * 默认歌词画笔
     */
    private lateinit var backgroundTextPaint: Paint

    /**
     * 测试画笔
     */
    private var testPaint: Paint? = null

    /**
     * 歌词颜色
     */
    private var lyricTextColor = 0

    /**
     * 文字大小
     */
    private var lyricTextSize = 0

    /**
     * 是否选中
     */
    private var lineSelected = false

    /**
     * 高亮画笔
     */
    private lateinit var foregroundTextPaint: Paint

    /**
     * 高亮歌词颜色
     */
    private var lyricSelectedTextColor = 0

    /**
     * 是否是精确到字歌词
     */
    private var accurate = false

    /**
     * 当前播放时间点，在该行的第几个字
     */
    private var lyricCurrentWordIndex = 0

    /**
     * 当前行歌词已经唱过的宽度，也就是歌词高亮的宽度
     */
    private var lineLyricPlayedWidth = 0f

    /**
     * 当前字，已经播放的时间
     */
    private var wordPlayedTime = 0L
    private val stringBuilder = StringBuilder()

    /**
     * 歌词位置
     */
    private var lyricGravity = 0

    constructor(context: Context?) : super(context) {
        init(null)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(attrs)
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        lyricTextColor = DEFAULT_LYRIC_TEXT_COLOR
        lyricTextSize = DensityUtil.dip2px(context, DEFAULT_LYRIC_TEXT_SIZE).toInt()
        lyricSelectedTextColor = DEFAULT_LYRIC_SELECTED_TEXT_COLOR

        //解析自定义属性
        if (attrs != null) {
            //获取属性值
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.LyricLineView)

            //这个属性名为
            //declare-styleable name名称+属性名
            //获取歌词大小
            lyricTextSize = typedArray.getDimension(
                R.styleable.LyricLineView_text_size,
                lyricTextSize.toFloat()
            ).toInt()

            //歌词默认颜色
            lyricTextColor =
                typedArray.getColor(R.styleable.LyricLineView_text_color, lyricTextColor)

            //歌词高亮颜色
            lyricSelectedTextColor = typedArray.getColor(
                R.styleable.LyricLineView_selected_text_color,
                lyricSelectedTextColor
            )

            //歌词位置
            lyricGravity = typedArray.getInt(R.styleable.LyricLineView_gravity, GRAVITY_CENTER)
        }

        //初始化画笔

        //默认歌词画笔
        //以下内容都是Java/Android绘图API知识
        backgroundTextPaint = Paint()

        //设置图像防抖动
        backgroundTextPaint!!.isDither = true

        //设置抗锯齿
        backgroundTextPaint!!.isAntiAlias = true

        //设置文本颜色
        backgroundTextPaint!!.setColor(lyricTextColor)

        //高亮画笔
        foregroundTextPaint = Paint()
        foregroundTextPaint!!.isDither = true
        foregroundTextPaint!!.isAntiAlias = true
        updateTextColor()
        updateTextSize()

        //测试画笔
        //用了画矩形
        //目的方便调试
        testPaint = Paint()
        testPaint!!.isDither = true
        testPaint!!.isAntiAlias = true
        testPaint!!.setColor(Color.GREEN)

        //只绘制边框不填充
        testPaint!!.style = Paint.Style.STROKE
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //保存状态
        canvas.save()
        if (isEmptyLyric) {
            //如果没有歌词

            //就绘制默认提示文本
            //drawDefaultText(canvas);
        } else {
            //绘制真实歌词
            drawLyricText(canvas)
        }

        //恢复状态
        canvas.restore()
    }

    /**
     * 绘制真实歌词
     *
     * @param canvas
     */
    private fun drawLyricText(canvas: Canvas) {
//        Timber.d("drawLyricText %s", data!!.data)

        //获取歌词的宽高
        val textWidth: Float = TextUtil.getTextWidth(backgroundTextPaint, data!!.data)
        val textHeight: Float = TextUtil.getTextHeight(backgroundTextPaint)

        //水平居中坐标
        val centerX = getCenterX(textWidth)

        //垂直居中坐标
        val fontMetrics = backgroundTextPaint!!.getFontMetrics()
        val centerY =
            ((measuredHeight - textHeight) / 2 + abs(fontMetrics.top.toDouble())).toFloat()

        //绘制背景文字
        canvas.drawText(data!!.data, centerX, centerY, backgroundTextPaint!!)
        if (lineSelected) {
            //选中
            if (accurate) {
                //精确到字
                lineLyricPlayedWidth = if (lyricCurrentWordIndex == -1) {
                    //该行已经播放完了
                    textWidth
                } else {
                    //歌词所有字
                    val lyricWords = data!!.words

                    //歌词所有字对应时间
                    val wordDurations = data!!.wordDurations

                    //获取当前时间前面的文字
                    val beforeText = getBeforeText(data!!, lyricCurrentWordIndex)
                    val beforeTextWidth: Float =
                        TextUtil.getTextWidth(foregroundTextPaint, beforeText)

                    //当前字
                    val currentWord = lyricWords[lyricCurrentWordIndex]

                    //获取当前字宽度
                    val currentWordTextWidth: Float =
                        TextUtil.getTextWidth(foregroundTextPaint, currentWord)

                    //当前字已经演唱的宽度
                    val currentWordPlayedWidth =
                        currentWordTextWidth / wordDurations[lyricCurrentWordIndex] * wordPlayedTime

                    //这一行已经演唱的宽度
                    beforeTextWidth + currentWordPlayedWidth
                }
//                Timber.d("onDraw lineLyricPlayedWidth %f", lineLyricPlayedWidth)

                //绘制矩形宽高
//                canvas.drawRect(centerX,0,centerX+lineLyricPlayedWidth,getMeasuredHeight(),testPaint);

                //裁剪矩形
                //用来绘制已经唱的歌词
                canvas.clipRect(
                    centerX,
                    0f,
                    centerX + lineLyricPlayedWidth,
                    measuredHeight.toFloat()
                )
            }

            //绘制高亮
            canvas.drawText(data!!.data, centerX, centerY, foregroundTextPaint!!)
        }
    }

    /**
     * 获取该索引前面字符串
     *
     * @param data
     * @param index
     * @return
     */
    private fun getBeforeText(data: LyricLine, index: Int): String {
        stringBuilder.setLength(0)
        for (i in 0 until index) {
            stringBuilder.append(data.words[i])
        }
        return stringBuilder.toString()
    }

    /**
     * 获取歌词在水平方向上的中心点
     *
     * @param textWidth
     * @return
     */
    private fun getCenterX(textWidth: Float): Float {
        return when (lyricGravity) {
            GRAVITY_LEFT -> 0F
            else -> (measuredWidth - textWidth) / 2
        }
    }

    private val isEmptyLyric: Boolean
        private get() = data == null

    fun setData(data: LyricLine?) {
        this.data = data
        invalidate()
    }

    fun setLyricTextColor(lyricTextColor: Int) {
        this.lyricTextColor = lyricTextColor
        backgroundTextPaint!!.setColor(lyricTextColor)
        invalidate()
    }

    fun setLineSelected(lineSelected: Boolean) {
        this.lineSelected = lineSelected
    }

    fun setAccurate(accurate: Boolean) {
        this.accurate = accurate
    }

    /**
     * 歌词进度
     */
    fun onProgress() {
        if (!isEmptyLyric) {
            //有歌词就刷新控件
            invalidate()
        }
    }

    /**
     * 设置当前字索引
     *
     * @param lyricCurrentWordIndex
     */
    fun setLyricCurrentWordIndex(lyricCurrentWordIndex: Int) {
        this.lyricCurrentWordIndex = lyricCurrentWordIndex
    }

    /**
     * 设置当前字已经播放的时间
     *
     * @param wordPlayedTime
     */
    fun setWordPlayedTime(wordPlayedTime: Long) {
        this.wordPlayedTime = wordPlayedTime
    }

    /**
     * 设置歌词高亮颜色
     *
     * @param lyricSelectedTextColor
     */
    fun setLyricSelectedTextColor(lyricSelectedTextColor: Int) {
        this.lyricSelectedTextColor = lyricSelectedTextColor

        //更新文本颜色
        updateTextColor()
    }

    /**
     * 重新信息歌词高亮颜色
     */
    private fun updateTextColor() {
        foregroundTextPaint!!.setColor(lyricSelectedTextColor)

        //刷新一次
        invalidate()
    }

    /**
     * 减小字体大小
     *
     * @return
     */
    fun decrementTextSize(): Int {
        lyricTextSize--
        updateTextSize()
        return lyricTextSize
    }

    /**
     * 增大字体大小
     *
     * @return
     */
    fun incrementTextSize(): Int {
        lyricTextSize++
        updateTextSize()
        return lyricTextSize
    }

    /**
     * 重新设置字体大小
     */
    private fun updateTextSize() {
        backgroundTextPaint!!.textSize = lyricTextSize.toFloat()
        foregroundTextPaint!!.textSize = lyricTextSize.toFloat()

        //刷新一次
        invalidate()
    }

    /**
     * 设置歌词字体大小
     *
     * @param lyricTextSize
     */
    fun setLyricTextSize(lyricTextSize: Int) {
        this.lyricTextSize = lyricTextSize

        //更新文本大小
        updateTextSize()
    }

    companion object {
        /**
         * 默认歌词字大小
         * 单位：dp
         */
        public const val DEFAULT_LYRIC_TEXT_SIZE = 16f

        /**
         * 默认歌词颜色
         */
        private const val DEFAULT_LYRIC_TEXT_COLOR = Color.WHITE

        /**
         * 默认歌词高亮颜色
         */
        private val DEFAULT_LYRIC_SELECTED_TEXT_COLOR = Color.parseColor("#d6271c")

        /**
         * 歌词位置 水平居左，垂直居中
         */
        private const val GRAVITY_LEFT = 0x01

        /**
         * 歌词位置 垂直水平居中
         */
        private const val GRAVITY_CENTER = 0x10
    }
}
