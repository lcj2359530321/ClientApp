package com.quick.app.util

/**
 * url工具类
 */
object SuperUrlUtil {
    /**
     * 获取网址里面的查询参数
     *
     * @param data ?a=b&c=d
     * @return
     */
    fun getQueryMap(data: String): Map<String, Any?> {
        var data = data
        val map: MutableMap<String, Any> = HashMap()
        data = data.replace("?", ";")
        if (!data.contains(";")) {
            return map
        }
        return if (data.split(";".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray().size > 0) {
            val arr = data.split(";".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()[1].split("&".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
            for (s in arr) {
                val key = s.split("=".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
                val value = s.split("=".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
                map[key] = value
            }
            map
        } else {
            map
        }
    }

    /**
     * 是否是url
     */
    fun isUrl(data: String): Boolean {
        return data.startsWith("http://") || data.startsWith("https://")
    }
}