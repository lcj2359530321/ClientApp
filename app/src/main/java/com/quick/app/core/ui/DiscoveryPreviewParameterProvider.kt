import DiscoveryPreviewParameterData.SONGS
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.quick.app.core.model.Song
import com.quick.app.util.Constant

/**
 * 提供发现界面预览数据
 */
class DiscoveryPreviewParameterProvider : PreviewParameterProvider<List<Song>> {
    override val values: Sequence<List<Song>> = sequenceOf(SONGS)
}

object DiscoveryPreviewParameterData {
    val SONG = Song(
        id = "1",
        title = "爱的代价",
        uri = "assets/s2.mp3",
        icon = "assets/s2.jpg",
        album = "不舍",
        artist = "李宗盛",
        genre = "流行",
        lyricStyle = Constant.VALUE0,
        lyric = "",
        trackNumber = 1,
        totalTrackCount = 2,
    )
    val SONGS = listOf(
        SONG,
        Song(
            id = "2",
            title = "爱拼才会赢",
            uri = "assets/s1.mp3",
            icon = "assets/s1.jpg",
            album = "唱遍市场",
            artist = "叶启田",
            genre = "流行",
            lyricStyle = Constant.VALUE10,
            lyric = "",
            trackNumber = 2,
            totalTrackCount = 2,
        ),
        SONG,
        Song(
            id = "2",
            title = "爱拼才会赢",
            uri = "assets/s1.mp3",
            icon = "assets/s1.jpg",
            album = "唱遍市场",
            artist = "叶启田",
            genre = "流行",
            lyricStyle = Constant.VALUE10,
            lyric = "",
            trackNumber = 2,
            totalTrackCount = 2,
        ),
        SONG,
        Song(
            id = "2",
            title = "爱拼才会赢",
            uri = "assets/s1.mp3",
            icon = "assets/s1.jpg",
            album = "唱遍市场",
            artist = "叶启田",
            genre = "流行",
            lyricStyle = Constant.VALUE10,
            lyric = "",
            trackNumber = 2,
            totalTrackCount = 2,
        ),
        SONG,
        Song(
            id = "2",
            title = "爱拼才会赢",
            uri = "assets/s1.mp3",
            icon = "assets/s1.jpg",
            album = "唱遍市场",
            artist = "叶启田",
            genre = "流行",
            lyricStyle = Constant.VALUE10,
            lyric = "",
            trackNumber = 2,
            totalTrackCount = 2,
        ),
        SONG,
        Song(
            id = "2",
            title = "爱拼才会赢",
            uri = "assets/s1.mp3",
            icon = "assets/s1.jpg",
            album = "唱遍市场",
            artist = "叶启田",
            genre = "流行",
            lyricStyle = Constant.VALUE10,
            lyric = "",
            trackNumber = 2,
            totalTrackCount = 2,
        ),
        SONG,
        Song(
            id = "2",
            title = "爱拼才会赢",
            uri = "assets/s1.mp3",
            icon = "assets/s1.jpg",
            album = "唱遍市场",
            artist = "叶启田",
            genre = "流行",
            lyricStyle = Constant.VALUE10,
            lyric = "",
            trackNumber = 2,
            totalTrackCount = 2,
        ),
        SONG,
        Song(
            id = "2",
            title = "爱拼才会赢",
            uri = "assets/s1.mp3",
            icon = "assets/s1.jpg",
            album = "唱遍市场",
            artist = "叶启田",
            genre = "流行",
            lyricStyle = Constant.VALUE10,
            lyric = "",
            trackNumber = 2,
            totalTrackCount = 2,
        ),
    )
}