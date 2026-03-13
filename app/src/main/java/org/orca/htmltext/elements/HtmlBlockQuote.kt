package org.orca.htmltext.elements

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRowScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DividerDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun HtmlBlockQuote(
    modifier: Modifier = Modifier,
    content: @Composable FlowRowScope.() -> Unit
) {
    Row(
        modifier
            .padding(start = 8.dp)
            .height(IntrinsicSize.Min)
    ) {
        Box(
            Modifier
                .fillMaxHeight()
                .width(4.dp)
                .background(DividerDefaults.color)
        )
        HtmlParagraph(
            Modifier.padding(start = 8.dp),
            content,
        )
    }
}