package com.quick.app.util

object Constant {
    const val VALUE_NO = -1
    const val VALUE_NO_STRING = "-1"
    const val VALUE0 = 0
    const val VALUE1 = 1
    const val VALUE10 = 10
    const val VALUE20 = 20
    const val VALUE30 = 30
    const val VALUE40 = 40

    /**
     * LRC歌词
     */
    const val LRC = 0

    /**
     * KSC歌词
     */
    const val KSC = 10


    const val HEADER_AUTH = "Authorization"

    //html压缩：https://www.fulimama.com/formathtml/
    const val HTML_DATA_START =
        "<!DOCTYPE html><html><head><meta charset=\"utf-8\"><meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"><title></title><style>* {word-wrap: break-word;white-space: normal;margin: 0;padding: 0;color: #222;}body{font-family: -apple-system,helvetica,sans-serif;line-height: 1.71;font-size: 17px;}img {max-width: 100%;}</style></head><body>"
    const val HTML_DATA_END = "</body></html>"

    /**
     * 搜索接口查询关键字
     */
    const val QUERY = "query"
    const val STYLE = "style"

    const val STYLE_CODE_LOGIN = VALUE0
    const val STYLE_FORGOT_PASSWORD = VALUE10

    const val EXTRA_MEDIA_ID = "extra_media_id"
    const val EXTRA_GLOBAL_LYRIC = "extra_lyric"

    const val EXTRA_REQUEST_DRAW_OVERLAYS_PERMISSION = "EXTRA_REQUEST_DRAW_OVERLAYS_PERMISSION"

    const val ACTION_UNLOCK_LYRIC = "ACTION_UNLOCK_LYRIC"


    /**
     * 解锁全局歌词Id
     */
    const val NOTIFICATION_UNLOCK_LYRIC_ID = 10001

}