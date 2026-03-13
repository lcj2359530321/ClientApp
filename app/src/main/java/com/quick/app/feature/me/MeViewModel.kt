package com.quick.app.feature.me

import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.quick.app.MyAppState
import com.quick.app.core.data.repository.SheetRepository
import com.quick.app.core.exception.localException
import com.quick.app.core.model.Sheet
import com.quick.app.ui.BaseViewModel
import com.quick.app.ui.MyAppUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MeViewModel @Inject constructor(
    private val sheetRepository: SheetRepository,
) : BaseViewModel(
) {
    val createDatum = MutableStateFlow<List<Sheet>>(emptyList())
    val collectDatum = MutableStateFlow<List<Sheet>>(emptyList())

    fun loadData() : Unit {
        if(MyAppState.session.isNotBlank()){
            //登录了，才加载数据
            viewModelScope.launch {
                //创建异步任务
                val createSheetsJob =
                    async { runCatching { sheetRepository.createSheets(MyAppState.userId) } }
                val collectSheetsJob = async {
                    runCatching {
                        sheetRepository.collectSheets(
                            MyAppState.userId
                        )

                    }
                }

                // 等待所有作业完成
                val indexResult = createSheetsJob.await()
                val productsResult = collectSheetsJob.await()

                if (indexResult.isFailure || productsResult.isFailure) {
                    //发生异常了，例如：网络错误，解析数据失败等
                    val throwable =
                        indexResult.exceptionOrNull() ?: productsResult.exceptionOrNull()

                    tipError.value = throwable!!.localException().tipString
                    return@launch
                }

                //都请求成功

                //判断业务是否请求成功
                val indexResultData = indexResult.getOrNull()!!
                val productsResultResultData = productsResult.getOrNull()!!

                if (!indexResultData.isSucceeded) {
                    //业务失败
                    tipError.value = indexResultData.message
                    return@launch
                }

                if (!productsResultResultData.isSucceeded) {
                    //业务失败
                    tipError.value = productsResultResultData.message
                    return@launch
                }

                createDatum.value = indexResultData.data?.list ?: emptyList()
                collectDatum.value = productsResultResultData.data?.list ?: emptyList()
            }
        }
    }
}