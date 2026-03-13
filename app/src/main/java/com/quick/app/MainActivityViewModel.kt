package com.quick.app

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import cn.hutool.crypto.SecureUtil
import com.quick.app.core.data.repository.AdRepository
import com.quick.app.core.data.repository.UserDataRepository
import com.quick.app.core.data.repository.UserRepository
import com.quick.app.core.model.Ad
import com.quick.app.core.result.asResult
import com.quick.app.ui.MainActivityUiState
import com.quick.app.util.PreferenceUtil
import com.quick.app.util.ResourceUtil
import com.quick.app.util.StorageUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.apache.commons.io.FileUtils
import javax.inject.Inject

/**
 * 主界面viewmodel
 */
@HiltViewModel
class MainActivityViewModel
@Inject
constructor(
    private val adRepository: AdRepository,
    private val userRepository: UserRepository,
    private val userDataRepository: UserDataRepository,
    private val okHttpClient: OkHttpClient,
): ViewModel(){
    val state: StateFlow<MainActivityUiState> = userDataRepository.userData.map {
        MainActivityUiState.Success(it)
    }.stateIn(
        scope = viewModelScope,
        initialValue = MainActivityUiState.Loading,
        started = SharingStarted.WhileSubscribed(5_000),
    )

    fun loadSplashAd() {
        viewModelScope.launch() {
            //延迟，让主界面先请求数据
            delay(10000)

            adRepository.ads()
                .asResult()
                .collectLatest {
                    if (it.isSuccess) {
                        val r = it.getOrThrow()
                        if (r.data != null && r.data.list != null && r.data.list.isNotEmpty()) {
                            downloadAd(
                                r.data?.list!![java.util.Random()
                                    .nextInt(r.data?.list!!.size)]
//                                r.data?.list!![0] //视频广告
//                                r.data?.list!![1] //图片广告
                            )

                        } else {
                            //删除本地广告数据
                            deleteSplashAd()
                        }
                    }
                }
        }
    }

    private fun downloadAd(data: Ad) {
        //wifi才下载

        //判断文件是否存在，如果存在就不下载
        val targetFile = StorageUtil.adFile(MyApplication.instance, SecureUtil.md5(data.icon))
        if (targetFile.exists()) {
            Log.d(TAG, "skip downloadAd ${data.icon} ${targetFile.absolutePath}")

            //保存广告
            PreferenceUtil.setSplashAd(data)
            return
        }
        Log.d(TAG, "downloadAd  ${data.icon} ${targetFile.absolutePath}")

        Thread {
            try {
                val request = Request.Builder().url(ResourceUtil.r(data.icon)).get().build()

                // 发起请求得到请求结果
                val response = okHttpClient.newCall(request).execute()

                // 获取请求结果
                val responseBody = response.body
                if (null != responseBody) {
                    val inputStream = responseBody.byteStream()
                    //将文件拷贝到我们需要的位置
                    FileUtils.copyInputStreamToFile(inputStream, targetFile)

                    PreferenceUtil.setSplashAd(data)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }

    /**
     * 删除启动界面广告
     */
    private fun deleteSplashAd() {
        //获取广告信息
        val ad = PreferenceUtil.getSplashAd()

        ad?.let {
            //删除配置文件
            PreferenceUtil.setSplashAd(null)

            //删除文件
            val targetFile = StorageUtil.adFile(MyApplication.instance, SecureUtil.md5(it.icon))
            FileUtils.deleteQuietly(targetFile)
        }
    }

    fun loadUserData() {
        if (MyAppState.userId.isNotBlank()) {
            viewModelScope.launch {
                userRepository.userDetail(MyAppState.userId)
                    .asResult()
                    .collectLatest { userDetail ->
                        if (userDetail.isSuccess) {
                            //再更新本地存储
                            userDataRepository.setUser(userDetail.getOrThrow().data!!.toPreferences())
                        } else {

                        }
                    }
            }
        }
    }

    companion object {
        const val TAG = "MainActivityViewModel"
    }
}