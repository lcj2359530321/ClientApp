package com.quick.app.feature.splash

import android.os.CountDownTimer
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import cn.hutool.crypto.SecureUtil
import com.quick.app.MyApplication
import com.quick.app.core.data.repository.UserDataRepository
import com.quick.app.core.model.Ad
import com.quick.app.core.model.UserData
import com.quick.app.util.Constant
import com.quick.app.util.PreferenceUtil
import com.quick.app.util.StorageUtil
import com.quick.app.util.SuperProcessUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.time.delay
import java.io.File
import java.sql.Time
import javax.inject.Inject

/**
 * 启动界面
 */
@HiltViewModel
class SplashViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository,
): ViewModel(){
    private var timer: CountDownTimer? = null

    /**
     * 倒计时秒数
     */
    private val _timeLeft = MutableStateFlow(0L)
    val timeLeft: StateFlow<Long> = _timeLeft

    /**
     * 是否跳转到主界面
     */
    val navigateToMain = MutableStateFlow(false)

    /**
     * 是否跳转到引导页
     */
    private val _navigateToGuide = MutableStateFlow(false)
    val navigateToGuide: StateFlow<Boolean> = _navigateToGuide

    private val _isShowTermsServiceAgreementDialog = MutableStateFlow<Boolean>(false)
    val isShowTermsServiceAgreementDialog: StateFlow<Boolean> = _isShowTermsServiceAgreementDialog

    private val _imageAd = MutableStateFlow<String?>(null)
    val imageAd: StateFlow<String?> = _imageAd

    private val _videoAd = MutableStateFlow<String?>(null)
    val videoAd: StateFlow<String?> = _videoAd

    val navigateToAd = mutableStateOf<Ad?>(null)

    private lateinit var currentUserData: UserData
    private var adData: Ad? = null

    init {
        viewModelScope.launch {
            userDataRepository.userData
                .collectLatest {
                    currentUserData = it
                    if (it.notShowTermsServiceAgreement) {
                        //已经同意了用户协议
                        if (it.isLogin()) {
                            MyApplication.instance.initAfterLogin(it.session)
                        }

                        prepareNext()
                    } else {
                        //显示用户协议对话框
                        _isShowTermsServiceAgreementDialog.value = true
                    }
            }
        }
    }

    private fun prepareNext() {
        MyApplication.instance.initAfterAcceptTermsServiceAgreement()

        adData = PreferenceUtil.getSplashAd()
        if (adData != null) {
            val targetFile: File =
                StorageUtil.adFile(MyApplication.instance, SecureUtil.md5(adData!!.icon))
            if (!targetFile.exists()) {
                //记录日志，因为正常来说，只要保存了，文件不能丢失
                delayToNext()
                return
            }

            if (adData!!.style == Constant.VALUE0) {
                _imageAd.value = targetFile.absolutePath
                delayToNext()
            } else if (adData!!.style == Constant.VALUE10) {
                _videoAd.value = targetFile.absolutePath
            }
        } else {
            //没有广告
            delayToNext()
        }
    }

    private fun delayToNext(time: Long = 3000) {
        timer = object : CountDownTimer(time, 1000) {
            /**
             * 每次倒计时执行
             */
            override fun onTick(millisUntilFinished: Long) {
                _timeLeft.value = millisUntilFinished / 1000 + 1
            }

            /**
             * 倒计时结束
             */
            override fun onFinish() {
                toNext()
            }

        }.start()
    }

    private fun toNext() {
        if (currentUserData.notShowGuide) {
            navigateToMain.value = true
        } else {
            _navigateToGuide.value = true
        }
    }

    fun onSkipAdClick() {
        timer?.cancel()
        toNext()
    }

    fun setTimeLeft(data: Long) {
        if (data <= 0) {
            return
        }
        delayToNext(data)
    }

    private fun hideTermsServiceAgreementDialog() {
        _isShowTermsServiceAgreementDialog.value = false
    }

    /**
     * 同意用户协议按钮点击
     */
    fun onPrimaryClick() {
        hideTermsServiceAgreementDialog()
        viewModelScope.launch {
            userDataRepository.setNotShowTermsServiceAgreement(true)
            prepareNext()
        }
    }

    /**
     * 拒绝用户协议按钮点击
     */
    fun onDisagreeClick() {
        hideTermsServiceAgreementDialog()
        SuperProcessUtil.killApp()
    }

    fun onAdClick() {
        timer?.cancel()
        navigateToAd.value = adData
    }

    /**
     * 执行下一步操作
     */
    fun onSkipClick() {
        timer?.cancel()
        toNext()
    }
}