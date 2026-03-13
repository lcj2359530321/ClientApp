package com.quick.app.feature.musicappwidget

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import androidx.glance.ColorFilter
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.action.ActionParameters
import androidx.glance.action.action
import androidx.glance.action.actionParametersOf
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.LinearProgressIndicator
import androidx.glance.appwidget.action.actionStartService
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.ContentScale
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.material3.ColorProviders
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.quick.app.MainActivity
import com.quick.app.R
import com.quick.app.core.design.theme.DarkColors
import com.quick.app.core.design.theme.LightColors
import com.quick.app.core.design.theme.SpaceExtraOuter
import com.quick.app.core.design.theme.SpaceExtraSmall2
import com.quick.app.core.design.theme.SpaceMedium
import com.quick.app.core.design.theme.SpaceOuter
import com.quick.app.core.media.MediaServiceConnection
import com.quick.app.feature.lyric.LyricManager
import com.quick.app.util.Constant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MusicAppWidget(
    private val mediaServiceConnection: MediaServiceConnection,
    private val lyricManager: LyricManager,
) :
    GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        // In this method, load data needed to render the AppWidget.
        // Use `withContext` to switch to another thread for long running
        // operations.
        provideContent {
            // create your AppWidget here
            GlanceTheme(colors = GlanceColors) {
                MyContent()
            }
        }
    }

    @Composable
    private fun MyContent() {
        val context = LocalContext.current

        val nowPlaying by mediaServiceConnection.nowPlaying.collectAsState()
        val playbackState by mediaServiceConnection.playbackState.collectAsState()
        val currentPosition by mediaServiceConnection.currentPosition.collectAsState()

        val showGlobalLyricStyle by lyricManager.showGlobalLyric.collectAsState()

        val currentPercentage = if (playbackState.durationFormat == 0L)
            0f
        else
            currentPosition * 1f / playbackState.durationFormat

        var iconBitmap by remember {
            mutableStateOf<Bitmap?>(null)
        }

        LaunchedEffect(key1 = nowPlaying.mediaId) {
            withContext(Dispatchers.IO) {

//        iconBitmap =
//            BitmapFactory.decodeResource(context.resources, R.drawable.shortcut_local_music)
//                BitmapFactory.decodeFile()
                iconBitmap = loadBitmap(context, nowPlaying.mediaMetadata.artworkUri.toString())
            }
        }

        Box(
            contentAlignment = Alignment.TopEnd,
            modifier = GlanceModifier.fillMaxSize()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalAlignment = Alignment.CenterVertically,
                modifier = GlanceModifier.fillMaxSize()
                    .background(ImageProvider(R.drawable.music_player_background)),

                ) {

                //封面和歌曲信息
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = GlanceModifier.fillMaxWidth()
                        .padding(horizontal = SpaceOuter, vertical = SpaceExtraSmall2)
                ) {
                    Box(
                        modifier = GlanceModifier.size(100.dp)
                    ) {
                        Image(
                            provider = if (iconBitmap != null) ImageProvider(iconBitmap!!)
                            else ImageProvider(
                                R.drawable.placeholder
                            ),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = GlanceModifier.size(100.dp)
                                .clickable(
                                    actionStartActivity<MainActivity>(
                                        actionParametersOf(extraMediaId to nowPlaying.mediaId)
                                    )
                                ),
                        )
                    }
                    GlanceSpaceOuterWidth()
                    Column(
                        modifier = GlanceModifier.fillMaxWidth()
                    ) {
                        Text(
                            text = nowPlaying.mediaMetadata.title?.toString()
                                ?: context.getString(R.string.no_music_playe),
                            style = TextStyle(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = GlanceTheme.colors.onPrimary
                            ),
                            modifier = GlanceModifier
                        )
                        Spacer(modifier = GlanceModifier.height(SpaceOuter))
                        Text(
                            text =
                                if (nowPlaying.mediaId.isBlank())
                                    ""
                                else
                                    "${nowPlaying.mediaMetadata.artist} - ${nowPlaying.mediaMetadata.albumTitle}",
                            style = TextStyle(
                                fontSize = 14.sp, color = ColorProvider(Color.LightGray)
                            ),
                            modifier = GlanceModifier
                        )
                    }
                }

                Spacer(modifier = GlanceModifier.height(SpaceExtraOuter))

                //进度条
                LinearProgressIndicator(
                    progress = currentPercentage, // 取值范围0-1
                    modifier = GlanceModifier.padding(horizontal = SpaceOuter).fillMaxWidth()
                        .height(1.dp),
                    color = ColorProvider(Color.LightGray),
                    backgroundColor = ColorProvider(Color.Gray)
                )

                Spacer(modifier = GlanceModifier.height(SpaceExtraOuter))

                //控制按钮
                Row(
                    modifier = GlanceModifier.fillMaxWidth()
                ) {
                    Image(
                        provider = ImageProvider(R.drawable.music_collect),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = GlanceModifier.height(40.dp).defaultWeight()
                            .clickable(actionStartActivity<MainActivity>()),
                    )

                    Image(
                        provider = ImageProvider(R.drawable.music_previous),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = GlanceModifier.height(40.dp).defaultWeight().clickable(action {
                            mediaServiceConnection.seekToPrevious()
                        }),
                    )

                    Image(
                        provider = ImageProvider(
                            if (playbackState.isPlaying) R.drawable.music_pause
                            else R.drawable.music_play
                        ),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = GlanceModifier.size(40.dp).clickable(action {
                            mediaServiceConnection.playOrPause()
                        }),
                    )

                    Image(
                        provider = ImageProvider(R.drawable.music_next),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = GlanceModifier.height(40.dp).defaultWeight().clickable(action {
                            mediaServiceConnection.seekToNext()
                        }),
                    )


                    Image(
                        provider = ImageProvider(
                            if (showGlobalLyricStyle)
                                R.drawable.music_lyric_selected
                            else
                                R.drawable.music_lyric
                        ),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = GlanceModifier.height(40.dp).defaultWeight()
//                            .clickable(
//                                actionStartService(
//                                    Intent(context, MusicControlService::class.java).apply {
//                                        putExtra(
//                                            Constant.EXTRA_GLOBAL_LYRIC, //标记要显示全局桌面歌词
//                                            nowPlaying.mediaId
//                                        )
//                                    },
//                                    isForegroundService = true, //启动前台服务
//                                )
////                                action {
//
//
////                                    if (Settings.canDrawOverlays(context)) {
////                                        //有权限
////                                    }else{
////
////                                    }
////                                    musicServiceConnection.playOrPause()
////                                }
//                            ),
                    )
                }
            }

            //右上角图片
            Image(
                provider = ImageProvider(R.drawable.logo_transparent),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                colorFilter = ColorFilter.tint(ColorProvider(Color.LightGray)),
                modifier = GlanceModifier
                    .padding(
                        top = SpaceMedium,
                        end = SpaceMedium
                    )
                    .size(40.dp)
                    .clickable(actionStartActivity<MainActivity>()),
            )
        }
    }
}


@Composable
fun GlanceSpaceMediumWidth(): Unit {
    Spacer(modifier = GlanceModifier.width(SpaceMedium))
}

@Composable
fun GlanceSpaceOuterWidth(): Unit {
    Spacer(modifier = GlanceModifier.width(SpaceOuter))
}

val GlanceColors = ColorProviders(
    light = LightColors,
    dark = DarkColors
)

suspend fun loadBitmap(context: Context, imageUrl: String): Bitmap? {
    val imageLoader = ImageLoader(context)
    val request = ImageRequest.Builder(context).data(imageUrl)
//        .transformations(
//            RoundedCornersTransformation(5f)
//        )
        .build()

    val result = imageLoader.execute(request)

    return if (result is SuccessResult) {
        result.drawable.toBitmap()
    } else {
        null
    }
}

private val extraMediaId = ActionParameters.Key<String>(
    Constant.EXTRA_MEDIA_ID
)

private val extraLyric = ActionParameters.Key<String>(
    Constant.EXTRA_GLOBAL_LYRIC
)