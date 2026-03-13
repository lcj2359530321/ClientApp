package com.quick.app.feature.main

import android.text.TextUtils
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.MediaItem
import coil.compose.AsyncImage
import com.quick.app.R
import com.quick.app.core.database.model.SongEntity
import com.quick.app.core.datastore.UserPreferences
import com.quick.app.core.design.component.MyConfirmDialog
import com.quick.app.core.design.component.MyNavigationBar
import com.quick.app.core.design.theme.ArrowIcon
import com.quick.app.core.design.theme.SpaceExtraOuter
import com.quick.app.core.design.theme.SpaceExtraSmall
import com.quick.app.core.design.theme.SpaceExtraSmallHeight
import com.quick.app.core.design.theme.SpaceMedium
import com.quick.app.core.design.theme.SpaceMediumWidth
import com.quick.app.core.design.theme.SpaceOuter
import com.quick.app.core.design.theme.SpaceSmall
import com.quick.app.core.design.theme.SpaceSmallHeight
import com.quick.app.core.design.theme.SpacerOuterHeight
import com.quick.app.core.design.theme.bodyXLarge
import com.quick.app.core.design.theme.extraSmallRoundedCornerShape
import com.quick.app.core.extension.clickableNoRipple
import com.quick.app.core.media.EMPTY_PLAYBACK_STATE
import com.quick.app.core.media.PlaybackState
import com.quick.app.core.model.UserData
import com.quick.app.feature.discovery.DISCOVERY_ROUTE
import com.quick.app.feature.discovery.DiscoveryRoute
import com.quick.app.feature.me.MeRoute
import com.quick.app.feature.shortvideo.ShortVideoRoute
import com.quick.app.feature.feed.FeedRoute
import com.quick.app.feature.mediaplayer.MyMusicListDialog
import com.quick.app.feature.mediaplayer.MyMusicPlayerBottomBar
import com.quick.app.ui.MyAppUiState
import com.quick.app.util.Constant
import com.quick.app.util.ResourceUtil
import kotlinx.coroutines.launch

