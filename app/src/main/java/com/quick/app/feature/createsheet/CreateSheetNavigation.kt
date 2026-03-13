package com.quick.app.feature.createsheet

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.quick.app.feature.sheetdetail.SHEET_ID
import com.quick.app.util.Constant

const val CREATE_SHEET_ROUTE = "create_sheet"

fun NavController.navigateToCreateSheet(sheetId: String = Constant.VALUE_NO_STRING) =
    navigate("${CREATE_SHEET_ROUTE}/$sheetId")

fun NavGraphBuilder.createSheetScreen(
    finishPage: () -> Unit,
) {
    composable("${CREATE_SHEET_ROUTE}/{${SHEET_ID}}") {
        CreateSheetRoute(
            finishPage = finishPage,
        )
    }
}