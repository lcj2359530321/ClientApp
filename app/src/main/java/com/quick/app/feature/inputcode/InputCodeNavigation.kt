package com.quick.app.feature.inputcode

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.quick.app.feature.setpassword.SetPasswordPageData
import com.quick.app.util.Constant.STYLE

const val INPUT_CODE_USERNAME = "username"
const val INPUT_CODE_ROUTE = "input_code/{$STYLE}/{$INPUT_CODE_USERNAME}"

fun NavController.navigateToInputCode(data: InputCodePageData) {
    navigate("input_code/${data.style}/${data.username}") {
        launchSingleTop = true
    }
}

fun NavGraphBuilder.inputCodeScreen(
    finishPage: () -> Unit,
    toSetPassword: (SetPasswordPageData) -> Unit,
    finishAllLoginPages: () -> Unit,
) {
    composable(
        route = INPUT_CODE_ROUTE,
        arguments = listOf(
            navArgument(STYLE) { type = NavType.IntType },
            navArgument(INPUT_CODE_USERNAME) { type = NavType.StringType },
        ),
    ) {
        InputCodeRoute(
            finishPage = finishPage,
            finishAllLoginPages = finishAllLoginPages,
            toSetPassword = toSetPassword,
        )
    }
}
