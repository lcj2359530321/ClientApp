package com.quick.app.feature.login

import android.text.TextUtils
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.quick.app.MyApplication
import com.quick.app.R
import com.quick.app.core.data.repository.SessionRepository
import com.quick.app.core.data.repository.UserDataRepository
import com.quick.app.core.exception.localException
import com.quick.app.core.model.CodeLogin
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
class LoginViewModel @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val userDataRepository: UserDataRepository,
) : ViewModel() {
    val uiState = MutableStateFlow<LoginUiState>(LoginUiState.None)

    fun onLoginClick(username:String,password:String){
        //参数检测
        if(username.isBlank()){
            setUiStateErrorRes(R.string.error_username_format)
            return
        }

        //如果用户名
        //不是手机号也不是邮箱
        //就是格式错误
        if (!(SuperRegularUtil.isPhone(username) || SuperRegularUtil.isEmail(username))) {
            setUiStateErrorRes(R.string.error_username_format)
            return
        }

        if (TextUtils.isEmpty(password)) {
            setUiStateErrorRes(R.string.enter_password)
            return
        }

        //判断密码格式
        if (!StringUtil.isPassword(password)) {
            setUiStateErrorRes(R.string.error_password_format)
            return
        }

        val param = User(
            phone = if (SuperRegularUtil.isPhone(username)) username else "",
            email = if (SuperRegularUtil.isEmail(username)) username else "",
            password = password
        )
        login(param)
    }

    private fun setUiStateErrorRes(res: Int) {
        uiState.value = LoginUiState.ErrorRes(res)
    }

    private fun login(param: User) {
        viewModelScope.launch {
            sessionRepository.login(param)
                .asResult()
                .collectLatest {
                    if(it.isSuccess){
                        val result = it.getOrThrow()

                        //保存登录信息
                        val sessionRepository = result.data!!.toPreferences()
                        userDataRepository.setSession(sessionRepository)
                        userDataRepository.setUser(result.data.user.toPreferences())

                        MyApplication.instance.initAfterLogin(sessionRepository!!)

                        uiState.value = LoginUiState.Success
                    }else{
                        uiState.value = LoginUiState.Error(it.exceptionOrNull()!!.localException())
                    }
                }
        }
    }

    fun resetUiState() {
        uiState.value = LoginUiState.None
    }

    fun login(param: CodeLogin) {
        val isPhone = SuperRegularUtil.isPhone(param.username)

        val user = User(
            phone = if (isPhone) param.username else "",
            email = if (!isPhone) param.username else "",
            code = param.code,
        )
        login(user)
    }
}