@Composable
fun MainRoute(
    appUiState: MyAppUiState,
    finishPage: () -> Unit,
    toSheetDetail: (String) -> Unit,
    toFriend: (Int) -> Unit,
    toScan: () -> Unit,
    toProfile: () -> Unit,
    toCode: () -> Unit,
    toLogin: () -> Unit,
    toMessage: () -> Unit,
    toSetting: () -> Unit,
    toAbout: () -> Unit,
    toMusicPlayer: () -> Unit,
    toUri: (String) -> Unit,
    toLocalMusic: () -> Unit,
    toScanLocalMusic: () -> Unit,
    toEditSheet: (String) -> Unit,
    toPublishFeed: () -> Unit,
    onVideoPlayingChanged: (Boolean) -> Unit,
    toSearch: () -> Unit,
    onMainRouteChanged: (data: Int) -> Unit,
    viewModel: MainViewModel = hiltViewModel()
){
    val nowPlaying by viewModel.nowPlaying.collectAsStateWithLifecycle()
    val playbackState by viewModel.playbackState.collectAsStateWithLifecycle()
    val currentPosition by viewModel.currentPosition.collectAsStateWithLifecycle()
    val recordRotation by viewModel.recordRotation.collectAsStateWithLifecycle()
    val musicDatum by viewModel.datum.collectAsStateWithLifecycle()
    val showMusicListDialog by viewModel.showMusicListDialog.collectAsStateWithLifecycle()

    val isLogin by appUiState.isLogin.collectAsState()
    val userData by appUiState.userData.collectAsState()

    val isShowConfirmLogoutDialog by viewModel.isShowConfirmLogoutDialog.collectAsState()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val toggleDrawer: () -> Unit = {
        scope.launch {
            drawerState.apply {
                if (isClosed) open() else close()
            }
        }
    }

    val videoPlayingChanged = { it: Boolean ->
        onVideoPlayingChanged(it)
        if (it) {
            //视频播放了

            if (playbackState.isPlaying) {
                //音乐也在播放

                //暂停音乐
                viewModel.pause()
            }
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = MaterialTheme.colorScheme.background,
                modifier = Modifier
                    .fillMaxHeight()
            ) {
                MyDrawerView(
                    userData = userData,
                    isLogin = isLogin,
                    toFriend = toFriend,
                    toMessage = toMessage,
                    toCode = toCode,
                    toProfile = toProfile,
                    toLogin = {
                        toggleDrawer()
                        toLogin()
                    },
                    toScan = toScan,
                    toSetting = toSetting,
                    toAbout = toAbout,
                    onLogoutClick = {
                        viewModel.showConfirmLogoutDialog()
                    },
                )
            }
        },
    ) {
        MainScreen(
            toSheetDetail = toSheetDetail,
            toggleDrawer = toggleDrawer,
            datum = musicDatum,
            toMusicPlayer = toMusicPlayer,
            nowPlaying = nowPlaying,
            playbackState = playbackState,
            currentPosition = currentPosition.toFloat(),
            recordRotation = recordRotation,
            toUri = toUri,
            onPlayOrPauseClick = viewModel::onPlayOrPauseClick,
            onMusicListClick = viewModel::toggleShowMusicListDialog,
            appUiState = appUiState,
            toLogin = toLogin,
            toLocalMusic = toLocalMusic,
            toScanLocalMusic = toScanLocalMusic,
            toEditSheet = toEditSheet,
            toPublishFeed = toPublishFeed,
            onVideoPlayingChanged = onVideoPlayingChanged,
            toSearch = toSearch,
            onMainRouteChanged = onMainRouteChanged,
        )
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

    if (isShowConfirmLogoutDialog) {
        MyConfirmDialog(
            onConfirm = {
                viewModel.dismissConfirmLogoutDialog()
                viewModel.onLogoutClick()
            },
            onCancel = {
                viewModel.dismissConfirmLogoutDialog()
            },
            onDismissRequest = {
                viewModel.dismissConfirmLogoutDialog()
            }
        )
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreen(
    finishPage: () -> Unit = {},
    toSheetDetail: (String) -> Unit = {},
    toggleDrawer: () -> Unit = {},
    datum: List<SongEntity> = listOf(),
    toMusicPlayer: () -> Unit = {},
    nowPlaying: MediaItem = MediaItem.EMPTY,
    playbackState: PlaybackState = EMPTY_PLAYBACK_STATE,
    currentPosition: Float = 0F,
    recordRotation: Float = 0F,
    onPlayOrPauseClick: () -> Unit = {},
    onMusicListClick: () -> Unit = {},
    toUri: (String) -> Unit,
    appUiState: MyAppUiState,
    toLogin: () -> Unit,
    toLocalMusic: () -> Unit,
    toScanLocalMusic: () -> Unit,
    toEditSheet: (String) -> Unit,
    toPublishFeed: () -> Unit,
    toSearch: () -> Unit,
    onVideoPlayingChanged: (Boolean) -> Unit,
    onMainRouteChanged: (data: Int) -> Unit,
) {
    var currentDestination by rememberSaveable {
        mutableStateOf(ToplevelDestination.DISCOVERY.route)
    }
    val pageState = rememberPagerState {
        4
    }

    var isPausePlay by rememberSaveable {
        mutableStateOf(true)
    }

    //定义一个闭包，传递一个参数
    val mainRouteChanged = { it: Int ->
        onMainRouteChanged(it)
        isPausePlay = it != 1
    }


    //创立携程作用域
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        HorizontalPager(
            state = pageState,
            userScrollEnabled = false,//不允许用户手动滑动
            beyondBoundsPageCount = 4,//加载屏幕外更多的页面数量
            modifier = Modifier.fillMaxSize()
                .weight(1f)
        ) { page ->
            Box(modifier = Modifier.fillMaxSize().clipToBounds()) {
                when (page) {
                    0 -> DiscoveryRoute(
                        toSheetDetail = toSheetDetail,
                        toggleDrawer = toggleDrawer,
                        toMusicPlayer = toMusicPlayer,
                        toUri = toUri,
                        toSearch = toSearch,
                    )

                    1 -> ShortVideoRoute(
                        isPausePlay = isPausePlay,
                        onVideoPlayingChanged = onVideoPlayingChanged
                    )

                    2 -> MeRoute(
                        appUiState = appUiState,
                        toLogin = toLogin,
                        toSheetDetail = toSheetDetail,
                        toLocalMusic = toLocalMusic,
                        toScanLocalMusic = toScanLocalMusic,
                        toEditSheet = toEditSheet,
                    )

                    3 -> FeedRoute(
                        toPublishFeed = toPublishFeed,
                    )
                }
            }
        }
        //音乐控制
        if (nowPlaying.mediaId.isNotBlank() && datum.isNotEmpty()) { //TODO
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
                modifier = Modifier.clickable {
                    toMusicPlayer()
                },
            )
        }

        SpaceExtraSmallHeight()

        MyNavigationBar(
            destinations = ToplevelDestination.entries,
            currentDestination = currentDestination,
            onNavigateToDestination = { index ->
                currentDestination = ToplevelDestination.values()[index].route
                mainRouteChanged(index)
                //挂起函数必须在携程里
                scope.launch {
                    pageState.scrollToPage(index)
                }
            },
            modifier = Modifier
        )
    }
}

