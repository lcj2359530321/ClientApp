package com.quick.app.feature.shortvideo

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import android.view.LayoutInflater
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import coil.compose.AsyncImage
import com.quick.app.R
import com.quick.app.core.design.theme.SpaceLarge
import com.quick.app.core.design.theme.SpaceMedium
import com.quick.app.core.design.theme.SpaceMediumWidth
import com.quick.app.core.design.theme.SpaceOuter
import com.quick.app.core.design.theme.bodyXLargeBold
import com.quick.app.core.extension.clickableNoRipple
import com.quick.app.core.model.Content
import com.quick.app.util.ResourceUtil
import com.quick.app.util.StringUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
fun ShortVideoRoute(
    isPausePlay: Boolean,
    onVideoPlayingChanged: (Boolean) -> Unit,
    viewModel: ShortVideoViewModel = hiltViewModel()
) {
    val datum by viewModel.datum.collectAsStateWithLifecycle()

    ShortVideoScreen(
        datum = datum,
        players = viewModel.players,
        onPageChange = viewModel::onPageChange,
        pausePlay = viewModel::pausePlay,
        resumePlay = viewModel::resumePlay,
        onVideoPlayingChanged = onVideoPlayingChanged,
    )

    LaunchedEffect(isPausePlay) {
        Timber.d("isPausePlay: $isPausePlay")
        viewModel.setPausePlay(isPausePlay)
        if (isPausePlay) {
            viewModel.pausePlay()
        } else {
            viewModel.resumePlay()
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ShortVideoScreen(
    datum: List<Content> = listOf(),
    players: List<ExoPlayer>,
    onPageChange: (Int) -> Unit,
    pausePlay: () -> Unit,
    resumePlay: () -> Unit,
    onVideoPlayingChanged: (Boolean) -> Unit,
) {
    val pagerState = rememberPagerState { datum.size }

    VerticalPager(
        state = pagerState,
        beyondBoundsPageCount = 1, //加载屏幕外的更多页面
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) { page ->
        val data = datum[page]

        ItemShortVideo(
            data = data,
            player = players[page % 5],
            onVideoPlayingChanged = onVideoPlayingChanged,
        )
    }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.settledPage }.collect { pageIndex ->
            Timber.d("pageIndex: $pageIndex")
            onPageChange(pageIndex)
        }
    }

    val lifecycleOwner by rememberUpdatedState(LocalLifecycleOwner.current)

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> pausePlay()
                Lifecycle.Event.ON_RESUME -> {
                    resumePlay()
                }

                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun ItemShortVideo(
    data: Content,
    onVideoPlayingChanged: (Boolean) -> Unit,
    player: ExoPlayer,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()

    var showPlayButton by remember {
        mutableStateOf(false)
    }

    var currentProgress by remember { mutableFloatStateOf(0f) }

    var progressRange by remember {
        mutableStateOf(0f..100f)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        AsyncImage(
            model = ResourceUtil.r(data.medias!![0]),
            contentDescription = null,
            contentScale = if (data.isPortraitVideo()) {
                ContentScale.Crop
            } else
                ContentScale.Inside,
            modifier = Modifier
                .fillMaxSize(),
        )

        AndroidView(
            factory = { context ->
                PlayerView(context).apply {
                    this.player = player
                    useController = false // 不显示控制器
                    resizeMode = if (data.isPortraitVideo())
                        AspectRatioFrameLayout.RESIZE_MODE_ZOOM //等比例填充整个控件，多余裁剪
                    else AspectRatioFrameLayout.RESIZE_MODE_FIT //等比例填充整个控件，多余留黑边
                    layoutParams = FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    player.addListener(object : Player.Listener {
                        override fun onIsPlayingChanged(isPlaying: Boolean) {
                            showPlayButton = !isPlaying
                            onVideoPlayingChanged(isPlaying)

                            //每秒钟更新进度，暂停了，停止更新
                            scope.launch {
                                while (isPlaying) {
                                    currentProgress = player.currentPosition.toFloat()
                                    delay(100)
                                }
                            }
                        }

                        override fun onPlaybackStateChanged(state: Int) {
                            if (state == ExoPlayer.STATE_READY) {
                                progressRange = 0f..player.duration.toFloat()
                            }
                        }
                    })
                }
            },
            modifier = Modifier
                .fillMaxSize()
                .clickableNoRipple {
                    if (player.isPlaying) {
                        player.pause()
                    } else {
                        player.play()
                    }
                }
        )

        LaunchedEffect(data.uri) {
            player.setMediaItem(MediaItem.fromUri(ResourceUtil.r(data.uri!!)))
            player.playWhenReady = false
            player.prepare()
        }

        AnimatedVisibility(
            visible = showPlayButton,
            enter = scaleIn(spring(Spring.DampingRatioMediumBouncy), initialScale = 1.5f),
            exit = scaleOut(tween(150)),
            modifier = Modifier.align(Alignment.Center)
        ) {
            Image(
                painter = painterResource(id = R.drawable.player_pause_large),
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp),
            )
        }

        Slider(
            value = currentProgress,
            onValueChange = {
                player.seekTo(it.toLong())
            },
            valueRange = progressRange,
            modifier = Modifier
                .padding(horizontal = SpaceMedium)
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
        )

        VideoInfo(
            data,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 50.dp, start = SpaceOuter, end = SpaceMedium)
        )
    }
}

