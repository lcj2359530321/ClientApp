package com.quick.app.feature.mediaplayer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.RestoreFromTrash
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.media3.common.MediaItem
import com.quick.app.R
import com.quick.app.core.database.model.SongEntity
import com.quick.app.core.design.theme.LocalArrowColor
import com.quick.app.core.design.theme.LocalDividerColor
import com.quick.app.core.design.theme.SpaceExtraSmall
import com.quick.app.core.design.theme.SpaceExtraSmall2
import com.quick.app.core.design.theme.SpaceOuter
import com.quick.app.core.design.theme.SpaceSmall
import com.quick.app.core.extension.clickableNoRipple

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyMusicListDialog(
    datum: List<SongEntity>,
    nowPlaying: MediaItem,
    onClearPlayListClick: () -> Unit,
    onItemMusicDeleteClick: (Int) -> Unit,
    onItemPlayListClick: (Int) -> Unit,
    onDismissRequest: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(
    )
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
    ) {
        Column(

            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = SpaceOuter,
                        end = SpaceSmall,
                        top = SpaceExtraSmall2,
                        bottom = SpaceExtraSmall2
                    ),
            ) {
                Text(
                    text = stringResource(id = R.string.playlist),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = onClearPlayListClick) {
                    Icon(
                        imageVector = Icons.Default.RestoreFromTrash,
                        contentDescription = null,
                        tint = LocalArrowColor.current,
                    )
                }
            }

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(SpaceExtraSmall)
                    .background(
                        LocalDividerColor.current
                    )
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                itemsIndexed(datum) { index, it ->
                    MyMusicListItem(
                        data = it,
                        nowPlaying = nowPlaying,
                        onItemDeleteClick = {
                            onItemMusicDeleteClick(index)
                        },
                        modifier = Modifier.clickableNoRipple {
                            onItemPlayListClick(index)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun MyMusicListItem(
    data: SongEntity,
    nowPlaying: MediaItem,
    onItemDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(
            start = SpaceOuter,
            end = SpaceSmall,
            top = SpaceExtraSmall2,
            bottom = SpaceExtraSmall2
        ),
    ) {
        Text(
            text = "${data.title} - ${data.artist}",
            color = if (nowPlaying.mediaId == data.id)
                MaterialTheme.colorScheme.primary
            else
                MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
        IconButton(onClick = onItemDeleteClick) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = null,
                tint = LocalArrowColor.current,
            )
        }
    }
}
