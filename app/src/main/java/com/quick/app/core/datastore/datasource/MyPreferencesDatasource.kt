package com.quick.app.core.datastore.datasource

import android.util.Log
import androidx.datastore.core.DataStore
import com.quick.app.core.datastore.DarkThemeConfigPreferences
import com.quick.app.core.datastore.PlaybackModePreferences
import com.quick.app.core.datastore.SessionPreferences
import com.quick.app.core.datastore.UserDataPreferences
import com.quick.app.core.datastore.UserPreferences
import com.quick.app.core.datastore.copy
import com.quick.app.core.model.DarkThemeConfig
import com.quick.app.core.model.PlaybackMode
import com.quick.app.core.model.UserData
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject


class MyPreferencesDatasource @Inject constructor(
    /**
     * https://developer.android.google.cn/topic/libraries/architecture/datastore?hl=zh-cn#prefs-vs-proto
     * https://developer.android.google.cn/jetpack/androidx/releases/datastore?hl=en
     * https://developer.android.google.cn/codelabs/android-preferences-datastore?hl=zh_cn#0
     */
    private val userPreferences: DataStore<UserDataPreferences>,
) {
    val userData = userPreferences.data.map {
        UserData(
            notShowGuide = it.notShowGuide,
            session = it.session,
            user = it.user,
            notShowTermsServiceAgreement = it.notShowTermsServiceAgreement,
            darkThemeConfig = when (it.darkThemeConfig) {
                null,
                DarkThemeConfigPreferences.DARK_THEME_CONFIG_UNSPECIFIED,
                DarkThemeConfigPreferences.UNRECOGNIZED,
                DarkThemeConfigPreferences.DARK_THEME_CONFIG_FOLLOW_SYSTEM,
                ->
                    DarkThemeConfig.FOLLOW_SYSTEM

                DarkThemeConfigPreferences.DARK_THEME_CONFIG_LIGHT ->
                    DarkThemeConfig.LIGHT

                DarkThemeConfigPreferences.DARK_THEME_CONFIG_DARK -> DarkThemeConfig.DARK
            },
            useDynamicColor = it.useDynamicColor,
            playRepeatMode = when (it.playRepeatMode) {
                PlaybackModePreferences.PLAYBACK_MODE_REPEAT_LIST -> PlaybackMode.REPEAT_LIST
                PlaybackModePreferences.PLAYBACK_MODE_REPEAT_ONE -> PlaybackMode.REPEAT_ONE
                PlaybackModePreferences.PLAYBACK_MODE_SHUFFLE -> PlaybackMode.REPEAT_SHUFFLE
                else -> PlaybackMode.REPEAT_UNSPECIFIED
            },
            playMusicId = it.playMusicId,
            playProgress = it.playProgress,
            playDuration = it.playDuration,
            globalLyricStyle = it.globalLyricStyle,
        )
    }

    suspend fun setNotShowGuide(notShowGuide: Boolean) {
        try {
            userPreferences.updateData {
                it.copy { this.notShowGuide = notShowGuide }
            }
        } catch (ioException: IOException) {
            Log.e("NiaPreferences", "Failed to update user preferences", ioException)
        }
    }

    suspend fun setNotShowTermsServiceAgreement(notShow: Boolean) {
        try {
            userPreferences.updateData {
                it.copy { this.notShowTermsServiceAgreement = notShow }
            }
        } catch (ioException: IOException) {
            Log.e("NiaPreferences", "Failed to update user preferences", ioException)
        }
    }

    suspend fun setSession(data: SessionPreferences) {
        try {
            userPreferences.updateData {
                it.copy { this.session = data }
            }
        } catch (ioException: IOException) {
            Log.e("NiaPreferences", "Failed to update user preferences", ioException)
        }
    }

    suspend fun setUser(data: UserPreferences) {
        try {
            userPreferences.updateData {
                it.copy { this.user = data }
            }
        } catch (ioException: IOException) {
            Log.e("NiaPreferences", "Failed to update user preferences", ioException)
        }
    }

    suspend fun logout() {
        try {
            userPreferences.updateData {
                it.copy {
                    this.session = SessionPreferences.newBuilder().build()
                    this.user = UserPreferences.newBuilder().build()
                }
            }
        } catch (ioException: IOException) {
            Log.e("NiaPreferences", "Failed to update user preferences", ioException)
        }
    }

    suspend fun setDynamicColorPreference(useDynamicColor: Boolean) {
        userPreferences.updateData {
            it.copy { this.useDynamicColor = useDynamicColor }
        }
    }


    suspend fun saveRecentSong(id: String, playProgress: Long, duration: Long) {
        userPreferences.updateData {
            it.copy {
                this.playMusicId = id
                this.playProgress = playProgress
                this.playDuration = duration
            }
        }
    }

    suspend fun setRepeatModel(data: PlaybackModePreferences) {
        userPreferences.updateData {
            it.copy {
                this.playRepeatMode = data
            }
        }
    }

    suspend fun setGlobalLyricTextColorIndex(data: Int) {
        userPreferences.updateData {
            it.copy {
                this.globalLyricStyle = it.globalLyricStyle.copy {
                    this.fontColorIndex = data
                }
            }
        }
    }

    suspend fun setGlobalLyricTextSize(data: Int) {
        userPreferences.updateData {
            it.copy {
                this.globalLyricStyle = it.globalLyricStyle.copy {
                    this.fontSize = data
                }
            }
        }
    }

    suspend fun setGlobalLyricViewPositionY(y: Int) {
        userPreferences.updateData {
            it.copy {
                this.globalLyricStyle = it.globalLyricStyle.copy {
                    this.positionY = y
                }
            }
        }
    }

    suspend fun setShowGlobalLyric(data: Boolean) {
        userPreferences.updateData {
            it.copy {
                this.globalLyricStyle = it.globalLyricStyle.copy {
                    this.show = data
                }
            }
        }
    }

    suspend fun setGlobalLyricLock(data: Boolean) {
        userPreferences.updateData {
            it.copy {
                this.globalLyricStyle = it.globalLyricStyle.copy {
                    this.lock = data
                }
            }
        }
    }
}