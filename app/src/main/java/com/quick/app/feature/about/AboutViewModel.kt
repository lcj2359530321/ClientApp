package com.quick.app.feature.about

import androidx.lifecycle.viewModelScope
import com.quick.app.MyApplication
import com.quick.app.core.model.AboutModel
import com.quick.app.ui.BaseViewModel
import com.quick.app.util.SuperPackageUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AboutViewModel @Inject constructor(

) : BaseViewModel() {
    val uiState = MutableStateFlow<AboutUiState>(AboutUiState.Loading)

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            uiState.value = AboutUiState.Success(
                AboutModel(
                    versionName = SuperPackageUtil.getVersionName(MyApplication.instance),
                    versionCode = SuperPackageUtil.getVersionCode(MyApplication.instance),
                )
            )
        }
    }
}