package com.quick.app.feature.sheetdetail

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.quick.app.feature.splash.SPLASH_ROUTE
import com.quick.app.feature.splash.SplashRoute

const val SHEET_DETAIL_ROUTE = "sheet_detail"
const val SHEET_ID = "sheet_id"

fun NavController.navigateToSheetDetail(sheetId: String): Unit {
    navigate("${SHEET_DETAIL_ROUTE}/$sheetId")
}

fun NavGraphBuilder.sheetDetailScreen(
    toMusicPlayer: () -> Unit,
    finishPage : () -> Unit,
    toLogin: () -> Unit,
) {
    composable("${SHEET_DETAIL_ROUTE}/{${SHEET_ID}}",
        arguments = listOf(
            navArgument(SHEET_ID){
                type = NavType.StringType
            }
        )
    ) {
        SheetDetailRoute(
            finishPage = finishPage,
            toMusicPlayer = toMusicPlayer,
            toLogin = toLogin,
        )
    }
}