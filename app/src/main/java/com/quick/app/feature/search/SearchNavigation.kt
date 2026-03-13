package com.quick.app.feature.search

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable


const val SEARCH_ROUTE = "search"

fun NavController.navigateToSearch() =
    navigate(SEARCH_ROUTE)

fun NavGraphBuilder.searchScreen(
    finishPage: () -> Unit,
    toSheetDetail: (String) -> Unit,
    toUserDetail: (String) -> Unit,
): Unit {
    composable(
        SEARCH_ROUTE,
    ) { backStackEntry ->
        SearchRoute(
            finishPage = finishPage,
            toSheetDetail = toSheetDetail,
            toUserDetail = toUserDetail,
        )
    }
}