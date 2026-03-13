package com.quick.app.feature.createsheet


import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.quick.app.BuildConfig
import com.quick.app.MyApplication
import com.quick.app.R
import com.quick.app.core.data.repository.CommonRepository
import com.quick.app.core.data.repository.SheetRepository
import com.quick.app.core.exception.localException
import com.quick.app.core.model.SHEET_EMPTY
import com.quick.app.core.model.Sheet
import com.quick.app.core.result.asResult
import com.quick.app.feature.sheetdetail.SHEET_ID
import com.quick.app.ui.BaseViewModel
import com.quick.app.util.Constant
import com.quick.app.util.MediaUtil
import com.quick.app.util.MimeTypes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class CreateSheetViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val sheetRepository: SheetRepository,
    private val commonRepository: CommonRepository,
) : BaseViewModel() {
    private val sheetId: String = checkNotNull(savedStateHandle[SHEET_ID])

    val data = MutableStateFlow<Sheet>(SHEET_EMPTY())

    fun onValueChange(param: Sheet) {
        data.value = param
    }

    fun onSaveClick() {
        val param = data.value
        if (param.title.isBlank()) {
            setTipErrorRes(
                R.string.enter_sheet_title
            )
//            tipErrorRes.tryEmit(
//                R.string.enter_sheet_title
//            )
            return
        }

        viewModelScope.launch {
            if (sheetId == Constant.VALUE_NO_STRING) {
                sheetRepository.createSheet(param)
            } else {
                sheetRepository.updateSheet(param)
            }
                .asResult()
                .collectLatest {
                    if (it.isSuccess) {
                        setTipSuccessRes(
                            R.string.save_success
                        )
                        finish.value = true
                    } else {
                        setTipError(
                            it.exceptionOrNull()!!.localException().tipString!!
                        )
                    }
                }
        }
    }



    fun updateIcon(uri: Uri) {
//        data.value = data.value.copy(
//            icon = uri.toString()
//        )
        viewModelScope.launch {
            val localPath =
                MediaUtil.getPathForFileProviderUri(MyApplication.instance, uri)

            val file = File(localPath)

            //文件表单项
            val fileBody =
                file.asRequestBody(MimeTypes.IMAGE_ANY.toMediaType())
            val multipartBody =
                MultipartBody.Part.createFormData("file", file.getName(), fileBody)

            //渠道项
            val flavorBody =
                BuildConfig.FLAVOR
                    .toRequestBody(MimeTypes.MULTIPART_FORM_DATA.toMediaType())

            commonRepository.uploadFile(
                multipartBody,
                flavorBody,
                Constant.VALUE1.toString()
                    .toRequestBody(MimeTypes.MULTIPART_FORM_DATA.toMediaType())
            )
                .asResult()
                .collectLatest { result ->
                    if (result.isSuccess) {
                        data.value = data.value.copy(
                            icon = result.getOrThrow().data!!.id
                        )
                    } else {
                        setTipError(
                            result.exceptionOrNull()!!.localException().tipString!!
                        )
                    }
                }
        }
    }
}