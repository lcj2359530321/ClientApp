package com.quick.app.feature.shortvideo

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.exoplayer.ExoPlayer
import com.quick.app.core.data.repository.ContentRepository
import com.quick.app.core.model.Content
import com.quick.app.core.result.asResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShortVideoViewModel @Inject constructor(
    private val contentRepository: ContentRepository,
    application: Application,
) : AndroidViewModel(
    application
) {
    private val _datum = MutableStateFlow<List<Content>>(emptyList())
    val datum: StateFlow<List<Content>> = _datum

    var currentIndex = -1

    private var isPausePlay: Boolean = true

    val players = listOf(
        createPlayer(),
        createPlayer(),
        createPlayer(),
        createPlayer(),
        createPlayer()
    )

    init {
        loadData()
    }

    private fun loadData() {
        loadMore()
    }


    private fun loadMore() {
        viewModelScope.launch {
            contentRepository
                .contents()
                .asResult()
                .collectLatest {
                    if (it.isSuccess) {
                        _datum.value = it.getOrNull()?.data?.list ?: emptyList()
                    }
                }
        }
    }

    private fun createPlayer(): ExoPlayer {
        return ExoPlayer.Builder(getApplication()).build().apply {
            repeatMode = ExoPlayer.REPEAT_MODE_ONE
        }
    }

    fun onPageChange(index: Int) {
        if (currentIndex != -1) {
            players[currentIndex % 5].pause()
        }

        players[index % 5].play()

        currentIndex = index
    }

    fun pausePlay() {
        if (currentIndex != -1) {
            players[currentIndex % 5].run {
                if (isPlaying) {
                    pause()
                }
            }
        }
    }

    fun resumePlay() {
        if (isPausePlay) {
            return
        }

        if (currentIndex != -1) {
            players[currentIndex % 5].run {
                if (!isPlaying) {
                    play()
                }
            }
        }
    }

    fun setPausePlay(pausePlay: Boolean) {
        this.isPausePlay = pausePlay
    }
}