package com.quick.app.feature.sheet

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Headphones
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.quick.app.core.design.component.MyAsyncImage
import com.quick.app.core.design.theme.MyAppTheme
import com.quick.app.core.design.theme.SpaceExtraSmall2Width
import com.quick.app.core.design.theme.SpaceMedium
import com.quick.app.core.design.theme.SpaceSmall
import com.quick.app.core.model.Sheet
import com.quick.app.core.model.User
import com.quick.app.util.StringUtil

val sheetModifier = Modifier
    .fillMaxWidth()
    .aspectRatio(1f)

@Composable
fun ItemSheetGrid(
    data: Sheet,
    toSheetDetail: () -> Unit = {},
    modifier: Modifier = Modifier,
): Unit {
    Column(
        modifier
            .clip(MaterialTheme.shapes.small)
            .clickable {
                toSheetDetail()
            }
    ) {
        Box(
            modifier = sheetModifier
        ) {
            MyAsyncImage(
                model = data.icon,
                modifier = Modifier.fillMaxSize(),
            )

            //region 点击数
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = SpaceSmall, end = SpaceSmall)
            ) {
                Icon(
                    imageVector = Icons.Default.Headphones,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(13.dp),
                )

                SpaceExtraSmall2Width()

                Text(
                    text = StringUtil.formatCount(data.clicksCount),
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontSize = 12.sp
                    ),
                    color = MaterialTheme.colorScheme.onPrimary,
                )
            }
            //endregion

        }
        Column(
            modifier = Modifier.padding(
                SpaceMedium
            )
        ) {
            Text(
                text = data.title,
                minLines = 2,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

