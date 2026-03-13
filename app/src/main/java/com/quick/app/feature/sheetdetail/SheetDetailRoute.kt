package com.quick.app.feature.sheetdetail

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Checklist
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.MediaItem
import androidx.palette.graphics.Palette
//import androidx.palette.graphics.Palette
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.quick.app.MyAppState
import com.quick.app.R
import com.quick.app.core.design.component.MyAsyncImage
import com.quick.app.core.design.component.MyCenterTopAppBar
import com.quick.app.core.design.component.MyErrorView
import com.quick.app.core.design.component.MyLoading
import com.quick.app.core.design.component.MySweetError
import com.quick.app.core.design.theme.SpaceExtraSmall2
import com.quick.app.core.design.theme.SpaceMedium
import com.quick.app.core.design.theme.SpaceOuter
import com.quick.app.core.design.theme.SpaceSmall
import com.quick.app.core.design.theme.SpaceSmallWidth
import com.quick.app.core.design.theme.SpacerOuterHeight
import com.quick.app.core.design.theme.SpacerOuterWidth
import com.quick.app.core.design.theme.md_theme_light_transparent_button_bg
import com.quick.app.core.extension.clickableNoRipple
import com.quick.app.core.media.EMPTY_PLAYBACK_STATE
import com.quick.app.core.media.PlaybackState
import com.quick.app.core.model.Sheet
import com.quick.app.feature.mediaplayer.MyMusicListDialog
import com.quick.app.feature.mediaplayer.MyMusicPlayerBottomBar
import com.quick.app.feature.song.component.ItemSongSheet
import com.quick.app.util.ResourceUtil
import com.quick.app.util.StringUtil

