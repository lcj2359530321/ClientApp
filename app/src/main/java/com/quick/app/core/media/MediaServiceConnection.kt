package com.quick.app.core.media

import android.content.ComponentName
import android.content.Context
import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.quick.app.core.data.repository.SongRepository
import com.quick.app.core.data.repository.UserDataRepository
import com.quick.app.core.database.model.SongEntity
import com.quick.app.core.database.model.asMediaItem
import com.quick.app.core.model.PlaybackMode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.guava.await
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration.Companion.milliseconds

/**
 * 媒体服务连接器
 *
 * 主要用来控制媒体服务
 */
class MediaServiceConnection(
    context: Context,
    serviceComponent: ComponentName,
    private val songRepository: SongRepository,
    private val userDataRepository: UserDataRepository,
) {
    private var mediaController: MediaController? = null

    private val coroutineContext: CoroutineContext = Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext + SupervisorJob())

    val nowPlaying = MutableStateFlow<MediaItem>(NOTHING_PLAYING)
    val playbackState = MutableStateFlow<PlaybackState>(EMPTY_PLAYBACK_STATE)
    val currentPosition = MutableStateFlow<Long>(0)

    val networkFailure = MutableStateFlow<Boolean>(false)

    var publishProgressJob: Job? = null

    private val playerListener: PlayerListener = PlayerListener()

    private lateinit var currentPlayer: Player

    private var lastPosition: Long = 0


    private inner class PlayerListener : Player.Listener {

        override fun onEvents(player: Player, events: Player.Events) {
            currentPlayer = player
            if (events.contains(Player.EVENT_PLAY_WHEN_READY_CHANGED)
                || events.contains(Player.EVENT_PLAYBACK_STATE_CHANGED)
                || events.contains(Player.EVENT_MEDIA_ITEM_TRANSITION)
            ) {
                updatePlaybackState(player)
                if (player.playbackState != Player.STATE_IDLE) {
                    networkFailure.value = false
                }
            }
            if (events.contains(Player.EVENT_MEDIA_METADATA_CHANGED)
                || events.contains(Player.EVENT_MEDIA_ITEM_TRANSITION)
                || events.contains(Player.EVENT_PLAY_WHEN_READY_CHANGED)
            ) {

                updateNowPlaying(player)

//                player.currentMediaItem?.mediaId?.let {
//                    if (lyric.value.songId != it && Strings.isNullOrEmpty(lyric.value.lyric)) {
//                        scope.launch {
//                            val songDetail = songRepository.songDetail(it)
//                            if (songDetail.isSucceeded) {
//                                lyric.value = songDetail.data!!.lyric
//                            }
//                        }
//                    }
//                }
//
//                lyric.value.songId !=
//                prepareLyric(player.currentMediaItem.mediaId)
            }
        }

        override fun onPlayerErrorChanged(error: PlaybackException?) {
            error?.printStackTrace()
            when (error?.errorCode) {
                PlaybackException.ERROR_CODE_IO_BAD_HTTP_STATUS,
                PlaybackException.ERROR_CODE_IO_INVALID_HTTP_CONTENT_TYPE,
                PlaybackException.ERROR_CODE_IO_CLEARTEXT_NOT_PERMITTED,
                PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_FAILED,
                PlaybackException.ERROR_CODE_IO_NETWORK_CONNECTION_TIMEOUT -> {
//                    networkFailure.value = true
                }
            }
        }

    }

    init {
        scope.launch {
            //创建音频会话令牌
            val sessionToken =
                SessionToken(context, serviceComponent)

            //创建媒体控制器
            //用来在外界控制音频播放器
            mediaController =
                MediaController
                    .Builder(context, sessionToken)
                    .setListener(MediaControllerListener())
                    .buildAsync().await()

            mediaController!!.addListener(playerListener)

            //初始化播放列表
            initPlayList()
        }
    }

    private suspend fun initPlayList() {
        val datum = songRepository.getAllPlayListAsync()
        if (datum.isEmpty()) return

        val userData = userDataRepository.userData.first()
        val startIndex = datum.indexOfFirst { it.id == userData.playMusicId }

        mediaController?.run {
            setMedias(datum.map(SongEntity::asMediaItem), startIndex, userData.playProgress)

            prepare()
        }
    }

    private fun updatePlaybackState(player: Player) {
        playbackState.value =
            PlaybackState(
                player.playbackState,
                player.playWhenReady,
                player.duration
            )
        currentPosition.value = player.currentPosition

        checkPublishProgressTask()
    }

    private fun checkPublishProgressTask() {   //TODO 1111111
        if (playbackState.value.isPlaying) {
            if (publishProgressJob != null) {
                return
            }

            //每16毫秒更新一次进度
            publishProgressJob = scope.launch {
                while (playbackState.value.isPlaying) {
                    currentPosition.value = mediaController?.currentPosition ?: 0
//                    Timber.d("MusicServiceConnection publish currentPosition ${currentPosition.value}")
                    delay(16.milliseconds)
                    //间隔大于2秒后保存播放进度
                    if (currentPosition.value - lastPosition >= 2000) {
                        userDataRepository.saveRecentSong(
                            mediaController!!.currentMediaItem!!.mediaId,
                            currentPosition.value,
                            playbackState.value.duration,
                        )
//                        Timber.d("MusicServiceConnection save currentPosition ${currentPosition.value}")
                        lastPosition = currentPosition.value
                    }
                }
            }
        } else {
            publishProgressJob?.cancel()
            publishProgressJob = null
        }
    }

    private fun updateNowPlaying(player: Player) {
        val mediaItem = player.currentMediaItem ?: MediaItem.EMPTY
        if (mediaItem == MediaItem.EMPTY) {
            return
        }
        nowPlaying.value = mediaItem

//        playbackState.value = PlaybackState(
//            duration = mediaController?.duration ?: 0
//        )
//        currentPosition.value = mediaController?.currentPosition ?: 0

        // The current media item from the CastPlayer may have lost some information.
//        val mediaItemFuture = mediaController!!.getItem(mediaItem.mediaId)
//        mediaItemFuture.addListener(
//            Runnable {
//                val fullMediaItem = mediaItemFuture.get().value ?: return@Runnable
//                nowPlaying.postValue(
//                    mediaItem.buildUpon().setMediaMetadata(fullMediaItem.mediaMetadata).build()
//                )
//            },
//            MoreExecutors.directExecutor()
//        )
    }

    fun setMediasAndPlay(datum: List<MediaItem>, startIndex: Int) {
        setMedias(datum, startIndex)

        mediaController!!.prepare()
        mediaController!!.play()
    }

    fun setMedias(datum: List<MediaItem>, startIndex: Int, startPositionMs: Long = 0) {
        mediaController!!.setMediaItems(
            datum, startIndex, startPositionMs
        )
    }

    fun playOrPause() {
        mediaController?.run {
            if (isPlaying) {
                pause()
            } else {
                play()
            }
        }
    }

    fun seekToPrevious() {
        mediaController?.run {
            seekToPrevious()
            play()
        }
    }

    fun seekToNext() {
        mediaController?.run {
            seekToNext()
            play()
        }
    }


    fun pause() {
        mediaController?.run {
            if (isPlaying) {
                pause()
            }
        }
    }

    fun seekTo(data: Long) {
        mediaController?.run {
            seekTo(data)
            if (!isPlaying) {
                play()
            }
        }
    }

    private inner class MediaControllerListener : MediaController.Listener {
        override fun onDisconnected(controller: MediaController) {
            release()
        }
    }

    fun release() {
        nowPlaying.value = NOTHING_PLAYING
        mediaController?.let {
            it.removeListener(playerListener)
            it.release()
        }
        instance = null
    }

    fun setRepeatMode(playRepeatMode: Int) {
        if (playRepeatMode == PlaybackMode.REPEAT_SHUFFLE.ordinal) {
            //随机循环
            //https://developer.android.com/media/media3/exoplayer/playlists?hl=zh-cn
            currentPlayer.shuffleModeEnabled = true

            //整个播放列表在无限循环中重复播放。
            currentPlayer.repeatMode = Player.REPEAT_MODE_ALL
        } else {
            currentPlayer.shuffleModeEnabled = false
            currentPlayer.repeatMode = if (playRepeatMode == PlaybackMode.REPEAT_ONE.ordinal) {
                Player.REPEAT_MODE_ONE
            } else
                Player.REPEAT_MODE_ALL
        }
    }

    fun playIndex(index: Int) {
        Log.d("TAG", "playPosition: $index")
        mediaController?.run {
            seekTo(index, 0L)
            play()
        }
    }

    fun delete(index: Int) {
        mediaController?.removeMediaItem(index)
    }

    fun clearAll() {
        mediaController?.clearMediaItems()
    }

    companion object {
        private const val TAG = "MediaServiceConnection"

        @Volatile
        private var instance: MediaServiceConnection? = null

        /**
         * 获取单利对象
         */
        fun getInstance(
            context: Context, serviceComponent: ComponentName,
            songRepository: SongRepository,
            userDataRepository: UserDataRepository,
        ) =
            instance ?: synchronized(this) {
                instance ?: MediaServiceConnection(
                    context,
                    serviceComponent,
                    songRepository,
                    userDataRepository,
                ).also {
                    instance = it
                }
            }

        fun destroyInstance() {
            instance?.release()
        }
    }
}