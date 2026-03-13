package com.quick.app.core.media

import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture

/**
 * 参考：https://developer.android.com/media/media3/session/background-playback?hl=zh-cn
 */
class MediaService : MediaSessionService() {
    private var mediaSession: MediaSession? = null

    private val replaceableForwardingPlayer: ReplaceableForwardingPlayer by lazy {
        ReplaceableForwardingPlayer(player)
    }

    override fun onCreate() {
        super.onCreate()
        mediaSession = MediaSession.Builder(this, replaceableForwardingPlayer)
            .setCallback(getCallback())
            .build()
    }

    open inner class MediaServiceCallback : MediaSession.Callback {

    }


    open fun getCallback(): MediaSession.Callback {
        return MediaServiceCallback()
    }


    override fun onGetSession(controllerInfo: MediaSession.ControllerInfo): MediaSession? {
        return mediaSession
    }

    override fun onDestroy() {
        mediaSession?.run {
            player.release()
            release()
            mediaSession = null
        }
        super.onDestroy()
    }

    /**
     * 配置ExoPlayer处理音频焦点，详情查看[ExoPlayer.Builder.setAudioAttributes]
     */
    private val player: Player by lazy {
        val player = ExoPlayer.Builder(this).build().apply {
            setAudioAttributes(audioAttributes, true)
            setHandleAudioBecomingNoisy(true)
            addListener(playerEventListener)
        }
//        player.addAnalyticsListener(EventLogger(null, "exoplayer-uamp"))
        player
    }

    private val playerEventListener = PlayerEventListener()

    /** ExoPlayer事件监听器 */
    private inner class PlayerEventListener : Player.Listener {
        override fun onEvents(player: Player, events: Player.Events) {
            if (events.contains(Player.EVENT_POSITION_DISCONTINUITY)
                || events.contains(Player.EVENT_MEDIA_ITEM_TRANSITION)
                || events.contains(Player.EVENT_PLAY_WHEN_READY_CHANGED)
            ) {
                currentMediaItemIndex = player.currentMediaItemIndex
//                saveRecentSongToStorage()
            }
        }

        override fun onPlayerError(error: PlaybackException) {
//            var message = R.string.generic_error;
//            Log.e(TAG, "Player error: " + error.errorCodeName + " (" + error.errorCode + ")", error);
//            if (error.errorCode == PlaybackException.ERROR_CODE_IO_BAD_HTTP_STATUS
//                || error.errorCode == PlaybackException.ERROR_CODE_IO_FILE_NOT_FOUND) {
//                message = R.string.error_media_not_found;
//            }
//            Toast.makeText(
//                applicationContext,
//                message,
//                Toast.LENGTH_LONG
//            ).show()

        }
    }

    private var currentMediaItemIndex: Int = 0

}