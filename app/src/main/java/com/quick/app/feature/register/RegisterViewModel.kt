package com.quick.app.feature.register

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quick.app.MyApplication
import com.quick.app.R
import com.quick.app.core.data.repository.SessionRepository
import com.quick.app.core.data.repository.UserDataRepository
import com.quick.app.core.data.repository.UserRepository
import com.quick.app.core.exception.localException
import com.quick.app.core.model.User
import com.quick.app.core.result.asResult
import com.quick.app.util.StringUtil
import com.quick.app.util.SuperRegularUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val userDataRepository: UserDataRepository,
    private val userRepository: UserRepository,
) : ViewModel() {
    val uiState = MutableStateFlow<RegisterUiState>(RegisterUiState.None)

    val data = MutableStateFlow<User>(User())

    fun onValueChange(param: User) {
        data.value = param
    }

    fun resetUiState() {
        uiState.value = RegisterUiState.None
    }

    fun onRegisterClick() {
        val model = data.value

        //参数校验
        if (model.nickname!!.isBlank()) {
            setUiStateErrorRes(R.string.enter_nickname)
            return
        }

        if (!StringUtil.isNickname(model.nickname)) {
            setUiStateErrorRes(R.string.error_nickname_format)
            return
        }

        //手机号
        if (model.phone!!.isBlank()) {
            setUiStateErrorRes(R.string.enter_phone)
            return
        }

        if (!SuperRegularUtil.isPhone(model.phone)) {
            setUiStateErrorRes(R.string.error_phone_format)
            return
        }

        //email
        if (model.email!!.isBlank()) {
            setUiStateErrorRes(R.string.enter_email)
            return
        }

        if (!SuperRegularUtil.isEmail(model.email)) {
            setUiStateErrorRes(R.string.error_email_format)
            return
        }

        //密码
        if (model.password!!.isBlank()) {
            setUiStateErrorRes(R.string.enter_password)
            return
        }

        if (!StringUtil.isPassword(model.password)) {
            setUiStateErrorRes(R.string.error_password_format)
            return
        }

        //确认密码
        if (model.confirmPassword!!.isBlank()) {
            setUiStateErrorRes(R.string.enter_confirm_password)
            return
        }

        if (!StringUtil.isPassword(model.confirmPassword)) {
            setUiStateErrorRes(R.string.error_confirm_password_format)
            return
        }

        //确认密码和密码是否一样
        if (model.password != model.confirmPassword) {
            setUiStateErrorRes(R.string.error_confirm_password)
            return
        }

        val param = User(
            nickname = model.nickname,
            phone = model.phone,
            email = model.email,
            password = model.password,
        )

        viewModelScope.launch {
            userRepository.register(param)
                .asResult()
                .collectLatest {
                    if (it.isSuccess) {
                        //自动登录
                        login(param)
                    } else {
                        uiState.value =
                            RegisterUiState.Error(it.exceptionOrNull()!!.localException())
                    }
                }
        }

    }

    private fun login(param: User) {
        viewModelScope.launch {
            sessionRepository.login(param)
                .asResult()
                .collectLatest {
                    if (it.isSuccess) {
                        Log.d("TAG", "login: ${it.getOrThrow().data}")

                        val result = it.getOrThrow()

                        //保存登录信息
                        val sessionPreferences = result.data!!.toPreferences()
                        userDataRepository.setSession(sessionPreferences)
                        userDataRepository.setUser(result.data.user.toPreferences())

                        MyApplication.instance.initAfterLogin(sessionPreferences!!)

                        uiState.value = RegisterUiState.Success
                    } else {
                        uiState.value =
                            RegisterUiState.Error(it.exceptionOrNull()!!.localException())
                    }
                }
        }
    }

    private fun setUiStateErrorRes(res: Int) {
        uiState.value = RegisterUiState.ErrorRes(res)
    }
}

