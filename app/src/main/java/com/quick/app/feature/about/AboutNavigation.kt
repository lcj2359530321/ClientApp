package com.quick.app.feature.about

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable


const val ABOUT_ROUTE = "about"

fun NavController.navigateToAbout() =
    navigate(ABOUT_ROUTE)

fun NavGraphBuilder.aboutScreen(
    finishPage: () -> Unit,
): Unit {
    composable(
        ABOUT_ROUTE,
    ) { backStackEntry ->
        AboutRoute(
            finishPage = finishPage,
        )
    }
}