package com.quick.app.feature.profile

import com.quick.app.core.exception.CommonException

sealed interface ProfileUiState {
    data object Success : ProfileUiState
    data object Loading : ProfileUiState
    data class Error(val throwable: CommonException) : ProfileUiState
}