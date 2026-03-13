package com.quick.app.feature.guide

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quick.app.R
import com.quick.app.core.data.repository.UserDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GuideViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository,
) : ViewModel(
) {
    private val _datum = MutableStateFlow<List<Int>>(GUIDES)
    val datum: StateFlow<List<Int>> = _datum

    fun setNotShowGuide() {
        viewModelScope.launch {
            userDataRepository.setNotShowGuide(true)
        }
    }

    companion object {
        val GUIDES = listOf(
            R.drawable.guide1,
            R.drawable.guide2,
            R.drawable.guide3,
            R.drawable.guide4,
            R.drawable.guide5,
        )
    }
}