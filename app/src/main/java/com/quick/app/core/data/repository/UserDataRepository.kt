package com.quick.app.core.data.repository

import com.quick.app.core.datastore.PlaybackModePreferences
import com.quick.app.core.datastore.SessionPreferences
import com.quick.app.core.datastore.UserPreferences
import com.quick.app.core.model.UserData
import kotlinx.coroutines.flow.Flow

interface UserDataRepository {
    val userData: Flow<UserData>

    suspend fun setNotShowGuide(notShowGuide: Boolean)
    suspend fun setNotShowTermsServiceAgreement(notShow: Boolean)

    suspend fun setSession(data: SessionPreferences?)
    suspend fun setUser(data: UserPreferences?)
    suspend fun logout()

    suspend fun setDynamicColorPreference(useDynamicColor: Boolean)

    suspend fun saveRecentSong(
        id: String,
        currentPosition: Long,
        duration: Long,
    )

    suspend fun setRepeatModel(
        data: PlaybackModePreferences
    )

    suspend fun setGlobalLyricTextColorIndex(data: Int)
    suspend fun setGlobalLyricTextSize(data: Int)
    suspend fun setGlobalLyricViewPositionY(y: Int)
    suspend fun setShowGlobalLyric(data: Boolean)
    suspend fun setGlobalLyricLock(data: Boolean)
}