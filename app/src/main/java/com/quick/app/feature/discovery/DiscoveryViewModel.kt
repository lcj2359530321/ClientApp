package com.quick.app.feature.discovery

import androidx.lifecycle.viewModelScope
import com.quick.app.core.data.repository.CommonRepository
import com.quick.app.core.data.repository.SongRepository
import com.quick.app.core.data.repository.UserDataRepository
import com.quick.app.core.exception.CommonException
import com.quick.app.core.exception.localException
import com.quick.app.core.media.MediaServiceConnection
import com.quick.app.core.model.Song
import com.quick.app.feature.lyric.LyricManager
import com.quick.app.feature.mediaplayer.BaseMediaPlayerViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * 发现界面VM
 */
@HiltViewModel
class DiscoveryViewModel @Inject constructor(
    songRepository: SongRepository,
    mediaServiceConnection: MediaServiceConnection,
    userDataRepository: UserDataRepository,
    lyricManager: LyricManager,
    private val commonRepository: CommonRepository,
) : BaseMediaPlayerViewModel(
    mediaServiceConnection,
    songRepository,
    userDataRepository,
    lyricManager,
) {
//    private val _topDatum = MutableStateFlow<List<ViewData>>(emptyList())
//    val topDatum: StateFlow<List<ViewData>> = _topDatum

    val uiState = MutableStateFlow<DiscoveryUiState>(DiscoveryUiState())

    private var startTime: Long = 0

    init {
        loadData()
    }

    fun loadData() {
//        _datum.value = DiscoveryPreviewParameterData.SONGS

        //测试序列化
//        val json = Json.encodeToString(DiscoveryPreviewParameterData.SONG)
//        Log.d(TAG, "encodeToString: $json")
//
//        val obj = Json.decodeFromString<Song>(json)
//        Log.d(TAG, "decodeFromString: $obj")

        //测试网络请求
        viewModelScope.launch {
//            val songs = MyRetrofitDatasource.songs()
//            _datum.value = songs.data?.list ?: emptyList()
            try {
                startTime = System.currentTimeMillis()

                //设置为加载中状态
                uiState.value = uiState.value.copy(
                    refreshState = 1,
                    exception = null,
                )

                val indexes = commonRepository.indexes(app = 30)

                val endTime = System.currentTimeMillis()
                val consume = endTime - startTime

                if (consume < EXPECTED_COMSUME_TIME) {
                    val delayTimeMillis = EXPECTED_COMSUME_TIME - consume
                    Timber.d("loadData delay %d", delayTimeMillis)
                    delay(delayTimeMillis)
                }

                if (!indexes.isSucceeded) {
                    //业务失败
                    uiState.value = uiState.value.copy(
                        refreshState = 2,
                        exception = CommonException(
                            networkResponse = indexes,
                        ).localException(),
                    )
                    return@launch
                }

                //请求成功
                uiState.value = uiState.value.copy(
                    refreshState = 3,
                    exception = null,
                    topDatum = indexes.data?.list ?: emptyList(),
                )
            } catch (e: Exception) {
                uiState.value = uiState.value.copy(
                    refreshState = 2,
                    exception = e.localException(),
                )
            }
        }

//        viewModelScope.launch {
//            val songDetail = MyRetrofitDatasource.songDetail("4")
//            Log.d(TAG, "loadData: $songDetail")
//        }
    }

    fun onSongClick(datum: List<Song>, index: Int) {
        setMediasAndPlay(
            datum,
            index,
            true
        )
    }

    fun resetUiState() {
        uiState.value = uiState.value.copy(
            refreshState = 0,
            exception = null,
        )
    }

    companion object {
        const val TAG = "DiscoveryViewModel"
        const val EXPECTED_COMSUME_TIME = 1000

    }
}