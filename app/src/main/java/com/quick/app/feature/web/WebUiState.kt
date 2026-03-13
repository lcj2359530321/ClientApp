package com.quick.app.feature.web

sealed interface WebUiState {
    data class Success(
        val data: WebParam,
    ) : WebUiState

    data object Loading : WebUiState
}