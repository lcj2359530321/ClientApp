package com.quick.app.core.config

import com.quick.app.BuildConfig

/**
 * 配置文件
 *
 * 例如：API地址，QQ等第三方服务配置信息等
 */
object Config {
    /**
     * 是否是调试模式
     */
 //   val DEBUG: Boolean = true
    val DEBUG: Boolean = BuildConfig.DEBUG

    /**
     * 端点
     */
//    const val ENDPOINT = "https://quick-server-sp.ixuea.com/"
    const val ENDPOINT = BuildConfig.ENDPOINT

    /**
     * 资源端点
     */
   // var RESOURCE_ENDPOINT = "https://com-quick-project.oss-cn-beijing.aliyuncs.com/%s"
    var RESOURCE_ENDPOINT = BuildConfig.RESOURCE_ENDPOINT

    //    var RESOURCE_ENDPOINT2 = "http://course-music-dev.ixuea.com/%s"
    var RESOURCE_ENDPOINT2 = "https://rs.ixuea.com/music/%s"

    const val LINK_USER_USER_AGREEMENT = "https://gemini.google.com"
    const val LINK_USER_PRIVACY_POLICY = "https://gemini.google.com/"


    /**
     * 二维码地址
     * 真实项目中一般设置为应用的下载宣传界面，因为目前没有这样的界面，所以就设置为官网地址
     */
    const val QRCODE_URL = "https://www.ixuea.com"

    /**
     * 用户二维码
     */
    val USER_QRCODE_URL = String.format("%s?u=%%s", QRCODE_URL)

    /**
     * 微信id
     */
    const val WECHAT_AK = "wx28456f1c56a1c0c4"

    /**
     * QQ id
     */
    const val QQ_AK = "102081660"
}
