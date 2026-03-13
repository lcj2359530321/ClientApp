package com.quick.app.core.model

import com.quick.app.core.datastore.GlobalLyricStylePreferences
import com.quick.app.core.datastore.SessionPreferences
import com.quick.app.core.datastore.UserPreferences

//import com.quick.app.core.datastore.GlobalLyricStylePreferences
//import com.quick.app.core.datastore.SessionPreferences
//import com.quick.app.core.datastore.UserPreferences

/**
 * 用户偏好设置
 */
data class UserData(
    /**
     * 不显示引导界面
     *
     * protobuf默认false，所以用户安装后，会显示引导界面
     * 显示后，设置为true，下次就不显示了
     */
    val notShowGuide: Boolean = false,
    val session: SessionPreferences = SessionPreferences.newBuilder().build(),
    val user: UserPreferences = UserPreferences.newBuilder().build(),
    val notShowTermsServiceAgreement: Boolean = false,
    val darkThemeConfig: DarkThemeConfig = DarkThemeConfig.FOLLOW_SYSTEM,
    val useDynamicColor: Boolean = false,

    //播放循环模式
    val playRepeatMode: PlaybackMode = PlaybackMode.REPEAT_LIST,

    //播放音乐id
    val playMusicId: String = "",

    //播放进度
    val playProgress: Long = 0,

    //播放音乐时长
    val playDuration: Long = 0,

    val globalLyricStyle: GlobalLyricStylePreferences = GlobalLyricStylePreferences.newBuilder()
        .build(),
) {
    /**
     * 是否已经登录了
     */
    fun isLogin(): Boolean {
        return session.userId.isNotBlank()
        //return false
    }
}
