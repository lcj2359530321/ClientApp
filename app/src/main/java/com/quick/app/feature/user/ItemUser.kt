package com.quick.app.feature.user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.quick.app.core.design.component.MyAsyncImage
import com.quick.app.core.design.theme.SpaceExtraSmall
import com.quick.app.core.design.theme.SpaceMedium
import com.quick.app.core.design.theme.SpaceOuter
import com.quick.app.core.design.theme.SpaceSmallHeight
import com.quick.app.core.design.theme.SpacerOuterWidth
import com.quick.app.core.model.User


@Composable
fun ItemUser(
    data: User,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(top = SpaceExtraSmall)
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = SpaceOuter, vertical = SpaceMedium)
    ) {
        MyAsyncImage(
            model = data.icon, modifier = Modifier
                .size(55.dp)
                .clip(MaterialTheme.shapes.extraSmall)
        )

        SpacerOuterWidth()

        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            Text(
                text = data.nickname ?: "默认昵称",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )

            SpaceSmallHeight()

            Text(
                text = data.detail ?: "",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline,
            )
        }
    }
}
