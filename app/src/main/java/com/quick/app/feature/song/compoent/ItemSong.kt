package com.quick.app.feature.song.compoent

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import com.google.common.io.Resources
import com.quick.app.R
import com.quick.app.core.design.component.MyAsyncImage
import com.quick.app.core.design.theme.LocalDividerColor
import com.quick.app.core.design.theme.SpaceMedium
import com.quick.app.core.design.theme.SpaceSmallHeight
import com.quick.app.core.design.theme.extraSmallRoundedCornerShape
import com.quick.app.core.model.Song
import com.quick.app.util.ResourceUtil

/**
 * 单曲Item
 */
@Composable
fun ItemSong(data: Song, modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        MyAsyncImage(
            model = data.icon,
            modifier = Modifier.size(50.dp)
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = SpaceMedium)
        ) {
            Text(
                text = data.title,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface,
            )

            SpaceSmallHeight()

            Text(
                text = "${data.artist} - ${data.album}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}