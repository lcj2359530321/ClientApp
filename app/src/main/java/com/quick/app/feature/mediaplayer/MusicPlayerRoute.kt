package com.quick.app.feature.mediaplayer

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.MediaItem
import com.quick.app.R
import com.quick.app.core.database.model.SongEntity
import com.quick.app.core.design.theme.MyAppTheme
import com.quick.app.core.design.theme.SpaceLarge
import com.quick.app.core.design.theme.SpaceOuter
import com.quick.app.core.design.theme.SpacerOuterHeight
import com.quick.app.core.extension.asFormatTimeString
import com.quick.app.core.extension.clickableNoRipple
import com.quick.app.core.media.EMPTY_PLAYBACK_STATE
import com.quick.app.core.media.PlaybackState
import com.quick.app.core.model.PlaybackMode
import com.quick.app.feature.lyric.MyLyricList
import com.quick.app.feature.lyricparser.Lyric
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun MusicPlayerRoute(
    finishPage: () -> Unit,
    viewModel: MusicPlayViewModel = hiltViewModel()
) {
    val nowPlaying by viewModel.nowPlaying.collectAsStateWithLifecycle()
    val playbackState by viewModel.playbackState.collectAsStateWithLifecycle()
    val currentPosition by viewModel.currentPosition.collectAsStateWithLifecycle()
    val playRepeatMode by viewModel.playRepeatMode.collectAsStateWithLifecycle()
    val showMusicListDialog by viewModel.showMusicListDialog.collectAsStateWithLifecycle()
    val datum by viewModel.datum.collectAsStateWithLifecycle()
    val recordRotation by viewModel.recordRotation.collectAsStateWithLifecycle()
    val pauseRecordRotation by viewModel.pauseRecordRotation.collectAsStateWithLifecycle()
    val showRecord by viewModel.showRecord.collectAsStateWithLifecycle()
    val lyric by viewModel.lyric.collectAsStateWithLifecycle()

    MusicPlayerScreen(
        finishPage = finishPage,
        nowPlaying = nowPlaying,
        playbackState = playbackState,
        currentPosition = currentPosition,
        onSeek = viewModel::onSeek,
        playRepeatMode = playRepeatMode,
        onPreviousClick = viewModel::onPreviousClick,
        onPlayOrPauseClick = viewModel::onPlayOrPauseClick,
        onNextClick = viewModel::onNextClick,
        onChangeRepeatModeClick = viewModel::onChangeRepeatModeClick,
        onMusicListClick = viewModel::toggleShowMusicListDialog,
        datum = datum,
        recordRotation = recordRotation,
        pauseRecordRotation = pauseRecordRotation,
        onPauseRecordRotation = viewModel::onPauseRecordRotation,
        onScrollPage = viewModel::playIndex,
        toggleRecordAndLyric = viewModel::toggleRecordAndLyric,
        showRecord = showRecord,
        onLyricPlayClick = viewModel::onSeek,
        lyric = lyric,
    )

    if (showMusicListDialog) {
        MyMusicListDialog(
            datum = datum,
            nowPlaying = nowPlaying,
            onClearPlayListClick = viewModel::onClearPlayListClick,
            onItemPlayListClick = viewModel::onItemPlayListClick,
            onItemMusicDeleteClick = viewModel::onItemMusicDeleteClick,
            onDismissRequest = {
                viewModel.toggleShowMusicListDialog()
            }
        )
    }

    LaunchedEffect(viewModel.finish.value) {
        if (viewModel.finish.value) {
            finishPage()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicPlayerScreen(
    finishPage: () -> Unit = {},
    nowPlaying: MediaItem = MediaItem.EMPTY,
    playbackState: PlaybackState = EMPTY_PLAYBACK_STATE,
    currentPosition: Long = 0,
    onSeek: (Float) -> Unit = { },
    playRepeatMode: PlaybackMode = PlaybackMode.REPEAT_LIST,
    onChangeRepeatModeClick: () -> Unit = {},
    onPreviousClick: () -> Unit = {},
    onPlayOrPauseClick: () -> Unit = {},
    onNextClick: () -> Unit = {},
    onMusicListClick: () -> Unit = {},
    datum: List<SongEntity> = listOf(),
    onScrollPage: (Int) -> Unit = {},
    recordRotation: Float = 0F,
    onPauseRecordRotation: (Boolean) -> Unit = {},
    toggleRecordAndLyric: () -> Unit = {},
    showRecord: Boolean = true,
    pauseRecordRotation: Boolean = false,
    onLyricPlayClick: (Long) -> Unit = {},
    lyric: Lyric = Lyric.EMPTY,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(onClick = finishPage) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = null
                        )
                    }
                },
                title = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = nowPlaying.mediaMetadata.title.toString(),
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White,
                        )
                        Text(
                            text = nowPlaying.mediaMetadata.artist.toString(),
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White,
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        BoxWithConstraints(
            modifier = Modifier.fillMaxSize()
        ) {
            // 使用 BoxWithConstraints 的 maxWidth 来获取父布局的最大宽度
            //103/320；指针原点到底部状态栏
            val recordThumbMarginTop = maxWidth * 0.31875f

            //黑胶唱片背景距离顶部的距离
            //156/320
            val recordBackgroundMarginTop = maxWidth * 0.4875f

            //黑胶唱片距离顶部的距离
            val recordMarginTop = maxWidth * 0.49f

            ConstraintLayout(modifier = Modifier.fillMaxSize()) {
                val (musicPlayerBackground, recordThumb, recordBackground) = createRefs()

                //歌曲封面背景图片
                BackgroundContent(
                    data = "",
                    modifier = Modifier
                        .fillMaxSize()
                        .constrainAs(musicPlayerBackground) {
                            //宽高和父布局一样
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                )

                //黑胶唱片背景
                if (showRecord) {
                    Image(
                        painter =
                            painterResource(
                                id = R.drawable.music_record_background,
                            ),
                        contentDescription = null,
                        modifier = Modifier
                            .constrainAs(recordBackground) {
                                top.linkTo(parent.top, margin = recordBackgroundMarginTop.value.dp)
                                //水平居中
                                centerHorizontallyTo(parent)
                                //243/320
                                width = Dimension.percent(0.759375f)
                            }
                            .aspectRatio(1f),
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
//                        .padding(paddingValues)
                        .padding(bottom = SpaceLarge)
                ) {
                    Surface(
                        color = Color.Transparent,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                    ) {
                        //淡入淡出
                        AnimatedVisibility(
                            visible = showRecord,
                            enter = fadeIn(),
                            exit = fadeOut(),
                            modifier = Modifier
                        ) {
                            RecordPagerView(
                                datum = datum,
                                nowPlaying = nowPlaying,
                                onScrollPage = onScrollPage,
                                contentPaddingTop = recordMarginTop.value.dp,
                                recordRotation = recordRotation,
                                onPauseRecordRotation = onPauseRecordRotation,
                                modifier = Modifier
                                    .clickableNoRipple {
                                        toggleRecordAndLyric()
                                    }
                            )
                        }

                        AnimatedVisibility(
                            visible = !showRecord,
                            enter = fadeIn(),
                            exit = fadeOut(),
                            modifier = Modifier
                        ) {
                            MyLyricList(
                                data = lyric,
                                currentPosition = currentPosition,
                                onLyricPlayClick = onLyricPlayClick,
                                modifier = Modifier
                                    .clickableNoRipple {
                                        toggleRecordAndLyric()
                                    }
                                    .padding(paddingValues)
                            )
                        }
                    }

                    //其他按钮
                    PlayMediaOtherButtons(

                    )

                    SpacerOuterHeight()

                    //进度信息
                    ProgressInfo(
                        currentPosition = currentPosition,
                        duration = playbackState.durationFormat,
                        onSeek = onSeek,
                    )
                    SpacerOuterHeight()

                    //播放控制按钮
                    PlayerMediaButtons(
                        isPlaying = playbackState.isPlaying,
                        playRepeatMode = playRepeatMode,
                        onChangeRepeatModeClick = onChangeRepeatModeClick,
                        onPreviousClick = onPreviousClick,
                        onPlayOrPauseClick = onPlayOrPauseClick,
                        onNextClick = onNextClick,
                        onMusicListClick = onMusicListClick,
                    )
                }

                if (showRecord) {
                    RecordThumb(
                        isPlaying = playbackState.isPlaying && !pauseRecordRotation,
                        modifier = Modifier
                            .constrainAs(recordThumb) {
                                centerHorizontallyTo(parent)//水平居中
                                top.linkTo(
                                    parent.top,
                                    margin = recordThumbMarginTop.value.dp
                                )

                                //154/320
                                width = Dimension.percent(0.48125f)
                            }
                    )
                }
            }
        }
    }
}

@Composable
fun RecordThumb(
    isPlaying: Boolean,
    modifier: Modifier = Modifier
) {
    val rotationAngle by animateFloatAsState(
        targetValue = if (isPlaying) 0f else -25f,
        animationSpec = tween(durationMillis = THUMB_DURATION),
        label = "RecordThumb"
    ) // 创建动画值

    Image(
        painter = painterResource(id = R.drawable.music_record_thumb),
        contentDescription = null,
        contentScale = ContentScale.Inside,
        modifier = modifier
            .graphicsLayer {
                rotationZ = rotationAngle // 旋转角度
                transformOrigin = TransformOrigin(0.5f, 0.12f) // 设置旋转锚点，百分比
            },
    )
}

private const val THUMB_DURATION: Int = 300

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RecordPagerView(
    datum: List<SongEntity>,
    nowPlaying: MediaItem,
    onScrollPage: (Int) -> Unit,
    recordRotation: Float,
    contentPaddingTop: Dp,
    onPauseRecordRotation: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (datum.isNotEmpty()) {
        var currentIndex by rememberSaveable {
            mutableIntStateOf(0)
        }

        currentIndex = datum.indexOfFirst { it.id == nowPlaying.mediaId }
        if (currentIndex == -1) {
            return
        }

        val pagerState = rememberPagerState(
            initialPage = currentIndex,
            pageCount = { datum.size }
        )

        var isProgrammaticScroll by remember { mutableStateOf(false) }

        HorizontalPager(
            state = pagerState,
            beyondBoundsPageCount = 1, //加载屏幕外的更多页面
            modifier = modifier.fillMaxSize(),
        ) { page ->
            RecordView(
                data = datum[page],
                recordRotation = if (page == currentIndex)
                    recordRotation
                else
                    0f,
                modifier
                    .padding(top = contentPaddingTop)
            )
        }

        LaunchedEffect(pagerState) {
            snapshotFlow {
                pagerState.settledPage
            }
                .distinctUntilChanged()
                .collectLatest {
                    if (currentIndex != it) {
                        Log.d("TAG", "RecordPagerView: currentPage ${it} ${currentIndex}")
                        onScrollPage(it)
                    }
                }
        }

        LaunchedEffect(nowPlaying) {
            if (currentIndex != pagerState.currentPage) {
                isProgrammaticScroll = true
                pagerState.animateScrollToPage(page = currentIndex)
            }
        }

        LaunchedEffect(pagerState) {
            snapshotFlow { pagerState.isScrollInProgress }.collect { isScrolling ->
                if (isProgrammaticScroll) {
                    if (!isScrolling) {
                        isProgrammaticScroll = false
                    }
                } else {
                    if (isScrolling) {
                        onPauseRecordRotation(true)
//                        Timber.d("record list user scrolling")
                    } else {
                        onPauseRecordRotation(false)
//                        Timber.d("record list user scroll end")
                    }
                }
            }
        }
    }
}

@Composable
fun RecordView(
    data: SongEntity,
    recordRotation: Float,
    modifier: Modifier = Modifier
) {
    ConstraintLayout(
        modifier = modifier.fillMaxSize(),
    ) {
        val (recordBackground, musicIcon) = createRefs()
        Box(
            modifier = Modifier
                .constrainAs(recordBackground) {
                    //水平居中
                    centerHorizontallyTo(parent)
                    //243/320
                    width = Dimension.percent(0.759375f)
                }
                .aspectRatio(1f)
                .graphicsLayer(
                    rotationZ = recordRotation
                )
//                .background(Color.Blue)
        ) {
            val musicIconModifier = Modifier
                .align(Alignment.Center)
                .fillMaxSize(0.64f)

            MyRecordImage(
                icon = data.icon,
                modifier = musicIconModifier
            )

            Image(
                painter =
                    painterResource(
                        id = R.drawable.music_record_ring,
                    ),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
            )
        }

    }
}


@Composable
fun PlayerMediaButtons(
    isPlaying: Boolean,
    playRepeatMode: PlaybackMode,
    onChangeRepeatModeClick: () -> Unit,
    onPreviousClick: () -> Unit,
    onPlayOrPauseClick: () -> Unit,
    onNextClick: () -> Unit,
    onMusicListClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        MusicControlButton(
            when (playRepeatMode) {
                PlaybackMode.REPEAT_LIST -> R.drawable.music_repeat_list
                PlaybackMode.REPEAT_ONE -> R.drawable.music_repeat_single
                PlaybackMode.REPEAT_SHUFFLE, PlaybackMode.REPEAT_UNSPECIFIED -> R.drawable.music_repeat_random
            },
            modifier = Modifier
                .weight(1f)
                .clickableNoRipple {
                    onChangeRepeatModeClick()
                }
        )
        MusicControlMiddleButton(
            R.drawable.music_previous,
            modifier = Modifier
                .weight(1f)
                .clickableNoRipple {
                    onPreviousClick()
                }
        )
        Image(
            painter =
                painterResource(
                    id =
                        if (isPlaying)
                            R.drawable.music_pause
                        else
                            R.drawable.music_play
                ),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .weight(1f)
                .clickableNoRipple {
                    onPlayOrPauseClick()
                }
        )
        MusicControlMiddleButton(
            R.drawable.music_next,
            modifier = Modifier
                .weight(1f)
                .clickableNoRipple {
                    onNextClick()
                }
        )
        MusicControlButton(
            R.drawable.music_list,
            modifier = Modifier
                .weight(1f)
                .clickableNoRipple {
                    onMusicListClick()
                }
        )
    }
}

