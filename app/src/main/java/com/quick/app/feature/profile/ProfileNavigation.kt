package com.quick.app.feature.profile

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable


const val PROFILE_ROUTE = "profile"

fun NavController.navigateToProfile() =
    navigate(PROFILE_ROUTE)

fun NavGraphBuilder.profileScreen(
    finishPage: () -> Unit,
): Unit {
    composable(
        PROFILE_ROUTE,
    ) { backStackEntry ->
        ProfileRoute(
            finishPage = finishPage,
        )
    }
}