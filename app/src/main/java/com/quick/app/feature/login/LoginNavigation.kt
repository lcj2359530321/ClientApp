package com.quick.app.feature.login

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.quick.app.feature.loginhome.LoginHomeRoute
import com.quick.app.feature.web.WebParam

const val LOGIN_ROUTE = "login"

fun NavController.navigateToLogin() = navigate(LOGIN_ROUTE)

fun NavGraphBuilder.loginScreen(
    finishPage: () -> Unit,
    toRegister: () -> Unit,
    toSetPassword: () -> Unit,
    finishAllLoginPages: () -> Unit,
) {
    composable(LOGIN_ROUTE) {
        LoginRoute(
            finishPage = finishPage,
            toRegister = toRegister,
            toSetPassword = toSetPassword,
            finishAllLoginPages = finishAllLoginPages,
        )
    }
}