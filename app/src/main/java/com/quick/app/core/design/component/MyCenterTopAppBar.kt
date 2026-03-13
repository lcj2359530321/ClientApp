package com.quick.app.core.design.component

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextOverflow

/**
 * 居中的TopAppBar
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyCenterTopAppBar(
    titleText: String = "",
    title: (@Composable () -> Unit)? = null,
    colors: TopAppBarColors? = null,
    actions: @Composable (RowScope.() -> kotlin.Unit) = {},
    finishPage: (() -> Unit)? = null,
    navigationIcon: @Composable () -> Unit = {},
) {
    CenterAlignedTopAppBar(
        navigationIcon = {
            if (finishPage != null) {
                IconButton(onClick = finishPage) {
                    Icon(
                        imageVector = Icons.Default.ArrowBackIosNew,
                        contentDescription = null
                    )
                }
            } else {
                navigationIcon()
            }
        },
        title = {
            Text(
                text = titleText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        actions = actions,
        colors = colors ?: TopAppBarDefaults.centerAlignedTopAppBarColors().copy(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
    )
}