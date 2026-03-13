package com.quick.app.feature.mediaplayer

import android.net.Uri
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.MimeTypes
import com.quick.app.core.data.repository.SongRepository
import com.quick.app.core.data.repository.UserDataRepository
import com.quick.app.core.datastore.PlaybackModePreferences
import com.quick.app.core.media.MediaServiceConnection
import com.quick.app.core.model.PlaybackMode
import com.quick.app.core.model.Song
import com.quick.app.core.model.from
import com.quick.app.feature.lyric.LyricManager
import com.quick.app.ui.BaseViewModel
import com.quick.app.util.ResourceUtil
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

open class BaseMediaPlayerViewModel(
    protected val mediaServiceConnection: MediaServiceConnection,
    protected val songRepository: SongRepository,
    protected val userDataRepository: UserDataRepository,
    protected val lyricManager: LyricManager,
) : BaseViewModel() {
    val showMusicListDialog = MutableStateFlow(false)

    val toMusicPlayer = mutableStateOf<Boolean>(false)

    val nowPlaying = mediaServiceConnection.nowPlaying
    val playbackState = mediaServiceConnection.playbackState
    val currentPosition = mediaServiceConnection.currentPosition.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = 0,
    )
    val playRepeatMode = userDataRepository.userData.map { it.playRepeatMode }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = PlaybackMode.REPEAT_LIST,
    )

    val datum = songRepository.getAllPlayList().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList(),
    )
    val recordRotation = MutableStateFlow(0F)

    val pauseRecordRotation = MutableStateFlow(false)

    fun onPauseRecordRotation(data: Boolean) {
        pauseRecordRotation.value = data
    }

    val showRecord = MutableStateFlow<Boolean>(true)
    fun toggleRecordAndLyric() {
        showRecord.value = !showRecord.value
    }

    val lyric = lyricManager.lyric

    init {
        collectCurrentPosition()

//        viewModelScope.launch {
//            nowPlaying
//                .collectLatest {
//                    if (lyric.value.songId != it.mediaId) {
//                        recordRotation.value = 0F
//                    }
//                }
//        }
    }

    private fun collectCurrentPosition() {
        viewModelScope.launch {
            //播放位置改变了
            mediaServiceConnection.currentPosition.collectLatest { position ->
                if (!pauseRecordRotation.value) {
                    //判断旋转角度边界
                    if (recordRotation.value > 360F) {
                        //就设置为0
                        recordRotation.value = 0F
                    }

                    //加上每次旋转的角度
                    recordRotation.value += ROTATION_PER

                }
            }
        }
    }

    public fun setMediasAndPlay(
        datum: List<Song>,
        index: Int,
        navigateToMusicPlayer: Boolean = false,
    ) {
        viewModelScope.launch {
            //将原来播放列表所有音乐playlist该为false
            songRepository.clearAllPlayList()

            val songs = datum.mapIndexed { index, song ->
                song.copy(
                    totalTrackCount = datum.size,
                    trackNumber = index,
                )
            }

            //保存播放列表
            songRepository.insertList(songs.map {
                it.toSongEntity()
            })

            //转为MediaItem
            val mediaItems = songs.map {
                MediaItem.Builder()
                    .apply {
                        setMediaId(it.id)
                        setUri(ResourceUtil.r2(it.uri))
                        setMimeType(MimeTypes.AUDIO_MPEG)
                        setMediaMetadata(
                            MediaMetadata.Builder()
                                .from(it)
                                .apply {
                                    setArtworkUri(Uri.parse(ResourceUtil.r2(it.icon))) // Used by ExoPlayer and Notification
                                    // Keep the original artwork URI for being included in Cast metadata object.
//                                        val extras = Bundle()
//                                        extras.putString(ORIGINAL_ARTWORK_URI_KEY, it.image)
//                                        setExtras(extras)
                                }
                                .build()
                        )
                    }.build()
            }.toList()

            mediaServiceConnection.setMediasAndPlay(mediaItems, index)

            toMusicPlayer.value = navigateToMusicPlayer
        }
    }

    fun clearMusicPlayer(): Unit {
        toMusicPlayer.value = false
    }

    fun onSeek(data: Float) {
        mediaServiceConnection.seekTo(data.toLong())
    }

    fun onSeek(data: Long) {
        mediaServiceConnection.seekTo(data)
    }

    fun onPreviousClick() {
        mediaServiceConnection.seekToPrevious()
    }

    fun onNextClick() {
        mediaServiceConnection.seekToNext()
    }

    fun onPlayOrPauseClick() {
        mediaServiceConnection.playOrPause()
    }

    fun onChangeRepeatModeClick() {
        viewModelScope.launch {
            var playRepeatMode = userDataRepository.userData.first().playRepeatMode.ordinal
            playRepeatMode++
            if (playRepeatMode > PlaybackMode.REPEAT_SHUFFLE.ordinal) {
                playRepeatMode = PlaybackMode.REPEAT_LIST.ordinal
            }
            userDataRepository.setRepeatModel(
                PlaybackModePreferences.forNumber(playRepeatMode)
            )

            mediaServiceConnection.setRepeatMode(playRepeatMode)
        }
    }

    private fun hideMusicListDialog() {
        showMusicListDialog.value = false
    }

    fun toggleShowMusicListDialog() {
        showMusicListDialog.value = !showMusicListDialog.value
    }

    fun onClearPlayListClick() {
        hideMusicListDialog()

        mediaServiceConnection.clearAll()
        viewModelScope.launch {
            songRepository.deleteAll()
            finish.value = true
        }
    }

    fun onItemPlayListClick(index: Int) {
        playIndex(index)
    }

    fun onItemMusicDeleteClick(index: Int) {
        mediaServiceConnection.delete(index)
        viewModelScope.launch {
            songRepository.delete(datum.value[index])
            if (datum.value.size - 1 <= 0) {
                hideMusicListDialog()
                finish.value = true
            }
        }
    }

    fun playIndex(index: Int) {
        mediaServiceConnection.playIndex(index)
    }

    fun pause() {
        mediaServiceConnection.pause()
    }

    companion object {
        /**
         * 每16毫秒旋转的角度
         * 16毫秒是通过
         * 每秒60帧计算出来的
         * 也就是1000/60=16
         * 也就是说绘制一帧要在16毫秒中完成
         * 不然就能感觉卡顿
         * 用秒表测转一圈的时间
         */
        private const val ROTATION_PER = 0.2304f
    }
}