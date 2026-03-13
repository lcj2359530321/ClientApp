package com.quick.app.util

import android.os.Process

/**
 * 进程工具类
 */
object SuperProcessUtil {
    /**
     * 杀死当前应用
     */
    fun killApp() {
        Process.killProcess(Process.myPid())
    }
}