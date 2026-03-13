package com.quick.app.feature.splash

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.quick.app.core.model.Ad
import com.quick.app.feature.main.navigateToMain

const val SPLASH_ROUTE = "splash"

fun NavGraphBuilder.splashScreen(
    toMain : () -> Unit,
    toGuide : () -> Unit,
    toAd: (Ad) -> Unit,
){
    composable(SPLASH_ROUTE){
        SplashRoute(
            toMain = toMain,
            toGuide = toGuide,
            toAd = toAd,
        )
    }
}