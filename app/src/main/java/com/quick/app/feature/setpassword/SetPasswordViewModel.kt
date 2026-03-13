package com.quick.app.feature.setpassword

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.google.common.base.Strings
import com.quick.app.MyApplication
import com.quick.app.R
import com.quick.app.core.data.repository.SessionRepository
import com.quick.app.core.data.repository.UserDataRepository
import com.quick.app.core.data.repository.UserRepository
import com.quick.app.core.exception.localException
import com.quick.app.core.model.User
import com.quick.app.core.result.asResult
import com.quick.app.feature.inputcode.INPUT_CODE_USERNAME
import com.quick.app.ui.BaseViewModel
import com.quick.app.util.StringUtil
import com.quick.app.util.SuperRegularUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SetPasswordViewModel
@Inject constructor
    (
    private val savedStateHandle: SavedStateHandle,
    private val sessionRepository: SessionRepository,
    private val userRepository: UserRepository,
    private val userDataRepository: UserDataRepository,
) : BaseViewModel() {
    val username = savedStateHandle.getStateFlow(key = INPUT_CODE_USERNAME, initialValue = "")
    val code = savedStateHandle.getStateFlow(key = INPUT_CODE, initialValue = "")

    private val _finishAllLoginPages = mutableStateOf<Boolean>(false)
    val finishAllLoginPages: State<Boolean> = _finishAllLoginPages

    private val _password = MutableStateFlow<String>("")
    val password: StateFlow<String> = _password

    private val _confirmPassword = MutableStateFlow<String>("")
    val confirmPassword: StateFlow<String> = _confirmPassword

    fun onPasswordChange(data: String) {
        _password.value = data
    }

    fun onConfirmPasswordChange(data: String) {
        _confirmPassword.value = data
    }

    fun onPrimaryClick() {
        if (Strings.isNullOrEmpty(password.value)) {
            setTipErrorRes(R.string.enter_password)
            return
        }

        //密码格式
        if (!StringUtil.isPassword(password.value)) {
            setTipErrorRes(R.string.error_password_format)
            return
        }

        if (Strings.isNullOrEmpty(confirmPassword.value)) {
            setTipErrorRes(R.string.enter_confirm_password)
            return
        }

        //密码格式
        if (!StringUtil.isPassword(confirmPassword.value)) {
            setTipErrorRes(R.string.error_confirm_password_format)
            return
        }

        //判断密码和确认密码是否一样
        if (password.value != confirmPassword.value) {
            setTipErrorRes(R.string.error_confirm_password)
            return
        }

        viewModelScope.launch {
            val isPhone = SuperRegularUtil.isPhone(username.value)
            val param = User(
                phone = if (isPhone) username.value else "",
                email = if (!isPhone) username.value else "",
                code = code.value,
                password = password.value,
            )
            userRepository.setPassword(
                param
            ).asResult()
                .collectLatest {
                    if (it.isSuccess) {
                        //重置密码成功
                        //自动登录
                        login(param)
                    } else {
                        setTipError(it.exceptionOrNull()!!.localException().tipString!!)
                    }
                }
        }
    }

    private fun login(data: User) {
        viewModelScope.launch {
            sessionRepository.login(data)
                .asResult()
                .collectLatest {
                    if (it.isSuccess) {
                        val result = it.getOrThrow()
                        val sessionPreferences = result.data!!.toPreferences()
                        userDataRepository.setSession(sessionPreferences)
                        userDataRepository.setUser(result.data!!.user.toPreferences())
                        MyApplication.instance.initAfterLogin(sessionPreferences!!)
                        _finishAllLoginPages.value = true
                    } else {
                        setTipError(it.exceptionOrNull()!!.localException().tipString!!)
                    }

                }
        }
    }

}