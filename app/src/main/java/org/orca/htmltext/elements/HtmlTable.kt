package org.orca.htmltext.elements

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DividerDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import org.jsoup.nodes.Element
import org.orca.htmltext.HtmlText

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun HtmlTable(
    node: Element,
    modifier: Modifier = Modifier,
    style: TextStyle,
    uriHandler: UriHandler = LocalUriHandler.current,
    domain: String? = null

) {
    val children = node.children()
    val body = children.filter { it -> it.nodeName() == "tbody" }

    HtmlParagraph {
        Column(
            modifier
                .height(IntrinsicSize.Min)
                .border(2.dp, DividerDefaults.color)
        ) {
            // handle elements explicitly in the body
            body.forEach { body ->
                body.children().forEach { tableRow ->
                    Row(
                        Modifier
                            .height(IntrinsicSize.Min)
                            .fillMaxWidth()
                    ) {
                        tableRow.children().forEach { tableCell ->
                            Column(
                                Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .border(1.dp, DividerDefaults.color)
                            ) {
                                HtmlText(
                                    tableCell,
                                    Modifier.padding(8.dp),
                                    style,
                                    uriHandler,
                                    domain
                                )
                            }
                        }
                    }
                }
            }
            // todo: alternatively handle elements not in the body
        }
    }
}