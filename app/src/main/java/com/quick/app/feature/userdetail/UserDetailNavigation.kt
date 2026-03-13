package com.quick.app.feature.userdetail

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
//import com.quick.app.feature.scan.SCAN_ROUTE

const val USER_ID = "user_id"
const val USER_DETAIL_ROUTE = "users/{$USER_ID}"

fun NavController.navigateToUserDetail(data: String) {
    navigate("users/$data") {
        launchSingleTop = true

//        //关闭到指定界面之间的界面
//        popUpTo(SCAN_ROUTE) {
//            //包含指定的界面
//            inclusive = true
//        }
    }
}

fun NavGraphBuilder.userDetailScreen(
    finishPage: () -> Unit,
) {
    composable(
        route = USER_DETAIL_ROUTE,
    ) {
        UserDetailRoute(
            finishPage = finishPage,
        )
    }
}
