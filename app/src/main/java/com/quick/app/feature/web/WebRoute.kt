package com.quick.app.feature.web

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Message
import android.util.Log
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.quick.app.core.design.component.MyCenterTopAppBar
import com.quick.app.core.design.component.MyLoading
import com.quick.app.util.Constant.HTML_DATA_END
import com.quick.app.util.Constant.HTML_DATA_START

@Composable
fun WebRoute(
    finishPage: () -> Unit,
    viewModel: WebViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    WebScreen(
        uiState = uiState,
        finishPage = finishPage,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WebScreen(
    finishPage: () -> Unit = {},
    uiState: WebUiState = WebUiState.Loading,
) {
    when (val uiState = uiState) {
        is WebUiState.Success -> {
            ContentView(uiState.data, finishPage = finishPage)
        }

        else -> {
            MyLoading()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentView(
    data: WebParam,
    finishPage: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    var webTitle by remember {
        mutableStateOf("")
    }
    var currentProgress by remember { mutableStateOf(0) }

    val webView = remember {
        WebView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )

            settings.apply {
                javaScriptEnabled = true
                // 其他常见设置
                loadsImagesAutomatically = true
                useWideViewPort = true
                loadWithOverviewMode = true
                setSupportZoom(true)
                builtInZoomControls = true
                displayZoomControls = false
                setSupportMultipleWindows(false)
                javaScriptCanOpenWindowsAutomatically = true
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    safeBrowsingEnabled = true
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    mediaPlaybackRequiresUserGesture = false
                }
                domStorageEnabled = true
            }

            webViewClient = WebViewClient()

            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                    if (url.startsWith("http://") || url.startsWith("https://")) {
                        view.loadUrl(url)
                        return false
                    } else {
                        // 使用Intent打开其他类型的链接
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        context.startActivity(intent)
                        return true
                    }
                }

                override fun onReceivedError(
                    view: WebView,
                    request: WebResourceRequest,
                    error: WebResourceError
                ) {
                    Log.d(TAG, "onReceivedError: ${error.description}")
                }
            }
            webChromeClient = object : WebChromeClient() {
                override fun onCreateWindow(
                    view: WebView?,
                    isDialog: Boolean,
                    isUserGesture: Boolean,
                    resultMsg: Message?
                ): Boolean {
//                            (resultMsg?.obj as WebView.WebViewTransport).webView = view
//                            resultMsg.sendToTarget()
//                            return true
                    val newWebView = WebView(context)
                    val transport = resultMsg?.obj as WebView.WebViewTransport
                    transport.webView = newWebView
                    resultMsg.sendToTarget()
                    return true
                }

                override fun onReceivedTitle(view: WebView, title: String) {
                    super.onReceivedTitle(view, title)
                    webTitle = title
                }

                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    super.onProgressChanged(view, newProgress)
                    currentProgress = newProgress
                    Log.d(TAG, "onProgressChanged: $newProgress")
                }
            }

            if (data.uri != null) {
                //loadUrl(data.uri!!)
                loadUrl("https://www.msn.cn/zh-cn/weather") //TODO
            } else {
                //加载字符串html
                loadDataWithBaseURL(
                    null,
                    "${HTML_DATA_START}${data.content!!}${HTML_DATA_END}",
                    "text/html",
                    "utf-8",
                    null
                )
            }
        }
    }

    Scaffold(
        topBar = {
            MyCenterTopAppBar(
                finishPage = {
                    if (webView.canGoBack()) {
                        webView.goBack()
                    } else {
                        finishPage()
                    }
                },
                titleText = webTitle,
                actions = {
                    IconButton(onClick = finishPage) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null
                        )
                    }
                }
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            AndroidView(
                factory = { _ ->
                    webView
                },
//                    update = {
//                        it.loadUrl("https://www.google.com")
//                    }
                modifier = Modifier
                    .fillMaxSize()
            )

            if (currentProgress < 100) {
                LinearProgressIndicator(
                    progress = { currentProgress * 1.0F },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

private const val TAG = "WebRoute"

