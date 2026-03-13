package com.quick.app.feature.mediaplayer

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

const val MUSIC_PLAYER_ROUTE = "MusicPlayer"

fun NavController.navigateToMusicPlayer() =
    navigate(MUSIC_PLAYER_ROUTE)

fun NavGraphBuilder.musicPlayerScreen(
    finishPage: () -> Unit,
) {
    composable(MUSIC_PLAYER_ROUTE) {
        MusicPlayerRoute(
            finishPage = finishPage,
        )
    }
}