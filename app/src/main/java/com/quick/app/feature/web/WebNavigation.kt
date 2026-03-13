package com.quick.app.feature.web

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.URLEncoder

const val WEB_PARAM = "web_param"
const val WEB_ROUTE = "web/{$WEB_PARAM}"

val URL_CHARACTER_ENCODING = Charsets.UTF_8.name()

fun NavController.navigateToWeb(data: WebParam) {
    val paramString = Json.encodeToString(data)
    val encoded = URLEncoder.encode(paramString, URL_CHARACTER_ENCODING)

    navigate("web/$encoded") {
        launchSingleTop = true
    }
}


fun NavGraphBuilder.webScreen(
    finishPage: () -> Unit,
) {
    composable(
        route = WEB_ROUTE,
    ) {
        WebRoute(
            finishPage = finishPage,
        )
    }
}


@Serializable
data class WebParam(
    val uri: String? = null,
    val content: String? = null,
)