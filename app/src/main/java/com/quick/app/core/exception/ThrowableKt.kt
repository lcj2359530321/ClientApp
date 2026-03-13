package com.quick.app.core.exception

import com.quick.app.MyApplication
import com.quick.app.R
import com.quick.app.util.NetworkUtil

fun Throwable.localException(): CommonException {
    if (this is CommonException) {
        this.tipString = this.networkResponse!!.message
            ?: MyApplication.instance.getString(R.string.unknown_error)
        return this
    } else {
        if (NetworkUtil.isNetworkAvailable(MyApplication.instance)) {
            //有网络
            return CommonException(
                throwable = this,
                tipString = MyApplication.instance.getString(
                    R.string.error_loading,
                    this.localizedMessage
                )
            )
        } else {
            return CommonException(
                throwable = this,
                tipString = MyApplication.instance.getString(R.string.error_network_not_connect),
                tipIcon = R.drawable.bg_error,
            )

        }
    }
}