@Composable
fun SheetDetailRoute(
    finishPage: () -> Unit,
    toMusicPlayer: () -> Unit,
    toLogin: () -> Unit,
    viewModel: SheetDetailViewModel = hiltViewModel()
) {
    val data by viewModel.uiState.collectAsState()
    val nowPlaying by viewModel.nowPlaying.collectAsStateWithLifecycle()
    val playbackState by viewModel.playbackState.collectAsStateWithLifecycle()
    val currentPosition by viewModel.currentPosition.collectAsStateWithLifecycle()
    val recordRotation by viewModel.recordRotation.collectAsStateWithLifecycle()
    val showMusicListDialog by viewModel.showMusicListDialog.collectAsStateWithLifecycle()
    val musicDatum by viewModel.datum.collectAsStateWithLifecycle()

    val tipError by viewModel.tipError.collectAsStateWithLifecycle()

    SheetDetailScreen(
        data = data,
        finishPage = finishPage,
        onRetryClick = viewModel::onRetryClick,
        onSongClick = viewModel::onSongClick,
        onCollectClick = {
            if (MyAppState.userId.isNotBlank()) {
                viewModel.onCollectClick()
            } else {
                toLogin()
            }
        },
        nowPlaying = nowPlaying,
        playbackState = playbackState,
        currentPosition = currentPosition.toFloat(),
        recordRotation = recordRotation,
        toMusicPlayer = toMusicPlayer,
        onPlayOrPauseClick = viewModel::onPlayOrPauseClick,
        onMusicListClick = viewModel::toggleShowMusicListDialog,
    )

    tipError?.let {
        MySweetError(message = it)
        viewModel.resetBaseState()
    }

    if (showMusicListDialog) {
        MyMusicListDialog(
            datum = musicDatum,
            nowPlaying = nowPlaying,
            onClearPlayListClick = viewModel::onClearPlayListClick,
            onItemPlayListClick = viewModel::onItemPlayListClick,
            onItemMusicDeleteClick = viewModel::onItemMusicDeleteClick,
            onDismissRequest = {
                viewModel.toggleShowMusicListDialog()
            }
        )
    }

    LaunchedEffect(viewModel.toMusicPlayer.value) {
        if (viewModel.toMusicPlayer.value) {
            toMusicPlayer()
            viewModel.clearMusicPlayer()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SheetDetailScreen(
    data: SheetDetailUiState = SheetDetailUiState.Loading,
    finishPage: () -> Unit = {},
    onRetryClick: () -> Unit = {},
    onSongClick: (Int) -> Unit = {},
    onCollectClick: () -> Unit = {},
    nowPlaying: MediaItem = MediaItem.EMPTY,
    playbackState: PlaybackState = EMPTY_PLAYBACK_STATE,
    currentPosition: Float,
    recordRotation: Float,
    onPlayOrPauseClick: () -> Unit,
    onMusicListClick: () -> Unit,
    toMusicPlayer: () -> Unit,
) {
    when (val data = data) {
        is SheetDetailUiState.Loading -> {
            MyLoading()
        }

        is SheetDetailUiState.Error -> {
            MyErrorView(
                exception = data.exception,
                onRetryClick = onRetryClick,
            )
        }

        is SheetDetailUiState.Success -> {
            ContentView(
                finishPage = finishPage,
                data = data.data,
                onSongClick = onSongClick,
                onCollectClick = onCollectClick,
                toMusicPlayer = toMusicPlayer,
                nowPlaying = nowPlaying,
                playbackState = playbackState,
                currentPosition = currentPosition,
                recordRotation = recordRotation,
                onPlayOrPauseClick = onPlayOrPauseClick,
                onMusicListClick = onMusicListClick,
            )
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ContentView(
    finishPage: () -> Unit,
    data: Sheet,
    onCollectClick: () -> Unit,
    onSongClick: (Int) -> Unit,
    nowPlaying: MediaItem,
    playbackState: PlaybackState,
    currentPosition: Float,
    recordRotation: Float,
    onPlayOrPauseClick: () -> Unit,
    onMusicListClick: () -> Unit,
    toMusicPlayer: () -> Unit,
) {
    var sheetDetailInfoBackgroundColor by remember { mutableStateOf(Color.Black) }
    val listState = rememberLazyListState()

    Scaffold(
        topBar = {
            MyCenterTopAppBar(
                finishPage = finishPage,
                titleText = stringResource(id = R.string.sheet),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = sheetDetailInfoBackgroundColor,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
            )
        },
        bottomBar = {
            //音乐控制
            if (nowPlaying.mediaId.isNotBlank()) {
                MyMusicPlayerBottomBar(
                    title = nowPlaying.mediaMetadata.title.toString(),
                    artist = nowPlaying.mediaMetadata.artist.toString(),
                    icon = nowPlaying.mediaMetadata.artworkUri.toString(),
                    isPlaying = playbackState.isPlaying,
                    currentPosition = currentPosition,
                    duration = playbackState.durationFormat.toFloat(),
                    recordRotation = recordRotation,
                    onPlayOrPauseClick = onPlayOrPauseClick,
                    onMusicListClick = onMusicListClick,
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .navigationBarsPadding()
                        .clickable {
                            toMusicPlayer()
                        },
                )
            }
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            //禁用过渡滚动:https://stackoverflow.com/questions/69468212/remove-lazycolumn-overscroll-effect-in-jetpack-compose
            CompositionLocalProvider(
                LocalOverscrollConfiguration provides null
            ) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    item {
                        SheetDetailInfo(
                            data = data,
                            background = sheetDetailInfoBackgroundColor,
                            onCollectClick = onCollectClick,
                        )
                    }

                    data.songs?.let { songs ->
                        item {
                            ItemSheetDetailTitle(
                                count = songs.size,
                                onPlayAllClick = {
                                    onSongClick(0)
                                }
                            )
                        }

                        itemsIndexed(songs) { index, data ->
                            ItemSongSheet(
                                data = data,
                                index = index,
                                isPlaying = playbackState.isPlaying,
                                currentPlayMediaId = nowPlaying.mediaId,
                                modifier = Modifier.clickable {
                                    onSongClick(index)
                                })
                        }
                    }
                }
            }
        }

    }

    LaunchedEffect(key1 = nowPlaying) {
        //滚动到当前播放歌曲
        if (nowPlaying.mediaId.isNotBlank()) {
            val index = data.songs!!.indexOfFirst { it.id == nowPlaying.mediaId }
            if (index != -1) {
                listState.animateScrollToItem(index, -200)
            }
        }
    }

    //提取封面色调
    data.icon?.let {
        val iconUrl = ResourceUtil.r2(data.icon)

        val painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data(iconUrl)
                .allowHardware(false)
                .size(Size.ORIGINAL) // Set the target size to load the image at.
                .build()
        )

        val state = painter.state
        if (state is AsyncImagePainter.State.Success) {
            state.result.drawable.toBitmap()?.let { bitmap ->
                Palette.from(bitmap).generate { palette ->
                    palette?.vibrantSwatch?.rgb?.let { colorValue ->
                        sheetDetailInfoBackgroundColor = Color(colorValue)
                    }
                }
            }
        }
    }
}

@Composable
fun ItemSheetDetailTitle(
    count: Int,
    onPlayAllClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(
                end = SpaceSmall,
                top = SpaceExtraSmall2,
                bottom = SpaceExtraSmall2
            ),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .weight(1f)
                .clickableNoRipple {
                    onPlayAllClick()
                }
        ) {
            Box(modifier = Modifier.size(50.dp)) {
                Image(
                    painter = painterResource(id = R.drawable.music_play),
                    contentDescription = null,
                    modifier = Modifier
                        .size(25.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(SpaceSmall)
                        .align(Alignment.Center)
                )
            }
            Text(
                text = stringResource(id = R.string.play_all),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
            )
            SpaceSmallWidth()
            Text(
                text = "(${count})",
                color = MaterialTheme.colorScheme.outline,
                style = MaterialTheme.typography.bodySmall,
            )
        }
        IconButton(onClick = {}) {
            Icon(
                imageVector = Icons.Default.Download,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.outline,
            )
        }

        IconButton(onClick = {}) {
            Icon(
                imageVector = Icons.Default.Checklist,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.outline,
            )
        }
    }
}

@Composable
fun SheetDetailInfo(
    data: Sheet,
    background: Color,
    onCollectClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
//            .aspectRatio(910f / 730)
            .background(background)
            .padding(SpaceOuter)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
//                .padding(top = paddingTop)
        ) {
            MyAsyncImage(
                model = data.icon,
                modifier = Modifier
                    .size(87.dp)
                    .clip(MaterialTheme.shapes.small),
            )

            SpacerOuterWidth()

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = data.title,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onPrimary,
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    MyAsyncImage(
                        model = data.user!!.icon,
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape),
                    )
                    SpaceSmallWidth()
                    Text(
                        text = data.user!!.nickname ?: "",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.LightGray,
                    )
                }

            }
        }

        SpacerOuterHeight()

        //详情
        Text(
            text = data.detail ?: "",
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.LightGray,
        )

        SpacerOuterHeight()

        var itemButtonBackground by remember {
            mutableStateOf(md_theme_light_transparent_button_bg)
        }

        //按钮
        Row(
            horizontalArrangement = Arrangement.spacedBy(SpaceMedium),
            modifier = Modifier.fillMaxWidth()
        ) {
            FilledTonalButton(
                onClick = { },
                colors = ButtonDefaults.filledTonalButtonColors().copy(
                    containerColor = itemButtonBackground,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                ),
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.Share, contentDescription = null)
                Text("128")
            }
            FilledTonalButton(
                onClick = { },
                colors = ButtonDefaults.filledTonalButtonColors().copy(
                    containerColor = itemButtonBackground,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                ),
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
                Text(StringUtil.formatCount(data.commentsCount))
            }
            FilledTonalButton(
                onClick = onCollectClick,
                colors = ButtonDefaults.filledTonalButtonColors().copy(
                    containerColor =
                        if (data.isCollected) itemButtonBackground
                        else MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                ),
                modifier = Modifier.weight(1f)
            ) {
                if (data.isCollected) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Text(data.collectsCount.toString())
                } else {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Text(stringResource(id = R.string.collect))
                }
            }
        }
    }
}
