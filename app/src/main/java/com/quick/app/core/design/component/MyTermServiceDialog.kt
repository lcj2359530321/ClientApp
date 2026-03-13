package com.quick.app.core.design.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.quick.app.R
import com.quick.app.core.design.theme.MyAppTheme
import com.quick.app.core.design.theme.SpaceLarge
import com.quick.app.core.design.theme.SpaceLargeHeight
import com.quick.app.core.design.theme.SpaceMediumHeight
import com.quick.app.core.design.theme.body4XLargeBold
import org.orca.htmltext.HtmlText

//import org.orca.htmltext.HtmlText

@Composable
fun MyTermServiceDialog(
    onPrimaryClick: () -> Unit = {},
    onDisagreeClick: () -> Unit = {},
    onDismissRequest: () -> Unit = {},
    modifier: Modifier = Modifier,
): Unit {
    Dialog(
        onDismissRequest = { onDismissRequest() }
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
            ),
            modifier = Modifier
                .fillMaxWidth(),
            shape = MaterialTheme.shapes.small,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(SpaceLarge),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                //标题
                Text(
                    text = stringResource(id = R.string.term_service_privacy),
                    style = MaterialTheme.typography.body4XLargeBold,
                    color = MaterialTheme.colorScheme.onSurface,
                )

                SpaceLargeHeight()

                val scrollState = rememberScrollState()

                //内容
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .verticalScroll(scrollState)
                ) {
//                    Text(
//                        text = stringResource(id = R.string.term_service_privacy_content),
//                        color = MaterialTheme.colorScheme.onSurface
//                    )
                    HtmlText(
                        document = stringResource(id = R.string.term_service_privacy_content),
                        style = TextStyle(
                            fontSize = 14.sp,
                            lineHeight = 26.sp,
                        ),
                        modifier = Modifier.fillMaxSize()
                    )
                }

                SpaceMediumHeight()

                //同意按钮
                Button(
                    onClick = onPrimaryClick,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(text = stringResource(id = R.string.agree))
                }

                SpaceMediumHeight()

                //不同意按钮
                TextButton(
                    onClick = onDisagreeClick,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.onSurface
                    )
                ) {
                    Text(
                        text = stringResource(id = R.string.disagree),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline,
                    )
                }
            }
        }
    }
}


@Preview(showBackground = false)
@Composable
fun MyTermServiceDialogPreview() {
    MyAppTheme {
        MyTermServiceDialog()
    }
}