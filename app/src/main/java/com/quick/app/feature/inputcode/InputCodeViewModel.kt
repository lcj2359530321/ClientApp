package com.quick.app.feature.inputcode

import android.os.CountDownTimer
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.quick.app.MyApplication
import com.quick.app.R
import com.quick.app.core.data.repository.CodeRepository
import com.quick.app.core.data.repository.UserDataRepository
import com.quick.app.core.exception.localException
import com.quick.app.core.model.CodeLogin
import com.quick.app.core.model.CodeRequest
import com.quick.app.core.result.asResult
import com.quick.app.feature.setpassword.SetPasswordPageData
import com.quick.app.ui.BaseViewModel
import com.quick.app.util.Constant
import com.quick.app.util.Constant.STYLE
import com.quick.app.util.SuperRegularUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class InputCodeViewModel
@Inject constructor
    (
    private val savedStateHandle: SavedStateHandle,
    private val codeRepository: CodeRepository,
    private val userDataRepository: UserDataRepository,
) : BaseViewModel() {
    val style = savedStateHandle.getStateFlow(key = STYLE, initialValue = Constant.STYLE_CODE_LOGIN)
    val username =
        savedStateHandle.getStateFlow<String>(key = INPUT_CODE_USERNAME, initialValue = "")

    val codeRequest = if (SuperRegularUtil.isPhone(username.value)) {
        CodeRequest(phone = username.value)
    } else {
        CodeRequest(email = username.value)
    }

    val sendTargetTitle: StateFlow<String> = username.map {
        MyApplication.instance.getString(R.string.verification_code_sent_to, it)
    }.stateIn(
        scope = viewModelScope,
        initialValue = "",
        started = SharingStarted.WhileSubscribed(5_000),
    )

    private val _sendTitle =
        MutableStateFlow<String>(MyApplication.instance.getString(R.string.resend))
    val sendTitle: StateFlow<String> = _sendTitle

    val sendEnable: MutableStateFlow<Boolean> = MutableStateFlow<Boolean>(false)

    private val _code =
        MutableStateFlow<String>("")
    val code: StateFlow<String> = _code

    private val _codeLogin = mutableStateOf<CodeLogin>(CodeLogin())
    val codeLogin: State<CodeLogin> = _codeLogin

    private val _toSetPassword = mutableStateOf<SetPasswordPageData>(SetPasswordPageData())
    val toSetPassword: State<SetPasswordPageData> = _toSetPassword

    private var countDownTimer: CountDownTimer? = null

    init {
        onSendClick()
    }

    fun onCodeChange(data: String) {
        _code.value = data
    }

    fun onSendClick() {
        viewModelScope.launch {
            codeRepository.sendCode(
                codeRequest
            ).asResult()
                .collectLatest {
                    if (it.isSuccess) {
                        //发送成功了

                        //开始倒计时
                        startCountDown()
                    } else {
                        setTipError(it.exceptionOrNull()!!.localException().tipString!!)
                    }
                }
        }
    }

    /**
     * 开始倒计时
     */
    private fun startCountDown() {
        //倒计时的总时间,间隔
        //单位是毫秒
        countDownTimer = object : CountDownTimer(60000, 1000) {
            /**
             * 间隔时间调用
             * @param millisUntilFinished
             */
            override fun onTick(millisUntilFinished: Long) {
                _sendTitle.value = MyApplication.instance.getString(
                    R.string.resend_count,
                    millisUntilFinished / 1000
                )
            }

            /**
             * 倒计时完成
             */
            override fun onFinish() {
                enableSendButton()
            }
        }

        //启动
        countDownTimer!!.start()
        sendEnable.value = false
    }

    private fun enableSendButton() {
        _sendTitle.value = MyApplication.instance.getString(R.string.resend)
        sendEnable.value = true
    }

    fun onCodeInputComplete(data: String) {
        if (Constant.STYLE_CODE_LOGIN == style.value) {
            //验证码登录
            _codeLogin.value = CodeLogin(
                username = username.value,
                code = data,
            )
        } else {
            //设置密码

            //先校验验证码
            viewModelScope.launch {
                codeRepository
                    .checkCode(
                        codeRequest.copy(
                            code = data,
                        )
                    )
                    .asResult()
                    .collectLatest {
                        if (it.isSuccess) {
                            //验证码正确

                            //跳转到设置密码界面
                            _toSetPassword.value =
                                SetPasswordPageData(
                                    username = username.value,
                                    code = data,
                                )
                        } else {
                            setTipError(it.exceptionOrNull()!!.localException().tipString!!)
                        }
                    }
            }
        }


    }

    fun clearToSetPassword() {
        _toSetPassword.value =
            SetPasswordPageData()
    }
}