package com.quick.app.feature.setting

import androidx.lifecycle.viewModelScope
import com.quick.app.core.data.repository.UserDataRepository
import com.quick.app.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository,
) : BaseViewModel() {
    val uiState: StateFlow<SettingUiState> =
        userDataRepository.userData
            .map { userData ->
                SettingUiState.Success(
                    setting = UserEditableSetting(
                        useDynamicColor = userData.useDynamicColor,
                        darkThemeConfig = userData.darkThemeConfig,
                    ),
                )
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5.seconds.inWholeMilliseconds),
                initialValue = SettingUiState.Loading,
            )

    fun updateDynamicColorPreference(useDynamicColor: Boolean) {
        viewModelScope.launch {
            userDataRepository.setDynamicColorPreference(useDynamicColor)
        }
    }
}