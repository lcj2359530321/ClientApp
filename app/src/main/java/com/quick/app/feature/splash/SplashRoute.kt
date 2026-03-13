package com.quick.app.feature.splash

import android.view.ViewGroup
import android.widget.FrameLayout
import android.window.SplashScreen
import androidx.activity.compose.BackHandler
import androidx.annotation.OptIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.Timeline
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import coil.compose.AsyncImage
import com.quick.app.R
import com.quick.app.core.design.component.MyTermServiceDialog
import com.quick.app.core.design.theme.MyAppTheme
import com.quick.app.core.design.theme.SpaceExtraMedium
import com.quick.app.core.design.theme.SpaceLarge
import com.quick.app.core.design.theme.SpaceMedium
import com.quick.app.core.design.theme.SpaceOuter
import com.quick.app.core.design.theme.SpaceSmall
import com.quick.app.core.design.theme.bodyXLarge
import com.quick.app.core.design.theme.button_transparent_88
import com.quick.app.core.extension.clickableNoRipple
import com.quick.app.core.model.Ad
import com.quick.app.util.SuperDateUtil
import java.time.Year

@Composable
fun SplashRoute(
    toMain : () -> Unit,
    toGuide : () -> Unit,
    toAd: (Ad) -> Unit,
    viewModel : SplashViewModel = hiltViewModel()
){
    val timeLeft by viewModel.timeLeft.collectAsStateWithLifecycle()
    val navigateToMain by viewModel.navigateToMain.collectAsState()
    val navigateToGuide by viewModel.navigateToGuide.collectAsState()
    val isShowTermsServiceAgreementDialog by viewModel.isShowTermsServiceAgreementDialog.collectAsState()
    val imageAd by viewModel.imageAd.collectAsState()
    val videoAd by viewModel.videoAd.collectAsState()

    SplashScreen(
        year = SuperDateUtil.currentYear(),
        timeLeft = timeLeft,
        onSkipAdClick = viewModel::onSkipAdClick,
        imageAd = imageAd,
        videoAd = videoAd,
        setTimeLeft = viewModel::setTimeLeft,
        onSkipClick = viewModel::onSkipClick,
        onAdClick = viewModel::onAdClick,
    )

    if (isShowTermsServiceAgreementDialog) {
        MyTermServiceDialog(
            onPrimaryClick = viewModel::onPrimaryClick,
            onDisagreeClick = viewModel::onDisagreeClick,
            onDismissRequest = {
                //点击外部,不能关闭
            },
        )
    }

    if(navigateToMain){
        LaunchedEffect(key1 = true) {
            toMain()
        }
    }

    if(navigateToGuide){
        LaunchedEffect(key1 = true) {
            toGuide()
        }
    }

    LaunchedEffect(viewModel.navigateToAd.value) {
        viewModel.navigateToAd.value?.let {
            toAd(it)
        }
    }

    //enabled为true表示拦截
    BackHandler(enabled = true) {
        //如果拦截了，会执行这里
    }
}

@Composable
fun SplashScreen(
    year: Int = 2025,
    timeLeft : Long = 0,
    onSkipAdClick : () -> Unit = {},
    imageAd: String? = null,
    videoAd: String? = null,
    setTimeLeft: (Long) -> Unit = {},
    onAdClick: () -> Unit = {},
    onSkipClick: () -> Unit = {},
){
    Box(modifier = Modifier
        .fillMaxSize()
        .background(color = MaterialTheme.colorScheme.primary)
    ){
        //不显示到状态栏里
        //Text("111", modifier = Modifier.statusBarsPadding().background(Color.Blue))

        //region 启动界面 banner
        Image(
            painter = painterResource(id = R.drawable.splash_banner),
            contentDescription = null,
            modifier = Modifier.padding(top = 150.dp)
                .align(Alignment.TopCenter)
        )
        //endregion


        //region 启动界面 logo
        Image(
            painter = painterResource(id = R.drawable.splash_logo),
            contentDescription = null,
            modifier = Modifier.padding(bottom = 70.dp)
                .align(Alignment.BottomCenter)
                .clickable {
                    onSkipAdClick()
                }
        )
        //endregion

        // 版权文本
        Text(text = stringResource(id = R.string.copyright,year),
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 30.dp)
        )

        imageAd?.let {
            MyImageAd(it)
        }

        videoAd?.let {
            MyVideoAd(
                it,
                setTimeLeft = setTimeLeft,
            )
        }

        if (imageAd != null || videoAd != null) {
            MyAdControl(
                timeLeft,
                skipAd = onSkipClick,
                onAdClick = onAdClick,
            )
        }
    }
}

