package com.quick.app.core.network.datasource

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.quick.app.core.config.Config
import com.quick.app.core.model.Ad
import com.quick.app.core.model.BaseId
import com.quick.app.core.model.BaseModel
import com.quick.app.core.model.CodeRequest
import com.quick.app.core.model.Content
import com.quick.app.core.model.Feed
import com.quick.app.core.model.Session
import com.quick.app.core.model.Sheet
import com.quick.app.core.model.Song
import com.quick.app.core.model.Suggest
import com.quick.app.core.model.User
import com.quick.app.core.model.ViewData
import com.quick.app.core.model.WechatLoginRequest
import com.quick.app.core.model.response.NetworkPageData
import com.quick.app.core.model.response.NetworkResponse
import com.quick.app.core.network.retrofit.MyNetworkApiService
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.http.Query
import javax.inject.Inject

class MyRetrofitDatasource @Inject constructor(
    networkJson: Json,
    okhttpCallFactory: Call.Factory,
) : MyNetworkDatasource {
    /**
     * 网络请求接口
     */
    private val service = Retrofit.Builder()
        .baseUrl(Config.ENDPOINT)
        .callFactory(okhttpCallFactory)
        .addConverterFactory(
            networkJson.asConverterFactory("application/json".toMediaType())
        )
        .build()
        .create(MyNetworkApiService::class.java)

    override suspend fun songs(): NetworkResponse<NetworkPageData<Song>> {
        return service.songs()
    }

    override suspend fun songDetail(
        @Query(value = "id") id: String,
    ): NetworkResponse<Song> {
        return service.songDetail(id)
    }

    override suspend fun indexes(app: Int): NetworkResponse<NetworkPageData<ViewData>> {
        return service.indexes(app)
    }

    override suspend fun createSheets(userId: String): NetworkResponse<NetworkPageData<Sheet>> {
        return service.createSheets(userId)
    }

    override suspend fun collectSheets(userId: String): NetworkResponse<NetworkPageData<Sheet>> {
        return service.collectSheets(userId)
    }

    override suspend fun createSheet(data: Sheet): NetworkResponse<Sheet> {
        return service.createSheet(data)
    }

    override suspend fun updateSheet(data: Sheet): NetworkResponse<Sheet> {
        return service.updateSheet(data)
    }

    override suspend fun sheetDetail(id: String): NetworkResponse<Sheet> {
        return service.sheetDetail(id)
    }

    override suspend fun collectSheet(data: BaseId): NetworkResponse<BaseModel> {
        return service.collectSheet(data)
    }

    override suspend fun cancelCollectSheet(data: BaseId): NetworkResponse<BaseModel> {
        return service.cancelCollectSheet(data)
    }

    override suspend fun login(data: User): NetworkResponse<Session> {
        return service.login(data)
    }

    override suspend fun loginWechat(data: WechatLoginRequest): NetworkResponse<Session> {
        return service.loginWechat(data)
    }

    override suspend fun userDetail(id: String): NetworkResponse<User> {
        return service.userDetail(id)
    }

    override suspend fun updateUser(data: User): NetworkResponse<BaseModel> {
        return service.updateUser(data)
    }

    override suspend fun register(data: User): NetworkResponse<BaseId> {
        return service.register(data)
    }

    override suspend fun setPassword(data: User): NetworkResponse<BaseId> {
        return service.setPassword(data)
    }

    override suspend fun ads(
        position: Int,
        style: Int?,
        app: Int
    ): NetworkResponse<NetworkPageData<Ad>> {
        return service.ads(
            position = position,
            style = style,
            app = app,
        )
    }

    override suspend fun uploadFile(
        file: MultipartBody.Part,
        flavor: RequestBody,
        relative: RequestBody
    ): NetworkResponse<BaseId> {
        return service.uploadFile(file, flavor, relative)
    }

    override suspend fun uploadFiles(
        files: List<MultipartBody.Part>,
        flavor: RequestBody,
        relative: RequestBody
    ): NetworkResponse<NetworkPageData<String>> {
        return service.uploadFiles(files, flavor, relative)
    }

    override suspend fun createFeed(data: Feed): NetworkResponse<BaseId> {
        return service.createFeed(data)
    }

    override suspend fun feeds(data: Map<String, String>): NetworkResponse<NetworkPageData<Feed>> {
        return service.feeds(data)
    }

    override suspend fun contents(
        last: String?,
        categoryId: String?,
        userId: String?,
        size: Int,
        style: Int?,
        current: String?,
    ): NetworkResponse<NetworkPageData<Content>> {
        return service.contents(last, categoryId, userId, size, style, current)
    }

    //region 验证码
    override suspend fun sendCode(data: CodeRequest): NetworkResponse<BaseModel> {
        return service.sendCode(data)
    }

    override suspend fun checkCode(data: CodeRequest): NetworkResponse<BaseModel> {
        return service.checkCode(data)
    }
    //endregion

    override suspend fun searchSheets(query: String): NetworkResponse<NetworkPageData<Sheet>> {
        return service.searchSheets(query)
    }

    override suspend fun searchUsers(query: String): NetworkResponse<NetworkPageData<User>> {
        return service.searchUsers(query)
    }

    override suspend fun searchSuggest(query: String): NetworkResponse<Suggest> {
        return service.searchSuggest(query)
    }
}