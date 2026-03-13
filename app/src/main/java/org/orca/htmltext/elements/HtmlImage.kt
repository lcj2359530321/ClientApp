package org.orca.htmltext.elements

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import org.jsoup.nodes.Element
import org.orca.htmltext.util.validateUrl

@Composable
internal fun HtmlImage(
    node: Element,
    modifier: Modifier = Modifier,
    domain: String? = null
) {
    val source = node.attr("src")
    if (source == "") return

    val url = validateUrl(source, domain) ?: return
    AsyncImage(
        model = url,
        contentDescription = null,
        modifier = modifier.fillMaxWidth(),
        contentScale = ContentScale.FillWidth,
    )
//    KamelImage(
//        resource = asyncPainterResource(data = url),
//        contentDescription = "Profile",
//        modifier=modifier,
//        contentScale = ContentScale.Inside,
//    )
//    KamelImage(
//        lazyPainterResource(url),
//        node.attr("alt") ?: "No description.",
//        modifier,
//        contentScale = ContentScale.Inside
//    )
}