@Composable
fun MusicControlMiddleButton(icon: Int, modifier: Modifier) {
    Image(
        painter =
            painterResource(
                id = icon
            ),
        contentDescription = null,
        modifier = modifier
            .aspectRatio(1f)
            .padding(19.dp)
//            .background(Color.Red)
    )
}

@Composable
fun MusicControlButton(icon: Int, modifier: Modifier) {
    Image(
        painter =
            painterResource(
                id = icon
            ),
        contentDescription = null,
        modifier = modifier
            .aspectRatio(1f)
            .padding(SpaceOuter)
//            .background(Color.Red)
    )
}

@Composable
fun ProgressInfo(
    currentPosition: Long,
    duration: Long,
    onSeek: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = SpaceOuter),
    ) {
        Slider(
            modifier = Modifier
                .fillMaxWidth(),
            value = currentPosition.toFloat(),
            valueRange = 0f..duration.toFloat(),
            onValueChange = onSeek,
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = currentPosition.asFormatTimeString(),
                style = MaterialTheme.typography.bodySmall,
                color = Color.White
            )
            Text(
                text = duration.asFormatTimeString(),
                style = MaterialTheme.typography.bodySmall,
                color = Color.White
            )
        }
    }
}


@Composable
fun PlayMediaOtherButtons(
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        MusicControlSmallButton(
            R.drawable.music_download,
            modifier = Modifier
                .weight(1f)
                .clickableNoRipple {

                }
        )
        MusicControlSmallButton(
            R.drawable.music_collect,
            modifier = Modifier
                .weight(1f)
                .clickableNoRipple {

                }
        )
        MusicControlSmallButton(
            R.drawable.music_comment,
            modifier = Modifier
                .weight(1f)
                .clickableNoRipple {

                }
        )
        MusicControlSmallButton(
            R.drawable.music_sing,
            modifier = Modifier
                .weight(1f)
                .clickableNoRipple {

                }
        )
        MusicControlSmallButton(
            R.drawable.music_equalizer,
            modifier = Modifier
                .weight(1f)
                .clickableNoRipple {

                }
        )
    }
}

@Composable
fun MusicControlSmallButton(icon: Int, modifier: Modifier) {
    Image(
        painter =
            painterResource(
                id = icon
            ),
        contentDescription = null,
        modifier = modifier
            .aspectRatio(1f)
            .padding(SpaceOuter)
    )
}

@Composable
fun BackgroundContent(
    data: String,
    modifier: Modifier = Modifier,
) {
    Image(
        painter = painterResource(id = R.drawable.music_player_background),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = modifier,
    )
}

@Preview(showBackground = true)
@Composable
fun SplashRoutePreview(): Unit {
    MyAppTheme {
        MusicPlayerScreen(

        )
    }
}