@Composable
fun MyDrawerView(
    userData: UserData,
    isLogin: Boolean,
    toFriend: (Int) -> Unit,
    toMessage: () -> Unit,
    toCode: () -> Unit,
    toProfile: () -> Unit,
    toLogin: () -> Unit,
    toScan: () -> Unit,
    toSetting: () -> Unit,
    toAbout: () -> Unit,
    onLogoutClick: () -> Unit,
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = SpaceOuter)
    ) {
        SpacerOuterHeight()

        MyUserInfoView(
            userData = userData,
            isLogin = isLogin,
            toProfile = toProfile,
            toLogin = toLogin,
            toScan = toScan,
        )

        SpacerOuterHeight()

        Column(
            verticalArrangement = Arrangement.spacedBy(SpaceOuter),
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState) //可滚动
        ) {
            MyVipHint()

            MyCard1(
                toMessage = toMessage,
                toFriend = toFriend,
                toCode = toCode,
            )

            (1..2).forEach { _ ->
                MyDrawerCard()
            }

            MySettingCard(
                toSetting = toSetting,
            )

            MyAboutCard(
                toAbout = toAbout,
            )

            if (isLogin) {
                OutlinedButton(
                    onClick = onLogoutClick,
                    modifier = Modifier
                        .padding(horizontal = SpaceOuter, vertical = 40.dp)
                        .fillMaxWidth()
                ) {
                    Text(text = stringResource(id = R.string.logout))
                }
            }
        }
    }
}

@Composable
fun MyAboutCard(
    toAbout: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.small)
    ) {
        MySettingItem(
            title = R.string.my_customer_service,
        )
        SpaceExtraSmallHeight()
        MySettingItem(
            title = R.string.share_app,
        )
        SpaceExtraSmallHeight()
        MySettingItem(
            title = R.string.about,
            onClick = toAbout,
        )
    }
}

@Composable
fun MySettingCard(
    toSetting: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.small)
    ) {
        MySettingItem(
            title = R.string.setting,
            onClick = toSetting,
        )
        SpaceExtraSmallHeight()
        MySettingItem(
            title = R.string.member_center,
        )
        SpaceExtraSmallHeight()
        MySettingItem(
            title = R.string.member_center,

            )
    }
}

@Composable
fun MyDrawerCard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.small)
    ) {
        (1..4).forEach { it ->
            MySettingItem()
            if (it != 4)
                SpaceExtraSmallHeight()
        }
    }
}

@Composable
fun MyCard1(
    toMessage: () -> Unit,
    toFriend: (Int) -> Unit,
    toCode: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.small)
    ) {
        MySettingItem(
            title = R.string.my_message,
            onClick = toMessage,
        )
        SpaceExtraSmallHeight()
        MySettingItem(
            title = R.string.my_friend,
            onClick = {
                toFriend(Constant.VALUE0)
            },
        )
        SpaceExtraSmallHeight()
        MySettingItem(
            title = R.string.my_fans,
            onClick = {
                toFriend(Constant.VALUE10)
            },
        )
        SpaceExtraSmallHeight()
        MySettingItem(
            title = R.string.my_code,
            onClick = toCode,
        )
    }
}

