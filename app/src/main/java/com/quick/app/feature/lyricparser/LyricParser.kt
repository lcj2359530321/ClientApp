package com.quick.app.feature.lyricparser

import com.quick.app.util.Constant.KSC


/**
 * 歌词解析器
 */
object LyricParser {
    /**
     * 解析歌词
     *
     * @param type    歌词类型
     * @param content 歌词内容
     * @return 解析后的歌词对象
     */
    fun parse(type: Int, content: String): Lyric {
        return when (type) {
            KSC -> KSCLyricParser.parse(content)
            else ->                 //默认解析LRC歌词
                LRCLyricParser.parse(content)
        }
    }
}