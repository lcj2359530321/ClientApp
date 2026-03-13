package com.quick.app.feature.loginhome

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.quick.app.feature.main.navigateToMain
import com.quick.app.feature.splash.SplashRoute
import com.quick.app.feature.web.WebParam

const val LOGIN_HOME_ROUTE = "login_home"

fun NavController.navigateToLoginHome() = navigate(LOGIN_HOME_ROUTE)

fun NavController.finishAllLoginPages() {
    popBackStack(LOGIN_HOME_ROUTE, inclusive = true)
}

fun NavGraphBuilder.loginHomeScreen(
    finishPage: () -> Unit,
    toLogin: () -> Unit,
    toCodeLogin: () -> Unit,
    finishAllLoginPages: () -> Unit,
    toWebPage: (WebParam) -> Unit,
){
    composable(LOGIN_HOME_ROUTE){
        LoginHomeRoute(
            finishPage = finishPage,
            toLogin = toLogin,
            toCodeLogin = toCodeLogin,
            finishAllLoginPages = finishAllLoginPages,
            toWebPage = toWebPage,
        )
    }
}