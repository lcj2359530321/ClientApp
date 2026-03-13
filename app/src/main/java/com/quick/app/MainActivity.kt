package com.quick.app

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.quick.app.core.data.repository.UserDataRepository
import com.quick.app.core.design.theme.MyAppTheme
import com.quick.app.ui.MyApp
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import com.quick.app.core.model.DarkThemeConfig
import com.quick.app.feature.main.MAIN_ROUTE
import com.quick.app.feature.mediaplayer.navigateToMusicPlayer
import com.quick.app.feature.sheetdetail.SHEET_DETAIL_ROUTE
import com.quick.app.ui.MainActivityUiState
import com.quick.app.util.Constant
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var userDataRepository: UserDataRepository

    val viewModel: MainActivityViewModel by viewModels()

    //当前界面UI状态
    var uiState: MainActivityUiState by mutableStateOf(MainActivityUiState.Loading)

    private lateinit var navController: NavHostController

//    @Inject
//    lateinit var globalLyricManager: GlobalLyricManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //从viewmodel中监听状态
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.onEach {
                    uiState = it
                }
                    .collect()
            }
        }

        //显示到状态栏
        enableEdgeToEdge()

        //全部更改状态栏颜色白色透明
        //enableEdgeToEdge(statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT))

        setContent {
            navController = rememberNavController()

            val darkTheme = shouldUseDarkTheme(uiState)

            MyAppTheme (
                dynamicColor = shouldUseDynamicTheming(uiState),
            ){
                MyApp(
                    navController = navController,
                    userDataRepository = userDataRepository,
                    toOpenIntent = ::toOpenIntent,
                    onVideoPlayingChanged = {
                        //视频播放状态改变了
                        if (it) {
                            //屏幕常亮
                            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

                        } else {
                            //清除屏幕常亮
                            window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                        }
                    },
                    onMainRouteChanged = {
                        if (it == 1) {
                            //状态栏文字白色
                            setStatusBarDarkMode()

                            //取消屏幕方向
//                            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
                        } else {
                            //其他界面，状态栏颜色，根据系统自动设置
                            setStatusBarAutoMode(darkTheme)

                            //设置屏幕方向为竖屏
//                            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                        }
                    },
                    onRouteChanged = { data ->
//                        Log.d("TAG", "Mainactivity route changed: ${data.route}")
                        if (data.route!!.startsWith(SHEET_DETAIL_ROUTE) )
                        {
                            //状态栏文字白色
                            setStatusBarDarkMode()
                        } else {
                            //其他界面，状态栏颜色，根据系统自动设置
                            setStatusBarAutoMode(darkTheme)
                        }

//                        if (data.route!!.startsWith(MAIN_ROUTE)) {
//                            //取消屏幕方向
//                            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
//                        } else {
//                            //设置屏幕方向为竖屏
//                            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
//                        }
                    }
                )
//                SplashRoute()
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                Greeting(
//                       name = "Android",
//                      modifier = Modifier.padding(innerPadding)
//                   )
//                }
//                MyButton(R.string.agree)
            }
        }

        viewModel.loadSplashAd()
        viewModel.loadUserData()

        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent?) {
        intent?.let { intent ->
            //            Timber.d("processIntent: ${intent.extras.toString()}")
            intent.extras?.getString(Constant.EXTRA_MEDIA_ID)?.run {
                if (::navController.isInitialized) {
                    navController.navigateToMusicPlayer()
                } else {
//                    //延迟处理
//                    lifecycleScope.launch {
//                        delay(2000)
//                        navController.navigateToMusicPlayer()
//                    }
                    lifecycleScope.launch {
                        while (!::navController.isInitialized) {
                            delay(10)
                        }
                        delay(50)
                        navController.navigateToMusicPlayer()
                    }
                }
            }

//            intent.getStringExtra(Constant.EXTRA_REQUEST_DRAW_OVERLAYS_PERMISSION)?.run {
//                //请求悬浮权限
//
//                //请求获取权限，当然真实项目中，可以先显示一个提示对话框
//                //告诉用户为什么需要该权限，然后在调用方法获取
//                requestDrawOverlays()
//            }
        }
    }

    private fun toOpenIntent(data: String) {
        try {
            //应用
            val intent = Intent(Intent.ACTION_VIEW)
           // val uri = Uri.parse(data)
            val uriString = "https://www.baidu.com/"
            val uri = Uri.parse(uriString)

            intent.data = uri
            startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            //没有安装对应的应用
            Toast.makeText(this, R.string.not_found_activity, Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * 状态栏文字白色
     */
    private fun setStatusBarDarkMode() {
        enableEdgeToEdge(statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT))
    }

    private fun setStatusBarAutoMode(darkTheme: Boolean) {
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(
                android.graphics.Color.TRANSPARENT,
                android.graphics.Color.TRANSPARENT,
            ) {
                darkTheme
            },
//                    navigationBarStyle = SystemBarStyle.auto(
//                        lightScrim,
//                        darkScrim,
//                    ) { darkTheme },
        )
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Composable
fun MyButton(title: Int,modifier: Modifier = Modifier){
    Button(
        onClick = { Log.d("MyButton", "MyButton: ")}, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
    ) {
        Text(text = stringResource(id = title))
    }
}

/**
 * 返回true表示使用了动态颜色 [uiState].
 */
@Composable
private fun shouldUseDynamicTheming(
    uiState: MainActivityUiState,
): Boolean = when (uiState) {
    MainActivityUiState.Loading -> false
    is MainActivityUiState.Success -> uiState.userData.useDynamicColor
}

/**
 * 返回true表示使用了深色主题
 */
@Composable
private fun shouldUseDarkTheme(
    uiState: MainActivityUiState,
): Boolean = when (uiState) {
    MainActivityUiState.Loading -> isSystemInDarkTheme()
    is MainActivityUiState.Success -> when (uiState.userData.darkThemeConfig) {
        DarkThemeConfig.FOLLOW_SYSTEM -> isSystemInDarkTheme()
        DarkThemeConfig.LIGHT -> false
        DarkThemeConfig.DARK -> true
    }
}
