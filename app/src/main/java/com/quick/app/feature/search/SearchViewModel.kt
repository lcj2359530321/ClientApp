package com.quick.app.feature.search

import androidx.lifecycle.viewModelScope
import com.quick.app.core.data.repository.CommonRepository
import com.quick.app.core.data.repository.SearchHistoryRepository
import com.quick.app.core.database.model.SearchHistoryEntity
import com.quick.app.core.model.SuggestItem
import com.quick.app.core.result.asResult
import com.quick.app.ui.BaseViewModel
import com.quick.app.util.Constant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val commonRepository: CommonRepository,
    private val searchHistoryRepository: SearchHistoryRepository,
) : BaseViewModel() {
    /**
     * 界面状态
     */
    private val _uiState = MutableStateFlow<SearchUiState>(
        SearchUiState.Normal
    )
    val uiState: StateFlow<SearchUiState> = _uiState

    /**
     * 搜索关键字
     */
    private val _query = MutableStateFlow<String>("")
    val query: StateFlow<String> = _query

    /**
     * 搜索关键字为空时，占位文本，真实项目中，可以来自推荐引擎
     */
    private val _placeholder = MutableStateFlow<String>("手机")
    val placeholder: StateFlow<String> = _placeholder

    private val _focused = MutableStateFlow<Boolean>(true)
    val focused: StateFlow<Boolean> = _focused

    val searchHistories = searchHistoryRepository.getAll().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList(),
    )

    val searchHots = flow<List<String>> {
        emit(HOTS)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList(),
    )

    /**
     * 搜索历史编辑模式
     */
    private val _searchHistoryEditing = MutableStateFlow<Boolean>(false)
    val searchHistoryEditing: StateFlow<Boolean> = _searchHistoryEditing

    val selectedIndex = MutableStateFlow<Int>(0)

    init {
        viewModelScope.launch {
            query
                .debounce(700) // 设置防抖时间
                .collectLatest {
                    checkLoadSuggestIfNeed()
                }
        }
    }

    fun onQueryChange(data: String) {
        _query.value = data
    }

    fun onSearchClick() {
        if (query.value.isBlank()) {
            _query.value = placeholder.value
        }
        doSearch()
    }

    private fun doSearch() {
        _focused.value = false

        //主状态成功
        _uiState.value = SearchUiState.Search

        saveSearchHistory()
    }

    private fun saveSearchHistory() {
        viewModelScope.launch {
            val p = SearchHistoryEntity(
                query.value,
                System.currentTimeMillis(),
                Constant.VALUE0
            )
            searchHistoryRepository.insert(p)
        }
    }

    fun onFocusedChange(data: Boolean) {
        _focused.value = data
        checkLoadSuggestIfNeed()
    }

    fun finishPage() {
        _uiState.value = SearchUiState.Normal
    }

    fun onHotClick(data: String) {
        _query.value = data
        doSearch()
    }

    fun selectedIndexChanged(data: Int): Unit {
        selectedIndex.value = data
    }

    private fun checkLoadSuggestIfNeed() {
        if (focused.value) {
            if (query.value.isNotBlank()) {

                //获取搜索建议
                loadSuggest()
            } else {
                _uiState.value = SearchUiState.Normal
            }
        }
    }

    private fun loadSuggest() {
        viewModelScope.launch {
            commonRepository
                .searchSuggest(query.value)
                .asResult()
                .collectLatest {
                    if (it.isSuccess) {
                        _uiState.value = SearchUiState.Suggest(
                            suggests = (it.getOrNull()?.data?.sheets ?: emptyList()) +
                                    (it.getOrNull()?.data?.users ?: emptyList())
                        )
                    }
                }
        }
    }

    fun onSuggestClick(data: SuggestItem) {
        _query.value = data.title
        doSearch()
    }

    fun onSearchHistoryClick(data: SearchHistoryEntity) {
        _query.value = data.title
        doSearch()
    }

    fun toggleSearchHistoryEditing() {
        _searchHistoryEditing.value = !_searchHistoryEditing.value
    }

    fun onDeleteSearchHistoryClick(data: SearchHistoryEntity) {
        viewModelScope.launch {
            searchHistoryRepository.delete(data)
        }
    }

    fun onDeleteAllSearchHistoryClick() {
        viewModelScope.launch {
            searchHistoryRepository.deleteAll()
        }
    }

    companion object {
        val HOTS = listOf<String>(
            "lcj",
            "Android云音乐",
            "jetpack compose",
            "畅听",
            "vivo",
            "小程序",
            "守望先锋",
            "实习生",
            "xxxxxxxx",
            "音乐"
        )
    }
}