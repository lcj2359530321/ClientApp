package com.quick.app.feature.createsheet

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.google.common.base.Strings
import com.quick.app.R
import androidx.compose.foundation.text2.BasicTextField2
import androidx.compose.foundation.text2.input.TextFieldLineLimits
import cn.qhplus.emo.photo.activity.PhotoClipperActivity
import cn.qhplus.emo.photo.activity.PhotoPickerActivity
import cn.qhplus.emo.photo.activity.getPhotoClipperResult
import cn.qhplus.emo.photo.activity.getPhotoPickResult
import cn.qhplus.emo.photo.coil.CoilMediaPhotoProviderFactory
import cn.qhplus.emo.photo.coil.CoilPhotoProvider
import com.quick.app.core.design.component.MyCenterTopAppBar
import com.quick.app.core.design.component.MySweetError
import com.quick.app.core.design.theme.SpaceExtraMedium
import com.quick.app.core.design.theme.SpaceExtraOuter
import com.quick.app.core.design.theme.SpaceExtraSmall
import com.quick.app.core.design.theme.SpaceOuter
import com.quick.app.core.model.SHEET_EMPTY
import com.quick.app.core.model.Sheet
import com.quick.app.util.ResourceUtil
import timber.log.Timber

@Composable
fun CreateSheetRoute(
    finishPage: () -> Unit,
    viewModel: CreateSheetViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val data by viewModel.data.collectAsStateWithLifecycle()

    val tipErrorRes by viewModel.tipErrorRes.collectAsStateWithLifecycle()
    val tipError by viewModel.tipError.collectAsStateWithLifecycle()
    val tipSuccessRes by viewModel.tipSuccessRes.collectAsStateWithLifecycle()

    //图片裁剪回调
    val cropLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                it.data?.getPhotoClipperResult()?.let { ret ->
                    //content://com.quick.app.fileprovider/emo_public_2/emo_photo/emo_photo_94054626248830.jpeg
                    Timber.d("cropLauncher ret: %s", ret.uri)

                    viewModel.updateIcon(ret.uri)
                }
            }
        }

    // 注册选择媒体回调
    val pickLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                it.data?.getPhotoPickResult()?.let { ret ->
                    //content://media/external/images/media/1000000036
                    Timber.d("pickLauncher ret: %s", ret.list.first().uri)

                    //裁剪图片
                    cropLauncher.launch(
                        PhotoClipperActivity.intentOf(
                            context,
                            CoilPhotoProvider(
                                ret.list.first().uri,
                                ratio = 0f
                            ),
                        )
                    )
                }
            }
        }


    CreateSheetScreen(
        finishPage = finishPage,
        data = data,
        onValueChange = viewModel::onValueChange,
        onSaveClick = viewModel::onSaveClick,
        onSelectImageClick = {
            pickLauncher.launch(
                PhotoPickerActivity.intentOf(
                    context,
                    enableOrigin = false,
                    pickLimitCount = 1,
                    factoryCls = CoilMediaPhotoProviderFactory::class.java // 可自定义图片加载器
                )
            )
        },
    )

    tipError?.let {
        MySweetError(
            message = it
        )
        viewModel.resetBaseState()
    }
    tipErrorRes?.let {
        MySweetError(
            message = stringResource(id = it)
        )
        viewModel.resetBaseState()
    }
    tipSuccessRes?.let {
        MySweetError(
            message = stringResource(id = it)
        )
        viewModel.resetBaseState()
    }

    LaunchedEffect(viewModel.finish.value) {
        if (viewModel.finish.value) {
            finishPage()
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateSheetScreen(
    data: Sheet = SHEET_EMPTY(),
    onValueChange: (Sheet) -> Unit = {},
    onSaveClick: () -> Unit = {},
    finishPage: () -> Unit = {},
    onSelectImageClick: () -> Unit = {},
): Unit {
    Scaffold(
        topBar = {
            MyCenterTopAppBar(
                finishPage = finishPage,
                titleText = stringResource(id = R.string.create_sheet),
                actions = {
                    TextButton(
                        onClick = onSaveClick
                    ) {
                        Text(
                            text = stringResource(
                                id = R.string.save
                            )
                        )
                    }
                }
            )

        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        val scrollState = rememberScrollState()

        Column(
            modifier = Modifier
                .imePadding()
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(paddingValues)
        ) {
            //封面
            MySettingRow(
                modifier = Modifier
                    .clickable {
                        onSelectImageClick()
                    }
            ) {
                MySettingTitle(
                    title = R.string.sheet_icon,
                    modifier = Modifier.weight(1f)
                )

                val iconModifier = Modifier
                    .padding(vertical = SpaceExtraOuter)
                    .size(64.dp)
                    .clip(MaterialTheme.shapes.extraSmall)

                if (data.icon != null) {
                    AsyncImage(
                        model = ResourceUtil.r(data.icon!!),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = iconModifier,
                    )
                } else {
                    Image(
                        painter = painterResource(
                            id = R.drawable.placeholder
                        ),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = iconModifier,
                    )
                }
            }

            MySettingRow(
                modifier = Modifier
            ) {
                MySettingTitle(
                    title = R.string.sheet_title,
                    modifier = Modifier.weight(1f)
                )

                MySettingInput(
                    value = data.title ?: "",
                    onValueChanged = {
                        onValueChange(
                            data.copy(
                                title = it
                            )
                        )
                    },
                    modifier = Modifier.weight(1f)
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(top = SpaceExtraSmall)
                    .fillMaxWidth()
                    .heightIn(min = 50.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(start = SpaceOuter, end = SpaceExtraMedium),

                ) {
                MySettingTitle(
                    title = R.string.sheet_description,
                    modifier = Modifier.weight(1f)
                )
            }

            //region 描述 输入框
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(horizontal = SpaceOuter),
            ) {
                BasicTextField(
                    value = data.detail ?: "",
                    onValueChange = {
                        onValueChange(
                            data.copy(
                                detail = it
                            )
                        )
                    },
                    textStyle = MaterialTheme.typography.bodyLarge,
                    singleLine = true,
                    modifier = Modifier.fillMaxSize()
                )
                if (Strings.isNullOrEmpty(data.detail)) {
                    Text(
                        text = stringResource(id = R.string.hint_sheet_description),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.outline,
                    )
                }
            }
            //endregion
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MySettingInput(
    value: String,
    onValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var focused by remember {
        mutableStateOf(false)
    }

    BasicTextField2(
        value = value,
        onValueChange = onValueChanged,
        textStyle = MaterialTheme.typography.bodyLarge.copy(
            textAlign = TextAlign.Right,
            color = MaterialTheme.colorScheme.outline,
        ),
        lineLimits = TextFieldLineLimits.SingleLine,
        modifier = modifier
            .onFocusChanged {
                focused = it.isFocused
            }
    )
}

@Composable
fun MySettingTitle(
    title: Int,
    modifier: Modifier = Modifier,
) {
    Text(
        text = stringResource(id = title),
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = modifier,
    )
}

@Composable
fun MySettingRow(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit = {},
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(top = SpaceExtraSmall)
            .fillMaxWidth()
            .heightIn(min = 50.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = SpaceOuter),

        ) {
        content()
    }
}
