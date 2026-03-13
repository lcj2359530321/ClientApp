package com.quick.app.feature.inputidentity

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.quick.app.feature.inputcode.InputCodePageData
import com.quick.app.util.Constant
import com.quick.app.util.Constant.STYLE

const val INPUT_IDENTITY_ROUTE = "input_identity/{$STYLE}"

fun NavController.navigateToInputIdentity(style: Int = Constant.STYLE_CODE_LOGIN) {
    navigate("input_identity/$style") {
        launchSingleTop = true
    }
}

fun NavGraphBuilder.inputIdentityScreen(
    finishPage: () -> Unit,
    toInputCode: (InputCodePageData) -> Unit,
): Unit {
    composable(
        route = INPUT_IDENTITY_ROUTE,
        arguments = listOf(
            navArgument(STYLE) { type = NavType.IntType },
        ),
    ) { backStackEntry ->
        InputIdentityRoute(
            finishPage = finishPage,
            toInputCode = toInputCode,
        )
    }
}