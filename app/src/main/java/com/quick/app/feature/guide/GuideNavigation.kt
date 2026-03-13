package com.quick.app.feature.guide

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.quick.app.feature.splash.SPLASH_ROUTE

const val GUIDE_ROUTE = "guide"

fun NavController.navigateToGuide(): Unit {
    navigate(GUIDE_ROUTE) {
        launchSingleTop = true
        popUpTo(SPLASH_ROUTE) {
            inclusive = true
        }
    }
}

fun NavGraphBuilder.guideScreen(
    toMain: () -> Unit,
    toLogin: () -> Unit,
): Unit {
    composable(
        GUIDE_ROUTE,
    ) {
        GuideRoute(
            toMain = toMain,
            toLogin = toLogin,
        )
    }
}