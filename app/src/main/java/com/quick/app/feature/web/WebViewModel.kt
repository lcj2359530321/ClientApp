package com.quick.app.feature.web

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.quick.app.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class WebViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : BaseViewModel() {
    val uiState: StateFlow<WebUiState> =
        savedStateHandle.getStateFlow(
            key = WEB_PARAM, initialValue = ""
        )
            .map {
                WebUiState.Success(Json.decodeFromString<WebParam>(it))
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = WebUiState.Loading,
            )
}