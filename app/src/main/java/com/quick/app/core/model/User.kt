package com.quick.app.core.model

import com.google.common.base.Strings
import com.quick.app.core.datastore.UserPreferences
import kotlinx.serialization.Serializable

/**
 * 用户模型
 */
@Serializable
data class User(
    val id: String = "",
    /**
     * 昵称
     */
    val nickname: String = "",

    /**
     * 头像
     */
    val icon: String = "",

    /**
     * 描述
     */
    val detail: String = "",


    /**
     * 性别
     * 0：保密，10：男，20：女
     * 可以定义为枚举
     * 但枚举性能没有int好
     * 但int没有一些编译验证
     * Android中有替代方式
     * 这里用不到就不讲解了
     */
    val gender: String = "0",

    /**
     * 生日
     * 格式为：yyyy-MM-dd
     */
    val birthday: String = "",

    /**
     * 朋友圈背景
     */
    val bg: String = "",

    /**
     * 手机号
     */
    val phone: String = "",

    /**
     * 邮箱
     */
    val email: String = "",

    /**
     * 用户的密码,登录，注册向服务端传递
     */
    val password: String = "",
    val confirmPassword: String = "",

    /**
     * QQ第三方登录后昵称
     */
    val qqNickname: String = "",

    /**
     * 微信第三方登录后昵称
     */
    val wechatNickname: String = "",

    /**
     * 验证码
     * 只有找回密码的时候才会用到
     */
    val code: String = "",

    /**
     * 省
     */
    val province: String = "",

    /**
     * 省编码
     */
    val provinceCode: String = "",

    /**
     * 市
     */
    val city: String = "",

    /**
     * 市编码
     */
    val cityCode: String = "",


    /**
     * 区
     */
    val area: String = "",

    /**
     * 区编码
     */
    val areaCode: String = "",

    /**
     * 我的关注的人（好友）
     */
    val followingsCount: Long = 0,

    /**
     * 关注我的人（粉丝）
     */
    val followersCount: Long = 0,

    /**
     * 是否关注
     * 1:关注
     * 在用户详情才会返回
     */
    val following: String = "",

    //region 本地过滤
    /**
     * 拼音
     */
    val pinyin: String = "",

    /**
     * 拼音首字母
     */
    val pinyinFirst: String = "",

    /**
     * 拼音首字母的首字母
     */
    val first: String = "",

    //endregion

    //region 第三方登录
    val token: String = "",
    val refeshToken: String = "",
    val openid: String = "",
    val expiresIn: Long = 0,

    /**
     * 第三方登录
     * 10：qq登录
     */
    val style: Int = 0,
//endregion
) {
    fun toPreferences(): UserPreferences {
        return UserPreferences.newBuilder()
            .setId(id)
            .setNickname(nickname)
            .setIcon(icon ?: "")
            .setDetail(detail ?: "")
            .setGender(gender)
            .build()
    }

    /**
     * 格式化后的性别
     *
     * @return
     */
    fun getGenderFormat(): String {
        return when (gender) {
            "10" -> "男"
            "20" -> "女"
            else ->                 //0
                "保密"
        }
    }

    val areaDisplay: String
        get() = if (Strings.isNullOrEmpty(province)) "" else "$province$city$area"

    /**
     * 获取昵称
     *
     * @param id
     * @param nickname
     * @return
     */
    val nicknameFormat: String
        get() = if (Strings.isNullOrEmpty(nickname)) id!! else nickname!!
}
