package com.quick.app.feature.feed

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cn.qhplus.emo.photo.coil.CoilPhotoProvider
import cn.qhplus.emo.photo.ui.PhotoThumbnailWithViewer
import coil.compose.AsyncImage
import com.quick.app.MyAppState
import com.quick.app.R
import com.quick.app.core.design.theme.MyAppTheme
import com.quick.app.core.design.theme.SpaceExtraMedium
import com.quick.app.core.design.theme.SpaceExtraMediumHeight
import com.quick.app.core.design.theme.SpaceExtraSmall
import com.quick.app.core.design.theme.SpaceMediumHeight
import com.quick.app.core.design.theme.SpaceMediumWidth
import com.quick.app.core.design.theme.SpaceOuter
import com.quick.app.core.design.theme.SpaceSmall
import com.quick.app.core.design.theme.SpaceSmallHeight
import com.quick.app.core.design.theme.body2XLargeBold
import com.quick.app.core.design.theme.bodyXLarge
import com.quick.app.core.design.theme.md_theme_second_button_link
import com.quick.app.core.extension.clickableNoRipple
import com.quick.app.core.model.Feed
import com.quick.app.core.model.MediaResource
import com.quick.app.core.model.User
import com.quick.app.util.ResourceUtil
import com.quick.app.util.SuperDateUtil
import kotlinx.collections.immutable.toPersistentList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
fun FeedRoute(
    toPublishFeed: () -> Unit,
    viewModel: FeedViewModel = hiltViewModel(),
): Unit {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                ),
                title = {
                    Text(text = stringResource(id = R.string.mention))
                },
            )
        },
        //排除底部导航栏边距
        contentWindowInsets = ScaffoldDefaults
            .contentWindowInsets
            .exclude(WindowInsets.navigationBars),
        floatingActionButton = {
            FloatingActionButton(
                onClick = toPublishFeed,
            ) {
                Icon(Icons.Filled.Add, null)
            }
        }
    ) { paddingValues ->
        val datum by viewModel.datum.collectAsStateWithLifecycle()

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(SpaceExtraSmall),
            contentPadding = PaddingValues(vertical = SpaceExtraSmall),
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
        ) {
            items(datum) {
                ItemFeed(it)
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.loaData()
    }
}

@Composable
fun ItemFeed(
    data: Feed,
    deleteFeed: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(start = SpaceOuter, end = SpaceOuter, top = SpaceOuter, bottom = SpaceSmall)
    ) {
        AsyncImage(
            model = if (data.user!!.icon != null) ResourceUtil.r(data.user.icon!!) else null,
            error = painterResource(id = R.drawable.placeholder),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(46.dp)
                .clip(MaterialTheme.shapes.extraSmall),
        )

        SpaceMediumWidth()

        Column(
            modifier = Modifier.weight(1f)
        ) {
            //昵称
            Text(
                text = data.user.nicknameFormat,
                style = MaterialTheme.typography.body2XLargeBold,
                color = md_theme_second_button_link,
                modifier = Modifier
            )

            SpaceSmallHeight()

            //动态
            Text(
                text = data.content!!,
                style = MaterialTheme.typography.bodyXLarge,
                color = MaterialTheme.colorScheme.onSurface,
                lineHeight = 26.sp,
                modifier = Modifier
            )

            data.medias?.let {
                SpaceMediumHeight()

                //媒体
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    PhotoThumbnailWithViewer(
                        images = it.map {
                            CoilPhotoProvider(
                                ResourceUtil.r(it.uri).toUri(),
                                ratio = -1F,
                            )
                        }.toPersistentList(),
                    )
                }
            }

            data.position?.let {
                SpaceExtraMediumHeight()

                //位置
                Text(
                    text = "${data.city} ${data.position}",
                    style = MaterialTheme.typography.bodySmall,
                    color = md_theme_second_button_link,
                    modifier = Modifier
                        .clickableNoRipple {

                        }
                        .padding(top = SpaceExtraMedium)
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                //时间
                Text(
                    text = SuperDateUtil.commonFormat(data.created),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier
                        .padding(vertical = SpaceExtraMedium)
                )

                if (MyAppState.userId == data.user.id) {
                    //删除按钮
                    TextButton(
                        onClick = {
                            deleteFeed(data.id!!)
                        },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = md_theme_second_button_link
                        ),
                        modifier = modifier
                    ) {
                        Text(
                            text = stringResource(id = R.string.delete),
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
            }

        }
    }
}
