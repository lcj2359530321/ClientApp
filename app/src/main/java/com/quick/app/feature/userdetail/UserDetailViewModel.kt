package com.quick.app.feature.userdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.quick.app.core.data.repository.UserDataRepository
import com.quick.app.core.data.repository.UserRepository
import com.quick.app.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


@HiltViewModel
class UserDetailViewModel
@Inject constructor
    (
    savedStateHandle: SavedStateHandle,
    private val userRepository: UserRepository,
    private val userDataRepository: UserDataRepository,
) : BaseViewModel() {
    private val id = savedStateHandle.getStateFlow(key = USER_ID, initialValue = "")

    val uiState: StateFlow<UserDetailUiState> = userRepository.userDetail(
        id.value,
    ).map { networkResponse ->
        UserDetailUiState.Success(
            user = networkResponse.data!!,
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = UserDetailUiState.Loading,
        )

}
