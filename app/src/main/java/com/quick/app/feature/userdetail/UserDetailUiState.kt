package com.quick.app.feature.userdetail

import com.quick.app.core.exception.CommonException
import com.quick.app.core.model.User

sealed interface UserDetailUiState {
    data class Error(val throwable: CommonException) : UserDetailUiState
    data object Loading : UserDetailUiState
    data class Success(
        val user: User,
    ) : UserDetailUiState {
    }
}
