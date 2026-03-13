package com.quick.app

import android.app.Application
import android.util.Log
import com.quick.app.core.config.Config
import com.quick.app.core.data.repository.UserDataRepository
import com.quick.app.core.datastore.SessionPreferences
import com.quick.app.core.media.MediaServiceConnection
//import com.quick.app.util.SuperPackageUtil
//import com.tencent.mm.opensdk.modelbase.BaseResp
//import com.tencent.mm.opensdk.openapi.IWXAPI
//import com.tencent.mm.opensdk.openapi.WXAPIFactory
//import com.tencent.tauth.IUiListener
//import com.tencent.tauth.Tencent
//import com.tencent.tauth.UiError
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber
//import timber.log.Timber
import javax.inject.Inject

/**
 * 全局Application
 */
@HiltAndroidApp
class MyApplication : Application() {
    @Inject
    lateinit var userDataRepository: UserDataRepository

    private val applicationScope = CoroutineScope(SupervisorJob())

    /**
     * 微信回调
     *
     * 在登录等界面设置
     */
//    var processWechatStatusChanged: ((data: BaseResp) -> Unit)? = null
//
//    public lateinit var wxapi: IWXAPI

    override fun onCreate() {
        super.onCreate()
        instance = this

        initLog()
//
//        Log.d(TAG, "MyApplication onCreate: ")
//
//        initWechat()
//
//        initQQ()

        //监听用户信息，并更新到缓存对象上
        applicationScope.launch {
            userDataRepository.userData
                .map { it.session }
                .distinctUntilChanged()
                .collectLatest {
                    MyAppState.session = it.session
                    MyAppState.userId = it.userId
                }
        }

        //获取应用签名
//        val md5Signature = SuperPackageUtil.getMD5Signature(applicationContext)
//        val sha1Signature = SuperPackageUtil.getSHA1Signature(applicationContext)
//        Log.d(TAG, "md5Signature $md5Signature $sha1Signature")
    }

    /**
     * 初始化微信
     */
//    private fun initWechat() {
//        wxapi = WXAPIFactory.createWXAPI(applicationContext, null)
//        wxapi.registerApp(Config.WECHAT_AK)
//    }

    /**
     * 登录后初始化
     */
    private var isInitAfterLogin = false
    fun initAfterLogin(session: SessionPreferences) {
        destroyInstance()
        if (isInitAfterLogin) {
            return
        }

        isInitAfterLogin = true
    }

    //region 退出
    fun logout() {
        logoutSilence()
    }

    private fun logoutSilence() {
        isInitAfterLogin = false

        applicationScope.launch {
            //清除登录相关信息
            userDataRepository.logout()
        }

        destroyInstance()
    }
    //endregion

    private fun destroyInstance() {
        MyAppState.myDatabase = null
        MediaServiceConnection.destroyInstance()
    }

    /**
     * 初始化 只有同意了用户协议相关逻辑
     */
    private var isInitAfterAcceptTermsServiceAgreement = false
    fun initAfterAcceptTermsServiceAgreement() {
        if (isInitAfterAcceptTermsServiceAgreement) {
            return
        }
        isInitAfterAcceptTermsServiceAgreement = true
    }

    private fun initLog() {
        if (Config.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            //可以上报到任何地方
            //例如：阿里云日志服务；
        }

        Timber.w("警告")
        Timber.e("错误")

        Timber.d("调试 %s %d", "级别", 125)
    }

    //region QQ
//    public lateinit var tencentApi: Tencent
//
//    var processQQLoginSuccess: ((data: Any) -> Unit)? = null
//    var processQQLoginError: ((data: UiError) -> Unit)? = null
//
//    val qqLoginListener = object :
//        IUiListener {
//        override fun onComplete(data: Any) {
//            Timber.d("qq login onComplete: %s", data)
//            processQQLoginSuccess?.let { it(data) }
//        }
//
//        override fun onError(data: UiError) {
//            Timber.d("qq login onError: %s", data)
//            processQQLoginError?.let { it(data) }
//        }
//
//        override fun onCancel() {
//        }
//
//        override fun onWarning(p0: Int) {
//            Timber.d("qq login onWarning: %s", p0)
//        }
//    }
//
//    private fun initQQ() {
//        //https://wiki.connect.qq.com/qq%e7%99%bb%e5%bd%95
//        Tencent.setIsPermissionGranted(true)
//        tencentApi = Tencent.createInstance(Config.QQ_AK, applicationContext);
//    }
    //endregion

    companion object {
        private const val TAG = "MyApplication"

        lateinit var instance: MyApplication
    }
}