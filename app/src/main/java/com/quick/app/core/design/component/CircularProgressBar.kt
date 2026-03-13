package com.quick.app.core.design.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.quick.app.core.design.theme.LocalDividerColor
import com.quick.app.core.design.theme.MyAppTheme


@Composable
fun MyMusicCircularProgressBar(
    progress: Float = 0f,
    progressMax: Float = 100f,
    startAngle: Float = 0f,
    modifier: Modifier = Modifier,
) {
    CircularProgressBar(
        modifier = modifier,
        progress = progress,
        progressMax = progressMax,
        progressBarColor = MaterialTheme.colorScheme.onSurface,
        progressBarWidth = 1.5.dp,
        backgroundProgressBarColor = LocalDividerColor.current,
        backgroundProgressBarWidth = 1.5.dp,
        roundBorder = true,
        startAngle = startAngle
    )
}

/**
 * 圆环进度条
 *
 * https://github.com/hitanshu-dhawan/CircularProgressBar-Compose/blob/main/circularprogressbar/src/main/java/com/hitanshudhawan/circularprogressbar/CircularProgressBar.kt
 */
@Composable
fun CircularProgressBar(
    modifier: Modifier = Modifier,
    progress: Float = 0f,
    progressMax: Float = 100f,
    progressBarColor: Color = Color.Black,
    progressBarWidth: Dp = 7.dp,
    backgroundProgressBarColor: Color = Color.Gray,
    backgroundProgressBarWidth: Dp = 3.dp,
    roundBorder: Boolean = false,
    startAngle: Float = 0f
) {
    Canvas(modifier = modifier.fillMaxSize()) {

        val canvasSize = size.minDimension

        val radius = canvasSize / 2 - maxOf(backgroundProgressBarWidth, progressBarWidth).toPx() / 2

        drawCircle(
            color = backgroundProgressBarColor,
            radius = radius,
            center = size.center,
            style = Stroke(width = backgroundProgressBarWidth.toPx())
        )

        drawArc(
            color = progressBarColor,
            startAngle = 270f + startAngle,
            sweepAngle = (progress / progressMax) * 360f,
            useCenter = false,
            topLeft = size.center - Offset(radius, radius),
            size = Size(radius * 2, radius * 2),
            style = Stroke(
                width = progressBarWidth.toPx(),
                cap = if (roundBorder) StrokeCap.Round else StrokeCap.Butt
            )
        )
    }
}


