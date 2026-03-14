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

}
