package com.quick.app.feature.setting

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable


const val SETTING_ROUTE = "setting"

fun NavController.navigateToSetting() =
    navigate(SETTING_ROUTE)

fun NavGraphBuilder.settingScreen(
    finishPage: () -> Unit,
): Unit {
    composable(
        SETTING_ROUTE,
    ) { backStackEntry ->
        SettingRoute(
            finishPage = finishPage,
        )
    }
}