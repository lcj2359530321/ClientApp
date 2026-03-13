package com.quick.app.core.network.di

import android.util.Log
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.quick.app.MyAppState
import com.quick.app.MyApplication
import com.quick.app.core.config.Config
import com.quick.app.util.Constant
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 网络依赖注入模块
 */
@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun providesNetworkJson(): Json = Json {
        ignoreUnknownKeys = true
    }

    @Provides
    @Singleton
    fun okHttpCallFactory(
        okHttpClient: OkHttpClient,
    ): Call.Factory = okHttpClient

    @Provides
    @Singleton
    fun providesOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS) //连接超时时间
            .writeTimeout(10, TimeUnit.SECONDS) //写超时时间
            .readTimeout(10, TimeUnit.SECONDS) //读超时时间
            //添加日志拦截器
            .addInterceptor(HttpLoggingInterceptor().apply {
                setLevel(
                    if (Config.DEBUG)
                        HttpLoggingInterceptor.Level.BODY
                    else
                        HttpLoggingInterceptor.Level.NONE
                )
            })

            //添加chucker实现应用内显示网络请求信息拦截器
            .addInterceptor(ChuckerInterceptor.Builder(MyApplication.instance).build())

            .addInterceptor {
                var request = it.request()

                if (MyAppState.session.isNotBlank()) {
                    //已经登录
                    Log.d(TAG, "providesOkHttpClient auth: ${MyAppState.session}")

                    request = request.newBuilder()
                        .header(Constant.HEADER_AUTH, MyAppState.session)
                        .build()
                }

                it.proceed(request)
            }

            .build()
    }

    companion object {
        const val TAG = "NetworkModule"
    }
}