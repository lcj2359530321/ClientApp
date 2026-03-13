package com.quick.app.feature.inputcode

import com.quick.app.util.Constant

data class InputCodePageData(
    val username: String = "",
    val style: Int = Constant.STYLE_CODE_LOGIN,
) {
}