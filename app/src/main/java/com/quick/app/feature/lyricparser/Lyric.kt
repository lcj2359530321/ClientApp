package com.quick.app.feature.lyricparser


/**
 * 解析后的歌词模型
 */
data class Lyric(
    /**
     * 所有的歌词
     */
    val datum: List<LyricLine>,
    /**
     * 是否是精确到字的歌词
     */
    val accurate: Boolean = false,

    val songId: String = "",
) {

    companion object {
        val EMPTY = Lyric(emptyList(), false)
    }
}