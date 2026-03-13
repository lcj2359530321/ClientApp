package com.quick.app.feature.register

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val REGISTER_ROUTE = "Register"

fun NavController.navigateToRegister() = navigate(REGISTER_ROUTE)

fun NavGraphBuilder.registerScreen(
    finishPage: () -> Unit,
    finishAllLoginPages: () -> Unit,
) {
    composable(REGISTER_ROUTE) {
        RegisterRoute(
            finishPage = finishPage,
            finishAllLoginPages = finishAllLoginPages,
        )
    }
}