package com.quick.app.feature.search

import com.quick.app.core.model.SuggestItem

sealed interface SearchUiState {
    /**
     * 正常状态
     *
     * 显示搜索历史等
     */
    data object Normal : SearchUiState

    /**
     * 搜索建议
     */
    data class Suggest(
        val suggests: List<SuggestItem>,
    ) : SearchUiState {
    }

    /**
     *
     */
    data object Search : SearchUiState
}
