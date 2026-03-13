package com.quick.app.feature.lyricparser

/**
 * 一行歌词
 */
data class LyricLine(
    /**
     * 整行歌词
     */
    val data: String,

    /**
     * 开始时间
     * 单位毫秒
     */
    val startTime: Long = 0,

    /**
     * 每一个字
     */
    val words: List<String> = emptyList(),

    /**
     * 每一个字对应的时间
     */
    val wordDurations: List<Int> = emptyList(),

    /**
     * 结束时间
     */
    var endTime: Long = 0,
) {

}