package com.quick.app.core.media

import androidx.media3.common.MediaItem
import androidx.media3.common.Player


data class PlaybackState(
    private val playbackState: Int = Player.STATE_IDLE,
    private val playWhenReady: Boolean = false,
    val duration: Long = 0
) {
    val isPlaying: Boolean
        get() {
            return (playbackState == Player.STATE_BUFFERING
                    || playbackState == Player.STATE_READY)
                    && playWhenReady
        }
    val durationFormat: Long
        get() {
            return if (duration > 0) {
                duration
            } else {
                0
            }
        }
}

@Suppress("PropertyName")
val EMPTY_PLAYBACK_STATE: PlaybackState = PlaybackState()

@Suppress("PropertyName")
val NOTHING_PLAYING: MediaItem = MediaItem.EMPTY