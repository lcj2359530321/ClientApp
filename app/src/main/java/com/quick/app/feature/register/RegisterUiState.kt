package com.quick.app.feature.register

import com.quick.app.core.exception.CommonException


sealed interface RegisterUiState {
    /**
     * 成功
     */
    data object Success : RegisterUiState

    /**
     * 加载中
     */
    data object Loading : RegisterUiState

    data object None : RegisterUiState

    data class Error(
        val exception: CommonException,
    ) : RegisterUiState

    data class ErrorRes(
        val data: Int,
    ) : RegisterUiState
}