@Composable
fun MyAdControl(
    timeLeft: Long = 0,
    skipAd: () -> Unit = {},
    onAdClick: () -> Unit = {},
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = stringResource(id = R.string.wifi_preload),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .padding(start = SpaceOuter, top = 50.dp)
                .background(button_transparent_88, MaterialTheme.shapes.large)
                .padding(SpaceSmall)

        )

        Text(
            text = stringResource(id = R.string.skip_ad_count, timeLeft),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = SpaceLarge, top = 50.dp)
                .background(button_transparent_88, MaterialTheme.shapes.extraLarge)
                .clickableNoRipple {
                    skipAd()
                }
                .padding(horizontal = SpaceMedium, vertical = SpaceExtraMedium)
        )

        Box(
            modifier = Modifier
                .padding(start = 50.dp, end = 50.dp, bottom = 100.dp)
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(button_transparent_88, MaterialTheme.shapes.extraLarge)
                .clickableNoRipple {
                    onAdClick()
                }
                .padding(SpaceLarge)
        ) {
            Text(
                text = stringResource(id = R.string.ad_click_tip),
                style = MaterialTheme.typography.bodyXLarge,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .align(Alignment.Center)
            )
        }
    }
}

@OptIn(androidx.media3.common.util.UnstableApi::class)
@Composable
fun MyVideoAd(
    data: String,
    setTimeLeft: (Long) -> Unit = {},
) {
    val context = LocalContext.current

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            val mediaItem = MediaItem.fromUri(data)
            setMediaItem(mediaItem)
            volume = 0f // 设置为静音
            playWhenReady = true
            prepare()
        }
    }

    AndroidView(
        factory = { ctx ->
            PlayerView(ctx).apply {
                player = exoPlayer
                useController = false // 不显示控制器
                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM //等比例填充整个控件，多余裁剪
                layoutParams = FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }
        },
        modifier = Modifier
            .fillMaxSize()
    )

    val lifecycleOwner by rememberUpdatedState(LocalLifecycleOwner.current)
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_PAUSE -> exoPlayer.pause()
                Lifecycle.Event.ON_RESUME -> exoPlayer.play()
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        val videoListener = object : Player.Listener {
            override fun onTimelineChanged(timeline: Timeline, reason: Int) {
                if (reason === Player.TIMELINE_CHANGE_REASON_PLAYLIST_CHANGED) {
                    // 处理播放列表变化
                } else if (reason === Player.TIMELINE_CHANGE_REASON_SOURCE_UPDATE) {
                    // 处理动态源更新（如DASH/HLS）
                }

                // 常规处理: 更新UI或状态，如获取新的视频时长
                val duration = timeline.getWindow(0, Timeline.Window()).durationMs
                setTimeLeft(duration)
            }
        }
        exoPlayer.addListener(videoListener)

        onDispose {
            exoPlayer.removeListener(videoListener)
            lifecycleOwner.lifecycle.removeObserver(observer)
            exoPlayer.release()
        }
    }
}


@Composable
fun MyImageAd(data: String) {
    AsyncImage(
        model = data,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxSize(),
    )
}

@Preview(showBackground = true)
@Composable
fun SplashRoutePreview() : Unit{
    MyAppTheme {
        SplashScreen(
            year = 2025
        )
    }
}