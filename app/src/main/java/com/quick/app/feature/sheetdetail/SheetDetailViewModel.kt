package com.quick.app.feature.sheetdetail

import android.util.Log
import androidx.collection.intSetOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.quick.app.core.data.repository.SheetRepository
import com.quick.app.core.data.repository.SongRepository
import com.quick.app.core.data.repository.UserDataRepository
import com.quick.app.core.data.repository.UserRepository
import com.quick.app.core.exception.localException
import com.quick.app.core.media.MediaServiceConnection
import com.quick.app.core.model.BaseId
import com.quick.app.core.model.SHEET_EMPTY
import com.quick.app.core.model.Sheet
import com.quick.app.core.model.ViewData
import com.quick.app.core.network.datasource.MyRetrofitDatasource
import com.quick.app.core.result.asResult
import com.quick.app.feature.lyric.LyricManager
import com.quick.app.feature.mediaplayer.BaseMediaPlayerViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * 歌单详情VM
 */
@HiltViewModel
class SheetDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val sheetRepository : SheetRepository,
    mediaServiceConnection: MediaServiceConnection,
    private val userRepository: UserRepository,
    userDataRepository: UserDataRepository,
    songRepository: SongRepository,
    lyricManager: LyricManager
) : BaseMediaPlayerViewModel(
    mediaServiceConnection,
    songRepository,
    userDataRepository,
    lyricManager,
) {

    val sheetId: String = checkNotNull(savedStateHandle[SHEET_ID])

    private val _uiState = MutableStateFlow<SheetDetailUiState>(SheetDetailUiState.Loading)
    val uiState: StateFlow<SheetDetailUiState> = _uiState


    private lateinit var data : Sheet

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            sheetRepository.sheetDetail(sheetId)
                .asResult()
                .collectLatest { r->
                    if(r.isSuccess){
                        data = r.getOrThrow().data!!
                        _uiState.value = SheetDetailUiState.Success(data)
                    }else{
                        _uiState.value = SheetDetailUiState.Error(r.exceptionOrNull()!!.localException())
                    }
//                    if (r.status == 0) {
//                        _uiState.value = SheetDetailUiState.Success(r.uiState ?: SHEET_EMPTY())
//                    } else {
//                        _uiState.value = SheetDetailUiState.Error(r.message ?: "请求失败")
//                    }
                }
        }
//        viewModelScope.launch {
//            try {
//                sheetRepository.sheetDetail(sheetId)
//                    .collectLatest { r ->
//                        if (r.status == 0) {
//                            _uiState.value = SheetDetailUiState.Success(r.uiState ?: SHEET_EMPTY())
//                        } else {
//                            _uiState.value = SheetDetailUiState.Error(r.message ?: "请求失败")
//                        }
//                    }
//            } catch (e: Exception) {
//
//            }
//        }
//        viewModelScope.launch {
//            try {
//                val r = MyRetrofitDatasource.sheetDetail(sheetId)
//
//                if(r.status == 0){
//                    _uiState.value =SheetDetailUiState.Success(r.uiState?: SHEET_EMPTY())
//                }
//                else{
//                    _uiState.value =SheetDetailUiState.Error(r.message?: "请求失败")
//                }
//            } catch (e: Exception) {
//                _uiState.value =SheetDetailUiState.Error(e.localizedMessage)
//            }
//        }
    }

    fun onRetryClick() {
        loadData()
    }

    fun onSongClick(index: Int) {
        setMediasAndPlay(data.songs!!,index,true)
    }

    fun onCollectClick() {
        viewModelScope.launch {
            if (data.isCollected) {
                //以及收藏

                //取消收藏
                sheetRepository.cancelCollectSheet(
                    BaseId(sheetId)
                )
                    .asResult()
                    .collectLatest { r ->
                        if (r.isSuccess) {
                            setCollectStatus()
                        } else {
                            tipError.value = r.exceptionOrNull()!!.localException().tipString
                        }
                    }

            } else {
                sheetRepository.collectSheet(
                    BaseId(
                        id = data.id
                    )
                )
                    .asResult()
                    .collectLatest {
                        if (it.isSuccess) {
                            setCollectStatus("1") //不为空就表示收藏
                        } else {
                            tipError.value = it.exceptionOrNull()!!.localException().tipString
                        }
                    }
            }
        }
    }

    private fun setCollectStatus(
        collectId: String = "",
    ) {
        data = data.copy(
            collectId = collectId,
            collectsCount = if (collectId.isBlank()) data.collectsCount - 1 else data.collectsCount + 1
        )
        _uiState.value = SheetDetailUiState.Success(data)
    }
}

