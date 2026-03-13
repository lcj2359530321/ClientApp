package com.quick.app.feature.setting

import com.quick.app.core.model.DarkThemeConfig

sealed interface SettingUiState {
    data class Success(val setting: UserEditableSetting) : SettingUiState
    data object Loading : SettingUiState
}

data class UserEditableSetting(
    val useDynamicColor: Boolean,
    val darkThemeConfig: DarkThemeConfig,
)