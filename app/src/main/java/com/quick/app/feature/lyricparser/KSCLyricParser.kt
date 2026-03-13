package com.quick.app.feature.lyricparser

import com.quick.app.util.LyricStringUtil
import com.quick.app.util.SuperDateUtil

/**
 * KSC歌词解析器
 */
object KSCLyricParser {
    /**
     * 解析歌词
     *
     * @param data
     * @return
     */
    fun parse(data: String): Lyric {
        return Lyric(
            accurate = true,
            datum = parseData(data)
        )
    }

    private fun parseData(data: String): List<LyricLine> {
        return data
            .split(";".toRegex())
            .dropWhile { it.isEmpty() }
            .mapNotNull { parseLine(it.trim()) }
    }

    /**
     * 解析每一行歌词
     *
     *
     * 例如中文：karaoke.add('00:27.487', '00:32.068', '一时失志不免怨叹', '347,373,1077,320,344,386,638,1096');
     * 英文：   karaoke.add('00:48.153', '00:49.234', '[I ][had ][a ][dream]', '185,200,191,500');
     */
    private fun parseLine(data: String): LyricLine? {
        if (data.contains("KSC制作：爱学啊")) {
            return null
        }

        return if (data.startsWith("karaoke.add")) {
            //过滤了前面的元数据

            //移除字符串前面的karaoke.add('
            //移除字符串后面的');
            //data=00:27.487', '00:32.068', '一时失志不免怨叹', '347,373,1077,320,344,386,638,1096
            val data = data.substring(13, data.length - 3)

            //使用', '拆分字符串
            val commands = data.split("', '".toRegex()).toTypedArray()

            //开始时间
            val startTime = SuperDateUtil.parseLyricTimeToInt(commands[0])

            //结束时间
            val endTime = SuperDateUtil.parseLyricTimeToInt(commands[1])

            //歌词
            val command = commands[2]

            var words: List<String>
            var word: String
            if (command[0] == '[') {
                //英文
                words = LyricStringUtil.englishWords(command)
                word = words.joinToString(" ")
            } else {
                //将歌词拆分为每一个字
                words = LyricStringUtil.words(command)
                word = command
            }

            //每一个字的时间列表
            val wordDurations = mutableListOf<Int>()
            val lyricTimeString = commands[3]

            //使用,拆分
            val lyricTimeWords =
                lyricTimeString.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()

            //将字符串时间转为int
            for (string in lyricTimeWords) {
                //转为int时方便后面的计算
                wordDurations.add(string.toInt())
            }

            //将每一个字的时间也转为数组
            //是因为数组相对于列表来说更快
            val results = IntArray(wordDurations.size)
            for (i in wordDurations.indices) {
                results[i] = wordDurations[i] as Int
            }

            //返回歌词行
            LyricLine(
                word,
                startTime,
                words,
                wordDurations,
                endTime
            )
        } else {
            null
        }
    }
}