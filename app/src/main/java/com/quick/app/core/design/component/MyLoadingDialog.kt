package com.quick.app.core.design.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.quick.app.R
import com.quick.app.core.design.theme.MyAppTheme
import com.quick.app.core.design.theme.Space3XLargeHeight
import com.quick.app.core.design.theme.SpaceOuter

@Composable
fun MyLoadingDialog(
    title: String = stringResource(id = R.string.loading),
    onDismissRequest: () -> Unit
) {
    Dialog(
        onDismissRequest = { onDismissRequest() }
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color.Black,
            ),
            modifier = Modifier
                .size(200.dp)
                .padding(SpaceOuter),
            shape = MaterialTheme.shapes.small,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.width(50.dp),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
                Space3XLargeHeight()

                Text(
                    text = title,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White,
                    modifier = Modifier,
                )

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MyLoadingDialogPreview() {
    MyAppTheme {
        MyLoadingDialog(
            onDismissRequest = {

            }
        )
    }
}