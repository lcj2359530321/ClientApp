package com.quick.app.core.network.retrofit

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
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap

/**
 * 网络请求接口
 */
interface MyNetworkApiService {
    //region 音乐
    @GET("v1/songs/page")
    suspend fun songs(): NetworkResponse<NetworkPageData<Song>>

    @GET("v1/songs/info")
    suspend fun songDetail(
        @Query(value = "id") id: String,
    ): NetworkResponse<Song>
    //endregion

    /**
     * 首页列表
     */
    @GET("v1/indexes")
    suspend fun indexes(@Query(value = "app") app: Int): NetworkResponse<NetworkPageData<ViewData>>

    /**
     * 登录
     */
    @POST("v1/login")
    suspend fun login(
        @Body data: User,
    ): NetworkResponse<Session>

    /**
     * 微信登录
     *
     * 通过code
     */
    @POST("v2/wechat-login")
    suspend fun loginWechat(
        @Body data: WechatLoginRequest
    ): NetworkResponse<Session>

    @GET("v1/sheets/info")
    suspend fun sheetDetail(@Query(value = "id") id: String): NetworkResponse<Sheet>

    /**
     * 获取用户创建的歌单
     *
     * @param userId
     * @return
     */
    @GET("v1/users/{userId}/create")
    suspend fun createSheets(@Path("userId") userId: String): NetworkResponse<NetworkPageData<Sheet>>

    /**
     * 获取用户收藏的歌单
     *
     * @param userId
     * @return
     */
    @GET("v1/users/{userId}/collect")
    suspend fun collectSheets(@Path("userId") userId: String): NetworkResponse<NetworkPageData<Sheet>>

    /**
     * 创建歌单
     *
     * @param data
     * @return
     */
    @POST("v1/sheets/add")
    suspend fun createSheet(@Body data: Sheet): NetworkResponse<Sheet>

    @POST("v1/sheets/update")
    suspend fun updateSheet(@Body data: Sheet): NetworkResponse<Sheet>

    @POST("v1/collects/add")
    suspend fun collectSheet(
        @Body data: BaseId
    ): NetworkResponse<BaseModel>

    @POST("v1/collects/delete")
    suspend fun cancelCollectSheet(
        @Body data: BaseId
    ): NetworkResponse<BaseModel>

    @POST("v1/users/reset_password")
    suspend fun setPassword(
        @Body data: User
    ): NetworkResponse<BaseId>

    @POST("v1/users/add")
    suspend fun register(
        @Body data: User,
    ): NetworkResponse<BaseId>


    @GET("v1/users/info")
    suspend fun userDetail(@Query(value = "id") id: String): NetworkResponse<User>

    @POST("v1/users/update")
    suspend fun updateUser(
        @Body data: User
    ): NetworkResponse<BaseModel>

    @GET("v1/ads")
    suspend fun ads(
        @Query(value = "position") position: Int,
        @Query(value = "style") style: Int?,
        @Query(value = "app") app: Int
    ): NetworkResponse<NetworkPageData<Ad>>

    /**
     * 上传文件
     * @param file 文件
     * @param flavor 渠道，例如：客户端会传递prod，dev，local等值，服务端方便保存到不同地方，这样后面好清理测试资源
     * @param relative 0：返回绝对路径，默认；1：返回相对路径
     * @return
     */
    @Multipart
    @POST("v1/r")
    suspend fun uploadFile(
        @Part file: MultipartBody.Part,
        @Part("flavor") flavor: RequestBody,
        @Part("relative") relative: RequestBody,
    ): NetworkResponse<BaseId>

    /**
     * 上传多个文件
     * @param file 文件
     * @param flavor 渠道，例如：客户端会传递prod，dev，local等值，服务端方便保存到不同地方，这样后面好清理测试资源
     * @return
     */
    @Multipart
    @POST("v1/r/batch")
    suspend fun uploadFiles(
        @Part files: List<MultipartBody.Part>,
        @Part("flavor") flavor: RequestBody,
        @Part("relative") relative: RequestBody,
    ): NetworkResponse<NetworkPageData<String>>

    @POST("v1/feeds/add")
    suspend fun createFeed(
        @Body data: Feed
    ): NetworkResponse<BaseId>

    @GET("v1/feeds/page")
    suspend fun feeds(@QueryMap data: Map<String, String>): NetworkResponse<NetworkPageData<Feed>>

    /**
     * 内容列表
     *
     * @return
     */
    @GET("v1/contents/page")
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
    @POST("v1/codes")
    suspend fun sendCode(
        @Body data: CodeRequest
    ): NetworkResponse<BaseModel>

    /**
     * 校验验证码
     *
     * @param data
     * @return
     */
    @POST("v1/codes/check")
    suspend fun checkCode(
        @Body data: CodeRequest
    ): NetworkResponse<BaseModel>
    //endregion

    @GET("v1/searches/sheets")
    suspend fun searchSheets(@Query(value = "query") query: String): NetworkResponse<NetworkPageData<Sheet>>

    @GET("v1/searches/users")
    suspend fun searchUsers(@Query(value = "query") query: String): NetworkResponse<NetworkPageData<User>>

    /**
     * 搜索建议
     *
     * @param data
     * @return
     */
    @GET("v1/searches/suggests")
    suspend fun searchSuggest(@Query(value = "query") query: String): NetworkResponse<Suggest>
}