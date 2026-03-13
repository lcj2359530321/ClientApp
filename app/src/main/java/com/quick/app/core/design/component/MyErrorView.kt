package com.quick.app.core.design.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.quick.app.R
import com.quick.app.core.design.theme.MyAppTheme
import com.quick.app.core.design.theme.Space3XLargeHeight
import com.quick.app.core.design.theme.SpaceOuter
import com.quick.app.core.exception.CommonException
import com.quick.app.core.extension.clickableNoRipple

/**
 * 没有数据视图
 */
@Composable
fun MyEmptyView(
    message: String = stringResource(id = R.string.empty_data),
    icon: Int = R.drawable.bg_empty,
    retryButtonText: Int = R.string.click_retry,
    onRetryClick: () -> Unit = {},
    modifier: Modifier = Modifier,
): Unit {
    MyErrorView(
        message = message,
        retryButtonText = retryButtonText,
        icon = icon,
        onRetryClick = onRetryClick,
        modifier = modifier
    )
}

/**
 * 错误视图
 */
@Composable
fun MyErrorView(
    exception: CommonException,
    retryButtonText: Int = R.string.click_retry,
    onRetryClick: () -> Unit = {},
    modifier: Modifier = Modifier,
): Unit {
    MyErrorView(
        message = exception.tipString!!,
        icon = exception.tipIcon ?: R.drawable.bg_empty,
        retryButtonText = retryButtonText,
        onRetryClick = onRetryClick,
        modifier = modifier,
    )
}

@Composable
fun MyErrorView(
    message: String = stringResource(id = R.string.error_load),
    icon: Int = R.drawable.bg_error,
    retryButtonText: Int = R.string.click_retry,
    onRetryClick: () -> Unit = {},
    modifier: Modifier = Modifier,
): Unit {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .clickableNoRipple {
                onRetryClick()
            }
            .padding(SpaceOuter)
    ) {
        Image(
            modifier = Modifier.size(300.dp),
            painter = painterResource(id = icon),
            contentDescription = null,
        )

        Space3XLargeHeight()

        Text(
            text = message,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.outline,
        )

        Space3XLargeHeight()

        OutlinedButton(
            onClick = onRetryClick,
            border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline), // 设置边框颜色
            modifier = Modifier
                .padding(horizontal = 50.dp)
                .widthIn(200.dp)
        ) {
            Text(
                text = stringResource(id = retryButtonText),
            )
        }
    }
}


//@Preview(showBackground = true, apiLevel = 33)
//@Composable
//fun MyErrorViewPreview() {
//    MyAppTheme {
//        MyErrorView(
//            message = stringResource(id = R.string.error_network_not_connect)
//        )
//    }
//}
//
//@DevicePreviews
//@Composable
//fun MyErrorViewNoDataPreview() {
//    MyAppTheme {
//        MyErrorView(
//            message = stringResource(id = R.string.empty_data),
//            icon = R.drawable.bg_empty,
//        )
//    }
//}
