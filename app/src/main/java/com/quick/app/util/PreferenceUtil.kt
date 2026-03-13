package com.quick.app.util

import android.content.Context
import android.content.SharedPreferences
import com.google.common.base.Strings
import com.quick.app.MyApplication
import com.quick.app.core.model.Ad

/**
 * 偏好设置工具类
 */
object PreferenceUtil {
    val p: SharedPreferences by lazy {
        MyApplication.instance.getSharedPreferences("app_config", Context.MODE_PRIVATE)
    }

    /**
     * 获取启动界面广告
     *
     * @return
     */
    fun getSplashAd(): Ad? {
        val result = p.getString(SPLASH_AD, null)
        return if (Strings.isNullOrEmpty(result)) {
            null
        } else JSONUtil.fromJSON<Ad>(result!!)
    }

    /**
     * 设置启动界面广告
     *
     * @param data 如果为空，就是删除本地广告
     */
    fun setSplashAd(data: Ad?) {
        if (null == data) {
            delete(SPLASH_AD)
        } else {
            putString(SPLASH_AD, JSONUtil.toJSON(data))
        }
    }

    //region 辅助方法
    private fun getString(key: String): String? {
        return p.getString(key, null)
    }

    private fun putString(key: String, value: String) {
        p.edit().putString(key, value).apply()   //异步提交
    }

    private fun delete(data: String) {
        p.edit().remove(data).commit()   //同步提交
    }


    /**
     * 获取boolean
     *
     * @param key
     * @param defaultValue
     * @return
     */
    private fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return p.getBoolean(key, defaultValue)
    }

    /**
     * 保存boolean
     *
     * @param key
     * @param value
     */
    private fun putBoolean(key: String, value: Boolean) {
        p.edit().putBoolean(key, value).apply()
    }
    //endregion

    private const val SPLASH_AD = "SPLASH_AD"
}