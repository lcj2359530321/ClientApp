package com.quick.app.feature.main

import com.quick.app.MyApplication
import com.quick.app.core.data.repository.SongRepository
import com.quick.app.core.data.repository.UserDataRepository
import com.quick.app.core.media.MediaServiceConnection
import com.quick.app.feature.lyric.LyricManager
//import com.quick.app.feature.lyric.LyricManager
import com.quick.app.feature.mediaplayer.BaseMediaPlayerViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel
@Inject
constructor(
    songRepository: SongRepository,
    mediaServiceConnection: MediaServiceConnection,
    userDataRepository: UserDataRepository,
    lyricManager: LyricManager,
) : BaseMediaPlayerViewModel(
    mediaServiceConnection,
    songRepository,
    userDataRepository,
    lyricManager,
) {
    val isShowConfirmLogoutDialog = MutableStateFlow<Boolean>(false)

    fun dismissConfirmLogoutDialog() {
        isShowConfirmLogoutDialog.value = false
    }

    fun showConfirmLogoutDialog() {
        isShowConfirmLogoutDialog.value = true
    }

    fun onLogoutClick() {
        MyApplication.instance.logout()
    }


}