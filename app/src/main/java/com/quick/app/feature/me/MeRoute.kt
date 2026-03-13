package com.quick.app.feature.me

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.quick.app.R
import com.quick.app.core.design.component.MyCenterTopAppBar
import com.quick.app.core.design.theme.LocalDividerColor
import com.quick.app.core.design.theme.SpaceExtraSmallHeight
import com.quick.app.core.design.theme.SpaceOuter
import com.quick.app.core.design.theme.SpaceSmall
import com.quick.app.core.extension.clickableNoRipple
import com.quick.app.core.model.Sheet
import com.quick.app.feature.main.MySettingItem
import com.quick.app.feature.sheet.ItemSheet
import com.quick.app.ui.MyAppUiState
import com.quick.app.util.Constant

@Composable
fun MeRoute(
    appUiState: MyAppUiState,
    toLogin: () -> Unit,
    toSheetDetail: (String) -> Unit,
    toLocalMusic: () -> Unit,
    toScanLocalMusic: () -> Unit,
    toEditSheet: (String) -> Unit,
    viewModel: MeViewModel = hiltViewModel()
) {
    val isLogin by appUiState.isLogin.collectAsState()
    val createDatum by viewModel.createDatum.collectAsState()
    val collectDatum by viewModel.collectDatum.collectAsState()

    MeScreen(
        toLocalMusic = toLocalMusic,
        toScanLocalMusic = toScanLocalMusic,
        localMusicCount = 0,
        toEditSheet = {
            if (isLogin) {
                toEditSheet(it)
            } else {
                toLogin()
            }
        },
        createDatum = createDatum,
        collectDatum = collectDatum,
        toSheetDetail = toSheetDetail
    )

    LaunchedEffect(Unit) {
        viewModel.loadData()
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeScreen(
    toLocalMusic: () -> Unit = {},
    toScanLocalMusic: () -> Unit = {},
    localMusicCount: Int = 0,
    toEditSheet: (String) -> Unit = {},
    createDatum: List<Sheet> = listOf(),
    collectDatum: List<Sheet> = listOf(),
    toSheetDetail: (String) -> Unit = {},
) {
    Scaffold(
        topBar = {
            MyCenterTopAppBar(
                titleText = stringResource(id = R.string.me),
            )
        },
        //排除底部导航栏边距
        contentWindowInsets = ScaffoldDefaults
            .contentWindowInsets
            .exclude(WindowInsets.navigationBars),
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item {
                SpaceExtraSmallHeight()
                MySettingItem(
                    title = R.string.local_music,
                    value = "${localMusicCount}首",
                    onClick = {
                        if (localMusicCount == 0) {
                            toScanLocalMusic()
                        } else {
                            toLocalMusic()
                        }
                    }
                )
                SpaceExtraSmallHeight()
            }
            item {
                MySettingItem(
                    title = R.string.download_manager,
                )
                SpaceExtraSmallHeight()
            }

            item {
                MySettingItem(
                    title = R.string.play_history,
                )
                SpaceExtraSmallHeight()
            }
            item {
                MySettingItem(
                    title = R.string.my_radio,
                )
                SpaceExtraSmallHeight()
            }
            item {
                MySettingItem(
                    title = R.string.my_collect,
                )
            }

            item {
                MySettingTitleSmall(title = R.string.created_sheet, onAddClick = {
                    toEditSheet(Constant.VALUE_NO_STRING)
                })
            }
            items(createDatum) {
                ItemSheet(
                    data = it,
                    modifier = Modifier.clickable {
                        toSheetDetail(it.id)
                    }
                )
                //列表最后不添加分割线
                if (it != createDatum.last()) {
                    SpaceExtraSmallHeight()
                }
            }
            item {
                MySettingTitleSmall(title = R.string.collected_sheet)
            }
            items(collectDatum) {
                ItemSheet(
                    data = it,
                    modifier = Modifier.clickable {
                        toSheetDetail(it.id)
                    }
                )
                if (it != collectDatum.last()) {
                    SpaceExtraSmallHeight()
                }
            }
        }
    }
}

@Composable
fun MySettingTitleSmall(
    title: Int,
    onAddClick: (() -> Unit)? = null,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(LocalDividerColor.current)
            .padding(horizontal = SpaceOuter, vertical = SpaceSmall),
    ) {
        Text(
            text = stringResource(id = title),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )

        onAddClick?.let {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.outline,
                modifier = Modifier.clickableNoRipple {
                    it()
                }
            )
        }
    }
}