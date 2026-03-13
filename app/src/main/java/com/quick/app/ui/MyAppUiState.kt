package com.quick.app.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.viewModelScope
import com.quick.app.core.data.repository.UserDataRepository
import com.quick.app.core.model.UserData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/**
 * 获取全局应用Ui状态
 */
@Composable
fun rememberMyAppUiState(
    userDataRepository: UserDataRepository,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
): MyAppUiState {
    return remember(userDataRepository, coroutineScope) {
        MyAppUiState(
            coroutineScope,
            userDataRepository,
        )
    }
}

/**
 * 全局界面状态
 */
@Stable
class MyAppUiState(
    coroutineScope: CoroutineScope,
    userDataRepository: UserDataRepository,
) {
    /**
     * 用户数据
     */
    val userData =  userDataRepository.userData.stateIn(
        scope = coroutineScope,
        initialValue = UserData(),
        started = SharingStarted.WhileSubscribed(5_000)
    )

    /**
     * 是否登录
     */
    val isLogin : StateFlow<Boolean> = userDataRepository.userData.map{
        it.isLogin()
    }.stateIn(
        scope = coroutineScope,
        initialValue =false,
        started = SharingStarted.WhileSubscribed(5_000)
    )
}