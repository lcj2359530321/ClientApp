package com.quick.app.feature.discovery

import DiscoveryPreviewParameterData.SONGS
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.quick.app.R
import com.quick.app.core.design.component.HorizontalPagerIndicator
import com.quick.app.core.design.component.MyErrorView
import com.quick.app.core.design.component.MyLoading
import com.quick.app.core.design.component.MyLottieLoadingView
import com.quick.app.core.design.component.MySweetError
import com.quick.app.core.design.component.MySweetSuccess
import com.quick.app.core.design.theme.ArrowIcon
import com.quick.app.core.design.theme.SpaceExtraMedium
import com.quick.app.core.design.theme.SpaceLargeWidth
import com.quick.app.core.design.theme.SpaceMedium
import com.quick.app.core.design.theme.SpaceOuter
import com.quick.app.core.design.theme.SpaceSmallHeight
import com.quick.app.core.model.Ad
import com.quick.app.core.model.ButtonViewData
import com.quick.app.core.model.Song
import com.quick.app.core.model.ViewData
import com.quick.app.feature.sheet.ItemSheet
import com.quick.app.feature.sheet.ItemSheetGrid
import com.quick.app.feature.song.compoent.ItemSong
import com.quick.app.util.ResourceUtil
import com.quick.app.util.SuperDateUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun DiscoveryRoute(
    toSheetDetail: (String) -> Unit,
    toggleDrawer: () -> Unit,
    toMusicPlayer: () -> Unit,
    toUri: (String) -> Unit,
    toSearch: () -> Unit,
    viewModel: DiscoveryViewModel = hiltViewModel()
){
    val uiState by viewModel.uiState.collectAsState()

    DiscoveryScreen(
        uiState = uiState,
        toggleDrawer = toggleDrawer,
        toSheetDetail = toSheetDetail,
        toUri = toUri,
        toSearch = toSearch,
        onSongClick = viewModel::onSongClick,
        loadData = viewModel::loadData,
        resetUiState = viewModel::resetUiState,
    )

    LaunchedEffect(viewModel.toMusicPlayer.value) {
        if (viewModel.toMusicPlayer.value) {
            toMusicPlayer()
            viewModel.clearMusicPlayer()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscoveryScreen(
    uiState: DiscoveryUiState = DiscoveryUiState(),
    toggleDrawer: () -> Unit = {},
    toSearch: () -> Unit = {},
    toSheetDetail: (String) -> Unit = {},
    toUri: (String) -> Unit = {},
    loadData: () -> Unit = {},
    resetUiState: () -> Unit = {},
    onSongClick: (List<Song>, Int) -> Unit = { _, _ -> },
    songs: List<Song> = listOf(),
) {
    val gridState = rememberLazyGridState()

    val pullToRefreshState = rememberPullToRefreshState()

    val isRefreshIdle = uiState.refreshState == 0 || uiState.refreshState == 2

    if (isRefreshIdle) {
        LaunchedEffect(Unit) {
            //结束刷新
            pullToRefreshState.endRefresh()
        }
    }

    if (pullToRefreshState.isRefreshing) {
        LaunchedEffect(pullToRefreshState.isRefreshing) {
            loadData()
        }
    }

    Scaffold (
        topBar = {
            DiscoveryTopBar(
                toggleDrawer,toSearch
            )
        },

        //排除底部导航栏边距
        contentWindowInsets = ScaffoldDefaults
            .contentWindowInsets
            .exclude(WindowInsets.navigationBars)
      //      .exclude(WindowInsets.ime)
    ){ paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(pullToRefreshState.nestedScrollConnection)
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .padding(paddingValues)
        ){
            LazyVerticalGrid(
                state = gridState,
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(
                    horizontal = SpaceOuter
                ),
                verticalArrangement = Arrangement.spacedBy(SpaceExtraMedium),
                horizontalArrangement = Arrangement.spacedBy(SpaceExtraMedium),
                modifier = Modifier.fillMaxSize()
            ) {
                uiState.topDatum.forEach { viewData ->
                    if (viewData.ads != null) {
                        //轮播图
                        item(span = {
                            GridItemSpan(
                                maxLineSpan
                            )
                        }) {
                            DiscoveryBanner(
                                data = viewData.ads!!,
                                onAdClick = { ad ->
                                    ad.uri?.let { adUri ->
                                        toUri(adUri)
                                    }
                                }
                            )
                        }
                    } else if (viewData.buttons != null) {
                        //快捷按钮
                        item(span = {
                            GridItemSpan(
                                maxLineSpan
                            )
                        }) {
                            DiscoveryButton(
                                data = viewData.buttons!!
                            )
                        }
                    } else if (viewData.sheets != null) {
                        item(
                            span = {
                                GridItemSpan(
                                    maxLineSpan
                                )
                            }
                        ) {
                            ItemDiscoveryTitle(
                                title = R.string.recommend_sheet
                            )
                        }

                        items(viewData.sheets) { sheet ->
                            ItemSheetGrid(
                                data = sheet,
                                toSheetDetail = {
                                    toSheetDetail(sheet.id)
                                },
                            )
                        }
                    } else if (viewData.songs != null) {
                        item(
                            span = {
                                GridItemSpan(
                                    maxLineSpan
                                )
                            }
                        ) {
                            ItemDiscoveryTitle(
                                title = R.string.recommend_song
                            )
                        }

                        itemsIndexed(viewData.songs,
                            span = { _, _ ->
                                GridItemSpan(maxLineSpan)
                            }) { index, item ->
                            ItemSong(data = item, modifier = Modifier.clickable {
                                onSongClick(viewData.songs, index)
                            })
                        }
                    }
                }
            }

            PullToRefreshContainer(
                modifier = Modifier.align(Alignment.TopCenter),
                state = pullToRefreshState,
            )

            if (uiState.topDatum.isNotEmpty()) {
                //有数据

                // 才用提示
                if (uiState.refreshState == 3) {
                    MySweetSuccess(
                        stringResource(
                            id = R.string.refresh_success_new_data_added,
                            3
                        )
                    )
                    resetUiState()
                } else if (uiState.refreshState == 2) {
                    MySweetError(message = uiState.exception!!.tipString!!)
                }
            } else {
                //没有数据

                //显示占位控件
                if (uiState.refreshState == 0) {
                    MyErrorView(
                        message = stringResource(id = R.string.empty_data),
                        icon = R.drawable.bg_empty,
                        onRetryClick = loadData,
                    )
                } else if (uiState.refreshState == 1) {
                 //   MyLoading()
                    MyLottieLoadingView()
                } else if (uiState.refreshState == 2) {
                    MyErrorView(exception = uiState.exception!!, onRetryClick = loadData)
                }
            }
        }
    }
}




/**
 * 发现界面顶部标题栏
 */
@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun DiscoveryTopBar(toggleDrawer: () -> Unit,toSearch: () -> Unit) {
    CenterAlignedTopAppBar(
        navigationIcon = {
            IconButton(onClick = toggleDrawer) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = null,
                    modifier = Modifier.size(36.dp)
                )
            }
        },
        title = {
            Row (
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(38.dp)
                    .clip(RoundedCornerShape(50.dp))
                    .background(Color.Gray)
                    .clickable{
                        toSearch()
                    }
            ){
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.outline, //动态设置颜色
                    modifier = Modifier.size(18.dp),
                )
                Text(
                    text = stringResource(id = R.string.search_tip),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline,
                )
            }
        },
        actions = {
            Icon(
                painter = painterResource(id = R.drawable.message),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .padding(horizontal = SpaceExtraMedium)
                    .size(28.dp),
            )
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors().copy(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    )
}

@Composable
fun ItemDiscoveryTitle(title: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(
            text = stringResource(id = title),
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
                .padding(vertical = SpaceOuter)
                .weight(1f),
        )

        Text(
            text = stringResource(id = R.string.show_more),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.outline,
            modifier = Modifier
        )

        ArrowIcon()
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DiscoveryBanner(
    data: List<Ad>,
    onAdClick: (Ad) -> Unit = {},
) {
    Card(
    ) {
        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier
                .fillMaxWidth()
                //图片的宽度/高度
                .aspectRatio(2.571f)
                .clip(MaterialTheme.shapes.extraSmall)
        ) {
            val pagerState = rememberPagerState(pageCount = {
                data.size
            })

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxSize()
            ) { page ->
                val item = data[page]
                AsyncImage(
                    model = ResourceUtil.r(item.icon),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onAdClick(item)
                        },
                )
            }

            HorizontalPagerIndicator(
                pagerState = pagerState,
                pageCount = data.size,
                modifier = Modifier.padding(SpaceMedium),
            )

            val scope = rememberCoroutineScope()
            val lifecycle = LocalLifecycleOwner.current.lifecycle
            LaunchedEffect(pagerState) {
                var job: Job? = null

                val observer = object : DefaultLifecycleObserver {
                    override fun onResume(owner: LifecycleOwner) {
                        job = scope.launch {
                            while (true) {
                                delay(3000L)
                                val nextPage = (pagerState.currentPage + 1) % data.size
                                pagerState.animateScrollToPage(nextPage)
                            }
                        }
                    }

                    override fun onPause(owner: LifecycleOwner) {
                        super.onPause(owner)
                        job?.cancel()
                    }
                }

                lifecycle.addObserver(observer)
            }
        }
    }

}

@Composable
fun DiscoveryButton(
    data: List<ButtonViewData>
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = SpaceOuter),
    ) {
        itemsIndexed(data) { index, item ->
            DiscoveryButtonItem(
                data = item,
                modifier = Modifier
            )
            if (index != data.size - 1) {
                SpaceLargeWidth()
            }
        }
    }

}

@Composable
fun DiscoveryButtonItem(
    data: ButtonViewData,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
//            .padding(horizontal = SpaceMedium)
        ,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
        ) {
            AsyncImage(
                model = ResourceUtil.r(data.icon),
                contentDescription = null,
                modifier = Modifier
            )
            if (data.icon == "music/music_recommend_button.png") {
                //每日推荐
                Text(
                    text = SuperDateUtil.currentDayOfMonth().toString(),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(top = 7.dp)
                )
            }
        }
        SpaceSmallHeight()
        Text(
            text = data.title,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.outline,
        )
    }
}