@Composable
fun VideoInfo(
    data: Content,
    modifier: Modifier
) {
    Row(
        verticalAlignment = Alignment.Bottom,
        modifier = modifier
            .fillMaxWidth()
    ) {
        //用户和视频信息
        UserAndVideoInfo(
            nickname = data.user?.nickname ?: "默认用户",
            title = data.title ?: "默认视频标题",
            modifier = Modifier.weight(1f)
        )

        SpaceMediumWidth()

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(SpaceLarge),
            modifier = Modifier
        ) {
            //用户头像
            UserIcon(data.user!!.icon)

            //快捷按钮
            IconTitleButton(
                icon = R.drawable.heart,
                count = data.likesCount,
                title = R.string.like,
            )

            IconTitleButton(
                icon = R.drawable.comment_solid,
                count = data.commentsCount,
                title = R.string.comment,
            )

            IconTitleButton(
                icon = R.drawable.collect_star,
                count = data.collectsCount,
                title = R.string.collect,
            )

            IconTitleButton(
                icon = R.drawable.share_arrow,
                count = 0,
                title = R.string.share,
            )
        }
    }
}


@Composable
fun IconTitleButton(
    icon: Int = R.drawable.heart,
    count: Long = 0L,
    title: Int = R.string.comment
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = Modifier
        )
        Text(
            text = if (count == 0L)
                stringResource(id = title)
            else
                StringUtil.formatCount(count),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onPrimary,
        )
    }
}


@Composable
fun UserIcon(icon: String?, iconRes: Int = R.drawable.default_avatar) {
    Box(
        modifier = Modifier
            .width(50.dp)
            .height(65.dp)
    ) {
        if (icon != null) {
            AsyncImage(
                model = ResourceUtil.r(icon),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .border(2.dp, MaterialTheme.colorScheme.onPrimary, CircleShape)
                    .clickable(onClick = {

                    }),
            )
        } else {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .border(2.dp, MaterialTheme.colorScheme.onPrimary, CircleShape)
                    .clickable(onClick = {}),
            )
        }

        Image(
            painter = painterResource(id = R.drawable.follow_add),
            contentDescription = null,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}


@Composable
fun UserAndVideoInfo(
    nickname: String = "",
    title: String = "",
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = "@$nickname",
            style = MaterialTheme.typography.bodyXLargeBold,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .fillMaxWidth()
        )

        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .padding(top = SpaceMedium)
                .fillMaxWidth()
        )
    }
}