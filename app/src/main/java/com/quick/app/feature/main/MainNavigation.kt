package com.quick.app.feature.main

import android.telecom.Call.Details
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.quick.app.feature.guide.GUIDE_ROUTE
import com.quick.app.feature.splash.SPLASH_ROUTE
import com.quick.app.ui.MyAppUiState

const val MAIN_ROUTE = "main"

/**
 * 跳转到这个界面
 */
fun NavController.navigateToMain(): Unit{
    navigate(MAIN_ROUTE){
        //不开启多个界面
        launchSingleTop = true

        //关闭guide以及之前所有的界面
        popUpTo(GUIDE_ROUTE){
            inclusive = true
        }
    }
}

/**
 * 配置导航
 */
fun NavGraphBuilder.mainScreen(
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
){
    composable(MAIN_ROUTE){
        MainRoute(
            appUiState = appUiState,
            finishPage = finishPage,
            toSheetDetail = toSheetDetail,
            toFriend = toFriend,
            toMessage = toMessage,
            toCode = toCode,
            toProfile = toProfile,
            toLogin = toLogin,
            toScan = toScan,
            toSetting = toSetting,
            toAbout = toAbout,
            toMusicPlayer = toMusicPlayer,
            toUri = toUri,
            toLocalMusic = toLocalMusic,
            toScanLocalMusic = toScanLocalMusic,
            toEditSheet = toEditSheet,
            toPublishFeed = toPublishFeed,
            onVideoPlayingChanged = onVideoPlayingChanged,
            toSearch = toSearch,
            onMainRouteChanged = onMainRouteChanged,
        )
    }
}