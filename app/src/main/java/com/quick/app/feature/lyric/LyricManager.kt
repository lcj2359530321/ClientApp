package com.quick.app.feature.lyric

import android.content.Context
import com.google.common.base.Strings
import com.quick.app.core.data.repository.SongRepository
import com.quick.app.core.data.repository.UserDataRepository
import com.quick.app.core.media.MediaServiceConnection
import com.quick.app.feature.lyricparser.Lyric
import com.quick.app.feature.lyricparser.LyricParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LyricManager(
    val context: Context,
    val mediaServiceConnection: MediaServiceConnection,
    val songRepository: SongRepository,
    val userDataRepository: UserDataRepository,
) {
    val lyric = MutableStateFlow<Lyric>(Lyric.EMPTY)

    val nowPlaying = mediaServiceConnection.nowPlaying

    private val applicationScope = CoroutineScope(SupervisorJob())

    val showGlobalLyric = userDataRepository.userData.map {
        it.globalLyricStyle.show
    }.stateIn(
        scope = applicationScope,
        initialValue = false,
        started = SharingStarted.WhileSubscribed(5_000),
    )

    init {
        applicationScope.launch {
            nowPlaying
                .collectLatest {
                    prepareLyric(it.mediaId)
                }
        }
    }

    private suspend fun prepareLyric(id: String) {
        if (lyric.value.songId != id) {
            try {
                val songDetail = songRepository.songDetail(id)
                if (songDetail.isSucceeded) {
                    if (!Strings.isNullOrEmpty(songDetail.data!!.lyric)) {
                        lyric.value = LyricParser.parse(
                            songDetail.data!!.lyricStyle,
                            songDetail.data!!.lyric
                        ).copy(
                            songId = id,
                        )
                    } else {
                        lyric.value = Lyric.EMPTY
                    }
                } else {
                    lyric.value = Lyric.EMPTY
                }
            } catch (e: Exception) {
                //TODO 歌词加载错误处理
                lyric.value = Lyric.EMPTY
            }
        }
    }

}