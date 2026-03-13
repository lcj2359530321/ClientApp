package com.quick.app.core.design.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.quick.app.R
import com.quick.app.core.design.theme.LocalDividerColor
import com.quick.app.core.design.theme.MyAppTheme
import com.quick.app.core.design.theme.Space4XLarge
import com.quick.app.core.design.theme.SpaceExtraSmall
import com.quick.app.core.design.theme.SpaceLarge
import com.quick.app.core.design.theme.bodyXLarge

/**
 * 通用确认对话框
 */
@Composable
fun MyConfirmDialog(
    title: String = stringResource(id = R.string.confirm_delete),
    confirmTitle: String = stringResource(id = R.string.confirm),
    cancelTitle: String = stringResource(id = R.string.cancel),
    onConfirm: () -> Unit = {},
    onCancel: () -> Unit = {},
    onDismissRequest: () -> Unit = {},
) {
    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
            ),
            modifier = Modifier,
            shape = MaterialTheme.shapes.extraSmall,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyXLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .padding(horizontal = SpaceLarge, vertical = Space4XLarge),
                )

                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(SpaceExtraSmall)
                        .background(LocalDividerColor.current)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f)
                            .clickable { onCancel() },
                    ) {
                        Text(
                            text = cancelTitle,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                    Spacer(
                        modifier = Modifier
                            .width(SpaceExtraSmall)
                            .fillMaxHeight()
                            .background(LocalDividerColor.current)
                    )
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f)
                            .clickable { onConfirm() },
                    ) {
                        Text(
                            text = confirmTitle,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Center,
                        )
                    }

                }
            }
        }
    }

}


@Preview(showBackground = false)
@Composable
fun MyConfirmDialogPreview() {
    MyAppTheme {
        MyConfirmDialog(
            onDismissRequest = {

            }
        )
    }
}
