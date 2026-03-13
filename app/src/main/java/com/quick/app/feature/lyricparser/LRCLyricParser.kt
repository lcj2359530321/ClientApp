package com.quick.app.feature.lyricparser

import com.quick.app.util.SuperDateUtil

/**
 * LRC歌词解析器
 */
object LRCLyricParser {
    /**
     * 解析歌词
     *
     * @param data
     * @return
     */
    fun parse(data: String): Lyric {
        val lyricLines = parseData(data)

        //每个结束时间赋值到上一个开始时间
        for (i in 0 until lyricLines.size - 1) {
            lyricLines[i].endTime = lyricLines[i + 1].startTime
        }

        //最后一个
        lyricLines[lyricLines.size - 1].endTime = Long.MAX_VALUE

        return Lyric(
            datum = lyricLines
        )
    }

    /**
     * 解析一行歌词
     * 例如：[00:00.300]爱的代价 - 李宗盛
     *
     * @param data
     * @return
     */
    private fun parseLine(data: String): LyricLine? {
        if (data.contains("LRC制作：爱学啊")) {
            return null
        }

        return if (data.startsWith("[0")) {
            //去除前面的[
            val lineData = data.substring(1) //00:00.300]爱的代价 - 李宗盛

            //使用]拆分
            val commands = lineData.split("]".toRegex()).toTypedArray() //00:00.300,爱的代价 - 李宗盛

            // 安全校验：防止某些行只有时间没有歌词导致数组越界
            if (commands.size < 2) {
                return null
            }

            //返回解析后的歌词行
            return LyricLine(
                startTime = SuperDateUtil.parseLyricTimeToInt(commands[0]),
                data = commands[1],
            )
        } else {
            //过滤元数据
            null
        }
    }

    private fun parseData(data: String): List<LyricLine> {
        return data.split("\n".toRegex()) //使用\n拆分歌词
            .dropLastWhile { it.isEmpty() }
            .mapNotNull { parseLine(it) }
    }
}