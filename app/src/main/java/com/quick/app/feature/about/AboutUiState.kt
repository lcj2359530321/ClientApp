package com.quick.app.feature.about

import com.quick.app.core.model.AboutModel

sealed interface AboutUiState {
    data class Success(val data: AboutModel) : AboutUiState
    data object Loading : AboutUiState
}