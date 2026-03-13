package com.quick.app.core.model

import android.net.Uri
import androidx.media3.common.MediaMetadata
import com.quick.app.core.database.model.SongEntity

import com.quick.app.util.Constant
import com.quick.app.util.ResourceUtil
import kotlinx.serialization.Serializable
import androidx.core.net.toUri

/**
 * 歌曲

 */
@Serializable
data class Song(
    /**
     * 歌曲的唯一标识符
     */
    val id: String,

    /**
     * 歌曲标题
     */
    val title: String,

    /**
     * 音频文件
     */
    val uri: String,

    /**
     * 网络封面
     */
    val icon: String = "",

    /**
     * 专辑的名称
     */
    val album: String,

    /**
     * 艺术家的名称
     */
    val artist: String,

    /**
     * 歌曲的流派
     */
    val genre: String,

    val lyricStyle: Int = Constant.VALUE0,
    val lyric: String = "",

    /**
     * 歌曲的轨道号
     *
     * 从1开始
     */
    val trackNumber: Int = 1,

    /**
     * 专辑中的歌曲总数
     */
    val totalTrackCount: Int = 1,

//    /**
//     * 歌曲的持续时间
//     *
//     * 毫秒
//     */
//    val duration: Int=0,
) {
    fun toSongEntity(): SongEntity {
        return SongEntity(
            id = id,
            title = title,
            uri = ResourceUtil.r2(uri),
            icon = icon,
            album = album,
            artist = artist,
            genre = genre,
            lyricStyle = lyricStyle,
            lyric = lyric,
            trackNumber = trackNumber,
            totalTrackCount = totalTrackCount,
            playList = true,
        )
    }
}

fun MediaMetadata.Builder.from(data: Song): MediaMetadata.Builder {
    setTitle(data.title)
    setDisplayTitle(data.title)
    setArtist(data.artist)
    setAlbumTitle(data.album)
    setGenre(data.genre)
    setTrackNumber(data.trackNumber)
    setTotalTrackCount(data.totalTrackCount)
    setIsBrowsable(false)
    setMediaType(MediaMetadata.MEDIA_TYPE_MUSIC)
    setIsPlayable(true)
    // The duration from the JSON is given in seconds, but the rest of the code works in
    // milliseconds. Here's where we convert to the proper units.
//    val durationMs = TimeUnit.SECONDS.toMillis(data.duration)
//    val bundle = Bundle()
//    bundle.putLong("durationMs", durationMs)
    return this
}


fun MediaMetadata.Builder.from(data: SongEntity): MediaMetadata.Builder {
    setTitle(data.title)
    setDisplayTitle(data.title)
    setArtist(data.artist)
    setAlbumTitle(data.album)
    setGenre(data.genre)
    setTrackNumber(data.trackNumber)
    setTotalTrackCount(data.totalTrackCount)
    setIsBrowsable(false)
    setMediaType(MediaMetadata.MEDIA_TYPE_MUSIC)
    setIsPlayable(true)
    setArtworkUri(ResourceUtil.r2(data.icon).toUri())
    return this
}