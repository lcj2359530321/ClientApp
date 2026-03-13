package com.quick.app.feature.song.component

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.quick.app.R
import com.quick.app.core.design.theme.LocalArrowColor
import com.quick.app.core.design.theme.MyAppTheme
import com.quick.app.core.design.theme.SpaceSmall
import com.quick.app.core.model.Song

/**
 * 歌单详情音乐item
 *
 * @param data 音乐
 * @param index 序号，从0开始
 * @param currentPlayMediaId 当前播放的音乐id
 */
@Composable
fun ItemSongSheet(
    data: Song,
    index: Int,
    isPlaying: Boolean,
    currentPlayMediaId: String = "",
    modifier: Modifier = Modifier
): Unit {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(
                end = SpaceSmall,
                top = SpaceSmall,
                bottom = SpaceSmall
            ),
    ) {
        Box(modifier = Modifier.size(50.dp)) {
            if (currentPlayMediaId == data.id) {
                //当前音乐
                if (isPlaying) {
                    ItemMusicPlayingAnimation(
                        Modifier
                            .align(Alignment.Center)
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.music_playing1),
                        contentDescription = "Animated Frame",
                        modifier = Modifier
                            .align(Alignment.Center)
                    )
                }
            } else {
                Text(
                    text = "${index + 1}",
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier
                        .align(Alignment.Center)
                )
            }
        }

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = data.title,
                color = if (currentPlayMediaId == data.id)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
            )
            Text(
                text = "${data.artist} - ${data.album}",
                color = MaterialTheme.colorScheme.outline,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
            )
        }

        IconButton(onClick = {

        }) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = null,
                tint = LocalArrowColor.current,
            )
        }
    }
}

@Composable
fun ItemMusicPlayingAnimation(
    modifier: Modifier = Modifier,
) {
    // 假设我们有三个矢量资源作为动画帧
    val imageVectors by rememberSaveable {
        mutableStateOf(
            listOf(
                R.drawable.music_playing1,
                R.drawable.music_playing2,
                R.drawable.music_playing3,
                R.drawable.music_playing4,
            )
        )
    }

    // 创建无限循环的动画
    val infiniteTransition = rememberInfiniteTransition(label = "MusicPlaying")
    val frameIndex by infiniteTransition.animateValue(
        initialValue = 0,
        targetValue = imageVectors.size - 1,
        typeConverter = Int.VectorConverter,
        animationSpec = infiniteRepeatable(
            // TweenSpec用于平滑过渡帧
            tween(durationMillis = 400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "MusicPlayingIndex"
    )

    // 显示当前帧
    Image(
        painter = painterResource(id = imageVectors[frameIndex]),
        contentDescription = "Animated Frame",
        modifier = modifier
    )
}

@Preview
@Composable
fun ItemMusicPlayingAnimationPreview() {
    MyAppTheme {
        ItemMusicPlayingAnimation()
    }
}