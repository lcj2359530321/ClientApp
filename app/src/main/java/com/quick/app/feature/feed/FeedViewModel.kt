package com.quick.app.feature.feed

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.quick.app.core.data.repository.FeedRepository
import com.quick.app.core.data.repository.UserDataRepository
import com.quick.app.core.model.Feed
import com.quick.app.core.result.asResult
import com.quick.app.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class FeedViewModel
@Inject constructor
    (
    private val savedStateHandle: SavedStateHandle,
    private val feedRepository: FeedRepository,
    private val userDataRepository: UserDataRepository,
) : BaseViewModel() {

    val datum = MutableStateFlow<List<Feed>>(emptyList())

    fun loaData(): Unit {
        datum.value = emptyList()
        loadMore()
    }

    fun loadMore() {   //TODO
        viewModelScope.launch {
            val blackList = setOf("小米测试账号", "Hcc", "Sean")

            feedRepository.feeds(
            ).asResult()
                .collectLatest {result ->
                    if (result.isSuccess) {
                        val rawList = result.getOrThrow().data?.list ?: emptyList()

                        datum.value = rawList.filter { feed ->
                            val nickname = feed.user?.nicknameFormat
                            nickname !in blackList
                        }
                    } else {
                        // 处理失败逻辑，例如显示错误提示
                    }
                }
        }
    }
}