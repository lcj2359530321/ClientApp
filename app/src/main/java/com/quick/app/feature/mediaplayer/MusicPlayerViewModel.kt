package com.quick.app.feature.mediaplayer

import com.quick.app.core.data.repository.SongRepository
import com.quick.app.core.data.repository.UserDataRepository
import com.quick.app.core.media.MediaServiceConnection
import com.quick.app.feature.lyric.LyricManager
//import com.quick.app.feature.lyric.LyricManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MusicPlayViewModel @Inject constructor(
    mediaServiceConnection: MediaServiceConnection,
    songRepository: SongRepository,
    userDataRepository: UserDataRepository,
    lyricManager: LyricManager,
) : BaseMediaPlayerViewModel(
    mediaServiceConnection,
    songRepository,
    userDataRepository,
    lyricManager
) {

}