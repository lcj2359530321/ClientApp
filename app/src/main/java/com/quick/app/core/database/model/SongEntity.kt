package com.quick.app.core.database.model

import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.MimeTypes
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.quick.app.core.model.Song
import com.quick.app.core.model.from
import com.quick.app.util.Constant

/**
 * 音乐数据库模型
 *
 * 主要用来保存播放列表
 *
 * 参考 [com.quick.app.core.model.Song]
 */
@Entity(tableName = "song")
data class SongEntity(
    @PrimaryKey
    val id: String,

    val title: String,

    /**
     * 在音乐相对路径
     *
     * 本地扫描的音乐绝对路径
     */
    val uri: String = "",
    val icon: String = "",
    val album: String = "",
    val artist: String = "",
    val genre: String = "",

    @ColumnInfo(name = "lyric_style")
    val lyricStyle: Int = Constant.VALUE0,
    val lyric: String = "",

    @ColumnInfo(name = "track_number")
    val trackNumber: Int = 1,

    @ColumnInfo(name = "total_track_count")
    val totalTrackCount: Int = 1,

    @ColumnInfo(name = "play_list")
    val playList: Boolean = false,

    /**
     * 音乐来源
     */
    val source: Byte = 0,
) {
    fun toSong(): Song {
        return Song(
            id = id,
            title = title,
            uri = uri,
            icon = icon,
            album = album,
            artist = artist,
            genre = genre,
            lyricStyle = lyricStyle,
            lyric = lyric,
            trackNumber = trackNumber,
            totalTrackCount = totalTrackCount,
        )
    }
}


internal fun SongEntity.asMediaItem() = MediaItem.Builder()
    .apply {
        setMediaId(id)
        setUri(uri)
        setMimeType(MimeTypes.AUDIO_MPEG)
        setMediaMetadata(
            MediaMetadata.Builder()
                .from(this@asMediaItem)
                .build()
        )
    }.build()