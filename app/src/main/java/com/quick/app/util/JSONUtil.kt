package com.quick.app.util

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


/**
 * JSON工具类
 */
object JSONUtil {

    /**
     * 转为json字符串
     */
    inline fun <reified T> toJSON(data: T): String {
        return Json.encodeToString(data)
    }

    /**
     * 转为对象
     *
     * @param data
     * @param <T>
     * @return
    </T> */
    inline fun <reified T> fromJSON(data: String): T {
        return Json.decodeFromString<T>(data)
    }

    //转为json字符串
//    val json = Json.encodeToString(Product("1228", "lcj"))
//
//    //字符串转为对象
////    val obj = Json.decodeFromString<Product>("""{"a":42, "b": "str"}""")
//    val obj = Json.decodeFromString<Product>(json)
//    Log.d(ShortVideoScreenTAG, "testJSON: ${json} ${obj.title}")
}