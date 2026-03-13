package com.quick.app.util

import org.joda.time.DateTime
import java.util.Calendar
import java.util.Date

object SuperDateUtil {
    /**
     * 当前年
     */
    fun currentYear(): Int {
        return Calendar.getInstance().get(Calendar.YEAR)
    }

    fun currentDayOfMonth(): Int {
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
    }

    /**
     * 将分秒毫秒数据转为毫秒
     *
     * @param data 格式为：00:06.429
     * @return
     */
    fun parseLyricTimeToInt(data: String): Long {
        //将:替换成.
        var data = data
        data = data.replace(":", ".")

        //使用.拆分
        val strings = data.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }
            .toTypedArray()

        //分别取出分秒毫秒
        val m = strings[0].toInt()
        val s = strings[1].toInt()
        val ms = strings[2].toInt()

        //转为毫秒
        return ((m * 60 + s) * 1000 + ms).toLong()
    }

    /**
     * 将ISO8601字符串转为项目中通用的格式
     * 几秒钟前
     * 几天前
     *
     * @param date
     * @return
     */
    fun commonFormat(date: String?): String {
        //将字符串转为DateTime
        val dateTime = DateTime(date)
        return commonFormat(dateTime)
    }

    /**
     * 将DateTime转为项目中通用的格式
     *
     * @param dateTime
     * @return
     */
    private fun commonFormat(dateTime: DateTime): String {
        //计算和现在时间的差
        //单位毫秒
        //java.util.Date
        val value = Date().time - dateTime.toDate().time
        if (value < 1L * ONE_MINUTE) {
            //小于1分钟

            //显示多少秒前
            val data = toSeconds(value)
            return String.format("%d秒前", if (data <= 0) 1 else data)
        } else if (value < 60 * ONE_MINUTE) {
            //小于1小时

            //显示多少分钟前
            val data = toMinutes(value)
            return String.format("%d分钟前", data)
        } else if (value < 24 * ONE_HOUR) {
            //小于1天

            //显示多少小时前
            val data = toHours(value)
            return String.format("%d小时前", data)
        } else if (value < 30 * ONE_DAY) {
            //小于1月

            //显示多少天前
            val data = toDays(value)
            return String.format("%d天前", data)
        }

        //其他时间
        //格式化为yyyyMMddHHmm
        return yyyyMMddHHmm(dateTime)
    }

    /**
     * 将时间戳转为项目中通用的格式
     *
     * @param data
     * @return
     */
    fun commonFormat(data: Long): String {
        //解析时间戳
        val dateTime = DateTime(data)
        return commonFormat(dateTime)
    }

    /**
     * 将DateTime转为yyyy-MM-dd HH:mm
     *
     * @param dateTime
     * @return
     */
    fun yyyyMMddHHmm(dateTime: DateTime): String {
        //格式化日期
        return dateTime.toString(yyyyMMddHHmm)
    }

    /**
     * 将ISO8601字符串转为yyyy-MM-dd HH:mm
     *
     * @param date
     * @return
     */
    fun yyyyMMddHHmm(date: String): String {
        //将字符串转为DateTime
        val dateTime = DateTime(date)
        return yyyyMMddHHmm(dateTime)
    }

    /**
     * 转为秒
     *
     * @param date
     * @return
     */
    private fun toSeconds(date: Long): Long {
        return date / 1000L
    }

    /**
     * 转为分钟
     *
     * @param date
     * @return
     */
    private fun toMinutes(date: Long): Long {
        return toSeconds(date) / 60L
    }

    /**
     * 转为小时
     *
     * @param date
     * @return
     */
    private fun toHours(date: Long): Long {
        return toMinutes(date) / 60L
    }

    /**
     * 转为天
     *
     * @param date
     * @return
     */
    private fun toDays(date: Long): Long {
        return toHours(date) / 24L
    }

    /**
     * 转为月
     *
     * @param date
     * @return
     */
    private fun toMonths(date: Long): Long {
        return toDays(date) / 30L
    }

    /**
     * 1分钟毫秒数
     */
    private const val ONE_MINUTE = 60000L

    /**
     * 1小时毫秒数
     */
    private const val ONE_HOUR = 3600000L

    /**
     * 1天毫秒数
     */
    private const val ONE_DAY = 86400000L

    private var yyyyMMddHHmm = "yyyy-MM-dd HH:mm"
    private var yyyyMMddHHmmss = "yyyy-MM-dd HH:mm:ss"
    var HHmm = "HH:mm"
    var yyyyMMdd = "yyyy-MM-dd"
    var ISO8601 = "yyyy-MM-ddTHH:mm:ss.SSSZ"

}