package com.quick.app.ui

import com.quick.app.core.model.UserData

sealed interface MainActivityUiState {
    data object Loading : MainActivityUiState
    data class Success(val userData: UserData) : MainActivityUiState
}
