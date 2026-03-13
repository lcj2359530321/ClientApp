package com.quick.app.feature.setpassword

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.quick.app.feature.inputcode.INPUT_CODE_USERNAME

const val INPUT_CODE = "code"

const val SET_PASSWORD_ROUTE = "set_password/{$INPUT_CODE_USERNAME}/{$INPUT_CODE}"

fun NavController.navigateToSetPassword(data: SetPasswordPageData) {
    navigate("set_password/${data.username}/${data.code}") {
        launchSingleTop = true
    }
}

fun NavGraphBuilder.setPasswordScreen(
    finishPage: () -> Unit,
    finishAllLoginPages: () -> Unit,
) {
    composable(
        route = SET_PASSWORD_ROUTE,
    ) {
        SetPasswordRoute(
            finishPage = finishPage,
            finishAllLoginPages = finishAllLoginPages,
        )
    }
}
