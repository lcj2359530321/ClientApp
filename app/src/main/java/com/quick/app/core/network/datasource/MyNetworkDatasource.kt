package com.quick.app.core.network.datasource

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
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface MyNetworkDatasource {
    suspend fun songs(): NetworkResponse<NetworkPageData<Song>>

    suspend fun songDetail(
        @Query(value = "id") id: String,
    ): NetworkResponse<Song>

    suspend fun indexes(app: Int): NetworkResponse<NetworkPageData<ViewData>>

    suspend fun sheetDetail(id: String): NetworkResponse<Sheet>

    suspend fun createSheets(userId: String): NetworkResponse<NetworkPageData<Sheet>>

    suspend fun collectSheets(userId: String): NetworkResponse<NetworkPageData<Sheet>>

    suspend fun createSheet(@Body data: Sheet): NetworkResponse<Sheet>

    suspend fun updateSheet(@Body data: Sheet): NetworkResponse<Sheet>

    suspend fun collectSheet(
        @Body data: BaseId
    ): NetworkResponse<BaseModel>

    suspend fun cancelCollectSheet(
        @Body data: BaseId
    ): NetworkResponse<BaseModel>

    /**
     * 登录
     */
    suspend fun login(
        data: User,
    ): NetworkResponse<Session>

    /**
     * 微信登录
     *
     * 通过code
     */
    suspend fun loginWechat(
        data: WechatLoginRequest
    ): NetworkResponse<Session>

    suspend fun register(
        @Body data: User,
    ): NetworkResponse<BaseId>

    suspend fun setPassword(
        @Body data: User
    ): NetworkResponse<BaseId>

    suspend fun userDetail(id: String): NetworkResponse<User>

    suspend fun updateUser(
        @Body data: User
    ): NetworkResponse<BaseModel>

    suspend fun ads(
        @Query(value = "position") position: Int,
        @Query(value = "style") style: Int?,
        @Query(value = "app") app: Int
    ): NetworkResponse<NetworkPageData<Ad>>

    suspend fun uploadFile(
        file: MultipartBody.Part,
        flavor: RequestBody,
        relative: RequestBody,
    ): NetworkResponse<BaseId>

    suspend fun uploadFiles(
        files: List<MultipartBody.Part>,
        flavor: RequestBody,
        relative: RequestBody,
    ): NetworkResponse<NetworkPageData<String>>

    suspend fun createFeed(
        @Body data: Feed
    ): NetworkResponse<BaseId>

    suspend fun feeds(@QueryMap data: Map<String, String>): NetworkResponse<NetworkPageData<Feed>>

    /**
     * 内容列表
     *
     * @return
     */
    suspend fun contents(
        @Query(value = "last") last: String?,
        @Query(value = "category_id") categoryId: String?,
        @Query(value = "user_id") userId: String?,
        @Query(value = "size") size: Int,
        @Query(value = "style") style: Int? = null,
        @Query(value = "current") current: String? = null,
    ): NetworkResponse<NetworkPageData<Content>>

    //region 验证码
    /**
     * 发送验证码
     *
     * @param data
     * @return
     */
    suspend fun sendCode(
        @Body data: CodeRequest
    ): NetworkResponse<BaseModel>

    /**
     * 校验验证码
     *
     * @param data
     * @return
     */
    suspend fun checkCode(
        @Body data: CodeRequest
    ): NetworkResponse<BaseModel>
    //endregion


    suspend fun searchSheets(query: String): NetworkResponse<NetworkPageData<Sheet>>

    suspend fun searchUsers(query: String): NetworkResponse<NetworkPageData<User>>

    suspend fun searchSuggest(query: String): NetworkResponse<Suggest>
}