package com.quick.app.feature.profile

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import cn.qhplus.emo.photo.activity.PhotoClipperActivity
import cn.qhplus.emo.photo.activity.PhotoPickerActivity
import cn.qhplus.emo.photo.activity.getPhotoClipperResult
import cn.qhplus.emo.photo.activity.getPhotoPickResult
import cn.qhplus.emo.photo.coil.CoilMediaPhotoProviderFactory
import cn.qhplus.emo.photo.coil.CoilPhotoProvider
import coil.compose.AsyncImage
import com.quick.app.core.design.component.MyGenderDialog
import com.google.common.base.Strings
import com.quick.app.R
import com.quick.app.core.design.component.MyCenterTopAppBar
import com.quick.app.core.design.theme.ArrowIcon
import com.quick.app.core.design.theme.MyAppTheme
import com.quick.app.core.design.theme.SpaceExtraMedium
import com.quick.app.core.design.theme.SpaceExtraOuter
import com.quick.app.core.design.theme.SpaceExtraSmall
import com.quick.app.core.design.theme.SpaceExtraSmallHeight
import com.quick.app.core.design.theme.SpaceOuter
import com.quick.app.core.model.User
import com.quick.app.feature.createsheet.MySettingInput
import com.quick.app.feature.createsheet.MySettingRow
import com.quick.app.feature.createsheet.MySettingTitle
import com.quick.app.util.ResourceUtil
import timber.log.Timber

@Composable
fun ProfileRoute(
    finishPage: () -> Unit,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    val uiState by viewModel.uiState.collectAsState()
    val data by viewModel.data.collectAsState()
    val isShowGenderDialog by viewModel.isShowGenderDialog.collectAsState()

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

    ProfileScreen(
        uiState = uiState,
        data = data,
        finishPage = finishPage,
        onSaveClick = viewModel::onSaveClick,
        onUserValueChanged = viewModel::onUserValueChanged,
        showGenderDialog = viewModel::showGenderDialog,
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

    if (isShowGenderDialog) {
        MyGenderDialog(
            value = data.gender.toInt(),
            onSelectChange = {
                viewModel.onUserValueChanged(
                    data.copy(
                        gender = it.toString()
                    )
                )
                viewModel.dismissGenderDialog()
            },
            onDismissRequest = {
                viewModel.dismissGenderDialog()
            }
        )
    }

    LaunchedEffect(viewModel.finish.value) {
        if (viewModel.finish.value) {
            finishPage()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    uiState: ProfileUiState = ProfileUiState.Loading,
    data: User = User(),
    finishPage: () -> Unit = {},
    onSaveClick: () -> Unit = {},
    onUserValueChanged: (User) -> Unit = {},
    showGenderDialog: () -> Unit = {},
    onSelectImageClick: () -> Unit = {},
) {
    Scaffold(
        topBar = {
            MyCenterTopAppBar(
                finishPage = finishPage,
                titleText = stringResource(id = R.string.my_profile),
                actions = {
                    TextButton(
                        onClick = onSaveClick,
                    ) {
                        Text(
                            text = stringResource(id = R.string.save)
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
            when (val uiState = uiState) {
                is ProfileUiState.Success -> {
                    ContentView(
                        data = data,
                        onUserValueChanged = onUserValueChanged,
                        showGenderDialog = showGenderDialog,
                        onSelectImageClick = onSelectImageClick,
                    )
                }

                else -> {

                }
            }
        }
    }
}


@Composable
fun ContentView(
    data: User,
    onUserValueChanged: (User) -> Unit,
    showGenderDialog: () -> Unit,
    onSelectImageClick: () -> Unit,
) {
    //头像
    MySettingRow(
        modifier = Modifier
            .padding(top = SpaceExtraMedium)
            .clickable {
                onSelectImageClick()
            }
    ) {
        MySettingTitle(
            title = R.string.avatar,
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
                    id = R.drawable.default_avatar
                ),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = iconModifier,
            )
        }


    }

    //昵称
    MySettingRow(
        modifier = Modifier.padding(top = SpaceExtraMedium)
    ) {
        MySettingTitle(
            title = R.string.nickname,
            modifier = Modifier.weight(1f)
        )

        MySettingInput(
            value = data.nickname ?: "",
            onValueChanged = {
                onUserValueChanged(
                    data.copy(
                        nickname = it
                    )
                )
            },
            modifier = Modifier.weight(1f)
        )
    }
    SpaceExtraSmallHeight()
    //性别
    MySettingValue(
        title = R.string.gender,
        value = data.getGenderFormat(),
        modifier = Modifier
            .clickable {
                showGenderDialog()
            }
    )
    SpaceExtraSmallHeight()
    MySettingValue(
        title = R.string.birthday,
        value = data.birthday ?: "",
    )
    SpaceExtraSmallHeight()
    MySettingValue(
        title = R.string.area,
        value = data.areaDisplay,
        modifier = Modifier
    )

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(top = SpaceExtraMedium)
            .fillMaxWidth()
            .heightIn(min = 50.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(start = SpaceOuter, end = SpaceExtraMedium),

        ) {
        MySettingTitle(
            title = R.string.description,
            modifier = Modifier.weight(1f)
        )
    }

    //region 个人描述 输入框
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
                onUserValueChanged(
                    data.copy(
                        detail = it
                    )
                )
            },
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.outline,
            ),
            singleLine = true,
            modifier = Modifier.fillMaxSize()
        )
        if (Strings.isNullOrEmpty(data.detail)) {
            Text(
                text = stringResource(id = R.string.hint_description),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.outline,
            )
        }
    }
    //endregion

    MySettingValue(
        title = R.string.phone,
        value = data.phone ?: "",
        modifier = Modifier
            .padding(top = SpaceExtraMedium),
    )
    SpaceExtraSmallHeight()
    MySettingValue(
        title = R.string.email,
        value = data.email ?: "",
    )
}

@Composable
fun MySettingValue(
    title: Int,
    value: String,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(top = SpaceExtraSmall)
            .fillMaxWidth()
            .heightIn(min = 50.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(start = SpaceOuter, end = SpaceExtraMedium),

        ) {
        MySettingTitle(
            title = title,
            modifier = Modifier.weight(0.5f)
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

