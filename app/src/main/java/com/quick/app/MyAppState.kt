package com.quick.app

import com.quick.app.core.database.MyDatabase

/**
 * 全局界面以外状态
 */
object MyAppState {
    var session: String = ""
    var userId: String = ""
    var myDatabase: MyDatabase? = null
}