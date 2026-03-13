package com.quick.app.feature.lyric

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.quick.app.R
import com.quick.app.core.design.theme.SpacerOuterWidth
import com.quick.app.core.extension.asFormatTimeString
import com.quick.app.core.extension.clickableNoRipple
import com.quick.app.feature.lyricparser.Lyric
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.ceil

private var setNotDragJob: Job? = null

@Composable
fun MyLyricList(
    data: Lyric,
    currentPosition: Long,
    onLyricPlayClick: (Long) -> Unit,
    modifier: Modifier = Modifier
): Unit {
    val listState = rememberLazyListState()

    var currentIndex by rememberSaveable {
        mutableIntStateOf(0)
    }

    val coroutineScope = rememberCoroutineScope()

    var isProgrammaticScroll by remember { mutableStateOf(false) }

    var lyricListCenterY by rememberSaveable {
        mutableIntStateOf(0)
    }

    //歌词填充多个占位数据
    var lyricPlaceholderSize by rememberSaveable {
        mutableIntStateOf(0)
    }

    var lyricLineHeight = with(LocalDensity.current) {
        40.dp.toPx()
    }

    var currentLyricStartTime by rememberSaveable {
        mutableLongStateOf(0)
    }

    var draging by rememberSaveable {
        mutableStateOf(false)
    }

    var lyricCurrentWordIndex by rememberSaveable {
        mutableIntStateOf(-1)
    }

    var wordPlayedTime by rememberSaveable {
        mutableLongStateOf(0L)
    }

    Box(modifier = modifier
        .fillMaxSize()
        .onGloballyPositioned { layoutCoordinates ->
            if (lyricListCenterY == 0) {
                var lyricListHeight = layoutCoordinates.size.height
                lyricListCenterY = (lyricListHeight / 2 - lyricLineHeight / 2).toInt()

                lyricPlaceholderSize = ceil(lyricListHeight / 2 / lyricLineHeight).toInt()
            }
        }
        //避免onGloballyPositioned重复调用
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize(),
        ) {
            items(lyricPlaceholderSize) {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                )
            }
            itemsIndexed(data.datum) { index, lyricLine ->
//                Text(
//                    text = lyricLine.data,
//                    style = MaterialTheme.typography.bodyLarge,
//                    color =
//                    if (currentIndex == index)
//                        Color.Red else
//                        Color.White,
//                    textAlign = TextAlign.Center,
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(40.dp),
//                )
                LyricLineView(
                    data = lyricLine,
                    selected = currentIndex == index,
                    accurate = data.accurate,
                    lyricCurrentWordIndex = lyricCurrentWordIndex,
                    wordPlayedTime = wordPlayedTime,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp),
                )
            }
            items(lyricPlaceholderSize) {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                )
            }
        }

        if (draging && currentLyricStartTime != -1L) {
            DragStateView(
                currentLyricStartTime = currentLyricStartTime,
                onLyricPlayClick = {
                    cancelSetNotDragJob()
                    draging = false

                    onLyricPlayClick(currentLyricStartTime)
                },
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }

    LaunchedEffect(key1 = currentPosition) {
        //根据播放时间从歌词列表查询是哪一行歌词
        for (i in data.datum.indices) {
            val item = data.datum[i]
            if (currentPosition in item.startTime until item.endTime) {
                currentIndex = i
                break
            }
        }


        if (data.accurate) {
            //这一行的开始时间
            var lyricLine = data.datum[currentIndex]
            var startTime = lyricLine.startTime

            var isFoundWord = false
            for (index in lyricLine.words.indices) {
                val wordDuration = lyricLine.wordDurations[index]

                //累加时间
                startTime += lyricLine.wordDurations[index]

                if (currentPosition < startTime) {
                    //如果进度小于累加的时间
                    //就是这个索引
                    lyricCurrentWordIndex = index

                    //计算当前字已经播放的时间
                    wordPlayedTime = wordDuration - (startTime - currentPosition)

//                    Timber.d("item lyric selected ${currentPosition} ${startTime} ${currentIndex} ${index} ${wordPlayedTime} ${lyricLine.data}")
                    isFoundWord = true
                    break
                }
            }

            if (!isFoundWord) {
                lyricCurrentWordIndex = -1
            }
        }
    }

    // 监听当前行，并滚动到当前行
    LaunchedEffect(currentIndex) {
        if (draging) {
            return@LaunchedEffect
        }

        coroutineScope.launch {
            isProgrammaticScroll = true
            listState.animateScrollToItem(
                index = currentIndex + lyricPlaceholderSize,
                scrollOffset = -lyricListCenterY,
            )
        }
    }

    //监听列表开始滚动
    LaunchedEffect(listState) {
        snapshotFlow { listState.isScrollInProgress }
            .collect { isScrolling ->
                when {
                    isProgrammaticScroll -> {
                        // 这里处理程序触发的滚动逻辑
                        if (!isScrolling) {
                            // 当程序触发的滚动结束时，重置标志
                            isProgrammaticScroll = false
                        }
                    }

                    isScrolling -> {
                        // 这里处理用户触发的滚动逻辑
//                        Timber.d("lyric list user scrolling")
                        draging = true
                    }

                    else -> {
                        // 这里处理滚动停止的逻辑
//                        Timber.d("lyric list user scroll end")

                        //3秒钟后停止滚动
                        cancelSetNotDragJob()
                        setNotDragJob = coroutineScope.launch {
                            delay(3000)
                            draging = false
                        }
                    }
                }
            }
    }

    LaunchedEffect(listState) {
        // 监听滚动状态和布局信息的变化
        snapshotFlow {
            // 计算中心点
            //解释viewportStartOffset
            val center =
                listState.layoutInfo.viewportStartOffset + listState.layoutInfo.viewportSize.height / 2

            // 找到最接近中心的项目
            listState.layoutInfo.visibleItemsInfo.minByOrNull {
                abs((it.offset + it.size / 2) - center)
            }?.index
        }.collect { centerIndex ->
            // 当中心项索引变化时处理（例如打印）
            centerIndex?.let { index ->
                val index = index - lyricPlaceholderSize
                if (index in data.datum.indices) {
//                    Timber.d("中心项的索引: $index")
                    currentLyricStartTime = data.datum[index].startTime
                } else {
                    currentLyricStartTime = -1
                }
            }
        }
    }

    LaunchedEffect(data.songId) {
        //切换了音乐

        //取消延迟滚动
        cancelSetNotDragJob()

        //马上滚动
        draging = false
    }

}

fun cancelSetNotDragJob() {
    setNotDragJob?.let { it.cancel() }
}

@Composable
fun DragStateView(
    currentLyricStartTime: Long,
    onLyricPlayClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        SpacerOuterWidth()
        Text(
            text = currentLyricStartTime.asFormatTimeString(),
            color = Color.LightGray,
            style = MaterialTheme.typography.bodySmall
        )
        SpacerOuterWidth()
        Spacer(
            modifier = Modifier
                .weight(1f)
                .height(1.dp)
                .background(Color.LightGray)
        )

        Image(
            painter = painterResource(
                id = R.drawable.music_play,

                ),
            colorFilter = ColorFilter.tint(Color.LightGray),
            contentDescription = null,
            modifier = Modifier
                .size(40.dp)
                .clickableNoRipple {
                    onLyricPlayClick()
                }
                .padding(12.dp)
        )
    }
}