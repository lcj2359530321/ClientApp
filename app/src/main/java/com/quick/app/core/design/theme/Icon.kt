package com.quick.app.core.design.theme

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


@Composable
fun ArrowIcon(tint: Color = LocalArrowColor.current) {
    Icon(
        imageVector = Icons.Default.ChevronRight,
        contentDescription = null,
        tint = tint,
    )
}
