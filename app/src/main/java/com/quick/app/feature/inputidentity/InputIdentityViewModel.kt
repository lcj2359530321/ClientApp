package com.quick.app.feature.inputidentity

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.quick.app.R
import com.quick.app.core.config.Config
import com.quick.app.feature.inputcode.InputCodePageData
import com.quick.app.ui.BaseViewModel
import com.quick.app.util.Constant
import com.quick.app.util.Constant.STYLE
import com.quick.app.util.SuperRegularUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class InputIdentityViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : BaseViewModel() {
    val style = savedStateHandle.getStateFlow(key = STYLE, initialValue = Constant.STYLE_CODE_LOGIN)

    val title: StateFlow<Int> = style.map {
        if (it == Constant.STYLE_CODE_LOGIN)
        //验证码登录
            R.string.code_login
        else
        //找回密码
            R.string.forgot_password
    }.stateIn(
        scope = viewModelScope,
        initialValue = R.string.code_login,
        started = SharingStarted.WhileSubscribed(5_000),
    )

    private val _username = MutableStateFlow<String>(
        if (Config.DEBUG) "13141111222" else ""
    )
    val username: StateFlow<String> = _username

    private val _toInputCode = mutableStateOf<InputCodePageData>(InputCodePageData())
    val toInputCode: State<InputCodePageData> = _toInputCode

    fun onUsernameChange(data: String) {
        _username.value = data
    }

    fun onPrimaryClick() {
        val data = _username.value
        if (SuperRegularUtil.isPhone(data) || SuperRegularUtil.isEmail(data)) {
            _toInputCode.value = InputCodePageData(
                username.value,
                style.value,
            )
        } else {
            setTipErrorRes(R.string.error_username_format)
        }
    }

    fun clearToInputCode() {
        _toInputCode.value = InputCodePageData()
    }

}