package com.quick.app.feature.publishfeed

import android.annotation.SuppressLint
import android.app.Activity
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cn.qhplus.emo.photo.activity.PhotoPickerActivity
import cn.qhplus.emo.photo.activity.getPhotoPickResult
import cn.qhplus.emo.photo.coil.CoilMediaPhotoProviderFactory
import coil.compose.AsyncImage
import com.quick.app.R
import com.quick.app.core.design.component.MyLoadingDialog
import com.quick.app.core.design.theme.SpaceExtraMedium
import com.quick.app.core.design.theme.SpaceMediumHeight
import com.quick.app.core.design.theme.SpaceOuter
import com.quick.app.core.design.theme.SpaceSmall

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
fun PublishFeedRoute(
    finishPage: () -> Unit,
    viewModel: PublishFeedViewModel = hiltViewModel(),
): Unit {
    val context = LocalContext.current

    val loadingString by viewModel.loadingString.collectAsStateWithLifecycle()
    val medias by viewModel.medias.collectAsState()
    val pickedItems by viewModel.pickedItems.collectAsState()

    // 注册选择媒体回调
    val pickLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                it.data?.getPhotoPickResult()?.let { ret ->
                    viewModel.setMedias(ret.list.map {
                        it.uri
                    })
                }
            }
        }

    var mediaRowWidth by remember { mutableStateOf(0) }

    val mediaItemDivider = with(LocalDensity.current) { 5.dp.toPx() * 2 }

    var mediaItemWidth = with(LocalDensity.current) {
        ((mediaRowWidth - mediaItemDivider) / 3).toDp()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                ),
                title = {
                    Text(text = stringResource(id = R.string.create_feed))
                },
                navigationIcon = {
                    IconButton(onClick = finishPage) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                actions = {
                    TextButton(
                        onClick = viewModel::onSaveClick
                    ) {
                        Text(
                            text = stringResource(id = R.string.publish)
                        )
                    }
                }
            )
        },
    ) { paddingValues ->

        val scrollState = rememberScrollState()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(start = SpaceOuter, end = SpaceOuter, bottom = SpaceOuter)
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .onGloballyPositioned {
                        mediaRowWidth = it.size.width
                    }
            ) {
                val content by viewModel.content.collectAsState()

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                ) {
                    TextField(
                        value = content,
                        onValueChange = {
                            if (it.length <= 140) {
                                viewModel.onContentChanged(it)
                            }
                        },
                        placeholder = {
                            Text(text = stringResource(id = R.string.hint_feed))
                        },
                        modifier = Modifier
                            .fillMaxSize()
                    )

                    Text(
                        "${content.length}/140",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline,
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(end = SpaceOuter, bottom = SpaceOuter)
                    )
                }

                SpaceMediumHeight()

                //媒体
                medias.chunked(3).forEachIndexed { index, list ->
                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        list.forEachIndexed { subIndex, item ->
                            if (item is Uri) {
                                //选择的图片
                                Box(
                                    modifier = Modifier
                                        .padding(
                                            start = if (subIndex == 0) 0.dp else SpaceSmall,
                                            top = SpaceSmall,
                                        )
                                        .width(mediaItemWidth)
                                        .aspectRatio(1f)
                                        .clip(MaterialTheme.shapes.extraSmall)
                                ) {
                                    AsyncImage(
                                        model = item,
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .fillMaxSize(),
                                    )

                                    Image(
                                        painter = painterResource(id = R.drawable.close_circle),
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .size(29.dp)
                                            .clickable {
                                                viewModel.removeMedia(item)
                                            }
                                            .padding(SpaceExtraMedium)
                                            .align(Alignment.TopEnd)
                                    )
                                }
                            } else {
                                //添加按钮
                                Image(
                                    painter = painterResource(id = item as Int),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .padding(
                                            start = if (index == 0) 0.dp else SpaceSmall,
                                            top = SpaceSmall,
                                        )
                                        .width(mediaItemWidth)
                                        .aspectRatio(1f)
                                        .clip(MaterialTheme.shapes.extraSmall)
                                        .clickable {
                                            pickLauncher.launch(
                                                PhotoPickerActivity.intentOf(
                                                    context,
                                                    enableOrigin = false,
                                                    pickLimitCount = 9,
                                                    pickedItems = pickedItems,
                                                    factoryCls = CoilMediaPhotoProviderFactory::class.java // 可自定义图片加载器
                                                )
                                            )
                                        }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    loadingString?.let {
        MyLoadingDialog(
            title = it,
            onDismissRequest = {

            }
        )
    }

    LaunchedEffect(viewModel.finish.value) {
        if (viewModel.finish.value) {
            finishPage()
        }
    }
}
