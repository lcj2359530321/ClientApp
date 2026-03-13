package com.quick.app.feature.publishfeed

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val ADD_FEED_ROUTE = "feeds/create"

fun NavController.navigateToPublishFeed() {
    navigate(ADD_FEED_ROUTE) {
        launchSingleTop = true
    }
}

fun NavGraphBuilder.publishFeedScreen(
    finishPage: () -> Unit,
) {
    composable(
        route = ADD_FEED_ROUTE,
    ) {
        PublishFeedRoute(
            finishPage = finishPage,
        )
    }
}
