package com.quick.app.ui

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import cn.qhplus.emo.core.EmoBus
import com.quick.app.core.data.repository.UserDataRepository
import com.quick.app.core.design.component.MySweetError
import com.quick.app.core.design.component.MySweetSuccess
import com.quick.app.core.model.event.TipEvent
import com.quick.app.feature.about.aboutScreen
import com.quick.app.feature.about.navigateToAbout
import com.quick.app.feature.createsheet.createSheetScreen
import com.quick.app.feature.createsheet.navigateToCreateSheet
import com.quick.app.feature.guide.guideScreen
import com.quick.app.feature.guide.navigateToGuide
import com.quick.app.feature.inputcode.inputCodeScreen
import com.quick.app.feature.inputcode.navigateToInputCode
import com.quick.app.feature.inputidentity.inputIdentityScreen
import com.quick.app.feature.inputidentity.navigateToInputIdentity
import com.quick.app.feature.login.loginScreen
import com.quick.app.feature.login.navigateToLogin
import com.quick.app.feature.loginhome.finishAllLoginPages
import com.quick.app.feature.loginhome.loginHomeScreen
import com.quick.app.feature.loginhome.navigateToLoginHome
import com.quick.app.feature.main.mainScreen
import com.quick.app.feature.main.navigateToMain
import com.quick.app.feature.mediaplayer.musicPlayerScreen
import com.quick.app.feature.mediaplayer.navigateToMusicPlayer
import com.quick.app.feature.profile.navigateToProfile
import com.quick.app.feature.profile.profileScreen
import com.quick.app.feature.publishfeed.navigateToPublishFeed
import com.quick.app.feature.publishfeed.publishFeedScreen
import com.quick.app.feature.register.navigateToRegister
import com.quick.app.feature.register.registerScreen
import com.quick.app.feature.search.navigateToSearch
import com.quick.app.feature.search.searchScreen
import com.quick.app.feature.setpassword.navigateToSetPassword
import com.quick.app.feature.setpassword.setPasswordScreen
import com.quick.app.feature.setting.navigateToSetting
import com.quick.app.feature.setting.settingScreen
import com.quick.app.feature.sheetdetail.navigateToSheetDetail
import com.quick.app.feature.sheetdetail.sheetDetailScreen
import com.quick.app.feature.splash.SPLASH_ROUTE
import com.quick.app.feature.splash.splashScreen
import com.quick.app.feature.userdetail.navigateToUserDetail
import com.quick.app.feature.userdetail.userDetailScreen
import com.quick.app.feature.web.WebParam
import com.quick.app.feature.web.navigateToWeb
import com.quick.app.feature.web.webScreen
import com.quick.app.util.Constant
import com.quick.app.util.SuperUrlUtil
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun MyApp(
    navController: NavHostController,
    userDataRepository: UserDataRepository,
    toOpenIntent: (String) -> Unit,
    onVideoPlayingChanged: (Boolean) -> Unit,
    onMainRouteChanged: (data: Int) -> Unit,
    onRouteChanged: (data: NavDestination) -> Unit,
    appUiState: MyAppUiState = rememberMyAppUiState(userDataRepository = userDataRepository)
    ) {
    val coroutineScope = rememberCoroutineScope()

    var tipEvent by remember {
        mutableStateOf<TipEvent?>(null)
    }

    NavHost(navController = navController, startDestination = SPLASH_ROUTE){
        fun processUriClick(data: String) {
            navController.navigateToMain()

            Log.d(TAG, "processUriClick: $data")
            if (data.startsWith("quickapp://sheets/detail")) {
                //歌单详情
                val query = SuperUrlUtil.getQueryMap(data)

                (query["id"] as? String)?.let { id ->
                    navController.navigateToSheetDetail(id)
                }
            } else if (data.startsWith("http")) {
                //web打开网址
                navController.navigateToWeb(WebParam(uri = data))
            } else {
                //应用
                toOpenIntent(data)
            }
        }

        splashScreen(
            toGuide = navController::navigateToGuide,
            toMain = navController::navigateToMain,
            toAd = {
                it.uri?.let {
                    processUriClick(it)
                }
            }
        )
        mainScreen(
            appUiState = appUiState,
            finishPage = navController::popBackStack,
            toSheetDetail = navController::navigateToSheetDetail,
            toFriend = {},
            toMessage = {},
            toCode = {},
//            toCode = navController::navigateToCode,
            toProfile = navController::navigateToProfile,
            toLogin = navController::navigateToLoginHome,
//            toScan = navController::navigateToScan,
            toAbout = navController::navigateToAbout,
            toMusicPlayer = navController::navigateToMusicPlayer,
            toScan = {},
            toSetting = navController::navigateToSetting,
            toUri = ::processUriClick,
            toLocalMusic = {},
            toScanLocalMusic = {},
            toEditSheet = navController::navigateToCreateSheet,
            toPublishFeed = navController::navigateToPublishFeed,
            onVideoPlayingChanged = onVideoPlayingChanged,
            toSearch = navController::navigateToSearch,
            onMainRouteChanged = onMainRouteChanged,
        )
        sheetDetailScreen(
            finishPage = navController::popBackStack,
            toMusicPlayer = navController::navigateToMusicPlayer,
            toLogin = navController::navigateToLoginHome,
        )
        musicPlayerScreen(
            finishPage = navController::popBackStack,
        )
        loginHomeScreen(
            finishPage = navController::popBackStack,
            toLogin = navController::navigateToLogin,
            toCodeLogin = navController::navigateToInputIdentity,
            finishAllLoginPages = navController::finishAllLoginPages,
            toWebPage = {},
        )
        loginScreen(
            finishPage = navController::popBackStack,
            toRegister = navController::navigateToRegister,
            toSetPassword = {
                navController.navigateToInputIdentity(Constant.STYLE_FORGOT_PASSWORD)
            },
            finishAllLoginPages = navController::finishAllLoginPages,
        )
        registerScreen(
            finishPage = navController::popBackStack,
            finishAllLoginPages = navController::finishAllLoginPages,
        )
        guideScreen(
            toLogin = {
                navController.navigateToMain()
                navController.navigateToLoginHome()
            },
            toMain = navController::navigateToMain,
        )
        webScreen (
            finishPage = navController::popBackStack,
        )
        createSheetScreen(
            finishPage = navController::popBackStack,
        )
        profileScreen (
            finishPage = navController::popBackStack,
        )
        publishFeedScreen (
            finishPage = navController::popBackStack
        )
        inputIdentityScreen(
            finishPage = navController::popBackStack,
            toInputCode = navController::navigateToInputCode,
        )
        inputCodeScreen(
            finishPage = navController::popBackStack,
            toSetPassword = navController::navigateToSetPassword,
            finishAllLoginPages = navController::finishAllLoginPages,
        )
        setPasswordScreen(
            finishPage = navController::popBackStack,
            finishAllLoginPages = navController::finishAllLoginPages,
        )
        userDetailScreen(
            finishPage = navController::popBackStack,
        )
        searchScreen(
            finishPage = navController::popBackStack,
            toSheetDetail = navController::navigateToSheetDetail,
            toUserDetail = navController::navigateToUserDetail,
        )
        settingScreen(
            finishPage = navController::popBackStack,
        )
        aboutScreen(
            finishPage = navController::popBackStack,
        )
    }

    tipEvent?.tipError?.let {
        MySweetError(
            message = it
        )
        tipEvent = null
    }
    tipEvent?.tipErrorRes?.let {
        MySweetError(
            message = stringResource(id = it)
        )
        tipEvent = null
    }
    tipEvent?.tipSuccessRes?.let {
        MySweetSuccess(
            message = stringResource(id = it)
        )
        tipEvent = null
    }

    LaunchedEffect(key1 = Unit) {
        coroutineScope.launch {
            EmoBus.default.flowOf(TipEvent::class.java).collectLatest { event ->
                tipEvent = event
            }
        }
    }

    //region 监听路由变化
    //主要是设置不同界面的状态栏文字颜色
    DisposableEffect(navController) {
        val listener = NavController.OnDestinationChangedListener { controller, _, _ ->
//            Log.d("TAG", "MyApp route changed: ${controller.currentDestination?.route}")
            onRouteChanged(controller.currentDestination!!)
        }
        navController.addOnDestinationChangedListener(listener)
        onDispose {
            navController.removeOnDestinationChangedListener(listener)
        }
    }
    //endregion
}

private const val TAG = "MyApp"