@Composable
fun MySettingItem(
    title: Int = R.string.my_message,
    value: String = "",
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clickable {
                onClick()
            }
            .padding(
                start = SpaceOuter,
                end = SpaceMedium,
                top = SpaceExtraOuter,
                bottom = SpaceExtraOuter
            ),
    ) {
        Icon(
            imageVector = Icons.Default.QrCode,
            contentDescription = null,
        )

        SpaceMediumWidth()

        Text(
            text = stringResource(id = title),
            style = MaterialTheme.typography.bodyXLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.outline,
            textAlign = TextAlign.Right,
            modifier = Modifier.weight(1f),
        )

        ArrowIcon()
    }
}

@Composable
fun MyVipHint() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.small)
            .background(colorResource(id = R.color.black42))
            .padding(SpaceOuter)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(id = R.string.buy_vip),
                style = MaterialTheme.typography.bodyXLarge,
                color = colorResource(id = R.color.black183),
                modifier = Modifier.weight(1f)
            )

            Text(
                text = stringResource(id = R.string.member_center),
                style = MaterialTheme.typography.bodySmall,
                color = Color(0xff837774),
                modifier = Modifier
                    .border(1.dp, colorResource(id = R.color.vip_border), CircleShape)
                    .padding(horizontal = SpaceMedium, vertical = SpaceSmall)

            )
        }

        SpaceSmallHeight()

        Text(
            text = stringResource(id = R.string.vip_hint),
            style = MaterialTheme.typography.bodySmall,
            color = colorResource(id = R.color.vip_border),
        )

        Spacer(
            modifier = Modifier
                .padding(vertical = SpaceMedium)
                .height(SpaceExtraSmall)
                .fillMaxWidth()
                .background(colorResource(id = R.color.divider2))
        )

        Text(
            text = stringResource(id = R.string.vip_hint_price),
            style = MaterialTheme.typography.bodySmall,
            color = colorResource(id = R.color.vip_border),
        )
    }
}


/**
 * 用户信息
 */
@Composable
fun MyUserInfoView(
    userData: UserData,
    isLogin: Boolean,
    toProfile: () -> Unit,
    toScan: () -> Unit,
    toLogin: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        if (userData.isLogin()) {
            //用户信息
            UserProfile(
                userData.user,
                toProfile = toProfile,
                toScan = toScan,
            )
        } else {
            DefaultUserProfile(
                toLogin = toLogin,
                toScan = toScan,
            )
        }
    }
}

private val userIconModifier = Modifier
    .size(30.dp)
    .clip(extraSmallRoundedCornerShape)

@Composable
private fun DefaultUserProfile(
    toLogin: () -> Unit,
    toScan: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickableNoRipple { toLogin() },
    ) {
        Image(
            painter = painterResource(id = R.drawable.default_avatar),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = userIconModifier,
        )

        SpaceMediumWidth()

        Text(
            text = stringResource(id = R.string.login_or_register),
            style = MaterialTheme.typography.bodyXLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )

        ArrowIcon()

        Spacer(modifier = Modifier.weight(1f))

        IconButton(onClick = toScan) {
            Icon(
                painter = painterResource(id = R.drawable.scan),
                contentDescription = null,
                modifier = Modifier.size(36.dp),
            )
        }
    }
}

@Composable
private fun UserProfile(
    data: UserPreferences,
    toProfile: () -> Unit,
    toScan: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickableNoRipple {
                toProfile()
            }
    ) {
        if (TextUtils.isEmpty(data.icon)) {
            Image(
                painter = painterResource(id = R.drawable.default_avatar),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = userIconModifier,
            )
        } else {
            AsyncImage(
                model = ResourceUtil.r(data.icon),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = userIconModifier,
            )
        }

        SpaceMediumWidth()

        Text(
            text = data.nickname,
            style = MaterialTheme.typography.bodyXLarge,
            color = MaterialTheme.colorScheme.onSurface,
        )


        ArrowIcon()

        Spacer(modifier = Modifier.weight(1f))

        IconButton(onClick = toScan) {
            Icon(
                painter = painterResource(id = R.drawable.scan),
                contentDescription = null,
                modifier = Modifier.size(36.dp),
            )
        }
    }
}

