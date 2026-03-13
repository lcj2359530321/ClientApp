package com.quick.app.core.extension

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.quick.app.R
import java.util.Locale
import kotlin.time.Duration.Companion.milliseconds

@Composable
internal fun Long.asFormatTimeString() = milliseconds.toComponents { minutes, seconds, _ ->
    stringResource(
        id = R.string.player_progress_format,
        String.format(locale = Locale.US, format = "%02d", minutes),
        String.format(locale = Locale.US, format = "%02d", seconds)
    )
}