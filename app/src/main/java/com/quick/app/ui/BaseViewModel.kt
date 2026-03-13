package com.quick.app.ui

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cn.qhplus.emo.core.EmoBus
import com.quick.app.core.model.event.TipEvent
//import cn.qhplus.emo.core.EmoBus
//import com.quick.app.core.model.event.TipEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

/**
 * 通用VM
 */
open class BaseViewModel : ViewModel() {
    val finish = mutableStateOf<Boolean>(false)

    val tipError = MutableStateFlow<String?>(null)
    val tipErrorRes = MutableStateFlow<Int?>(null)
    val tipSuccessRes = MutableStateFlow<Int?>(null)

    fun resetBaseState(): Unit {
        tipError.value = null
    }

    fun setTipErrorRes(data: Int) {
        viewModelScope.launch {
            EmoBus.default.emit(
                TipEvent::class.java,
                TipEvent(tipErrorRes = data)
            )
        }
    }

    fun setTipError(data: String) {
        viewModelScope.launch {
            EmoBus.default.emit(
                TipEvent::class.java,
                TipEvent(tipError = data)
            )
        }
    }

    fun setTipSuccessRes(data: Int) {
        viewModelScope.launch {
            EmoBus.default.emit(
                TipEvent::class.java,
                TipEvent(tipSuccessRes = data)
            )
        }
    }
}