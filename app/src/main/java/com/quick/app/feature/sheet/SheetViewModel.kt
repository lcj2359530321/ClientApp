package com.quick.app.feature.sheet

import androidx.lifecycle.viewModelScope
import com.quick.app.core.data.repository.CommonRepository
import com.quick.app.core.exception.localException
import com.quick.app.core.model.Sheet
import com.quick.app.core.result.asResult
import com.quick.app.ui.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SheetViewModel @Inject constructor(
    private val commonRepository: CommonRepository,
) : BaseViewModel() {
    val datum = MutableStateFlow<List<Sheet>>(emptyList())

    fun loadData(query: String) {
        viewModelScope.launch {
            commonRepository.searchSheets(
                query,
            )
                .asResult()
                .collectLatest {
                    if (it.isSuccess) {
                        datum.value = it.getOrNull()!!.data?.list ?: emptyList()
                    } else {
                        setTipError(it.exceptionOrNull()!!.localException().tipString!!)
                    }
                }
        }
    }
}