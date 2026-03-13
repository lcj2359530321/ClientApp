package com.quick.app.feature.profile

import android.net.Uri
import android.os.CountDownTimer
import androidx.lifecycle.viewModelScope
import com.quick.app.BuildConfig
import com.quick.app.MyAppState
import com.quick.app.MyApplication
import com.quick.app.R
import com.quick.app.core.data.repository.CommonRepository
import com.quick.app.core.data.repository.UserDataRepository
import com.quick.app.core.data.repository.UserRepository
import com.quick.app.core.exception.localException
import com.quick.app.core.model.User
import com.quick.app.core.result.asResult
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
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val userDataRepository: UserDataRepository,
    private val commonRepository: CommonRepository,
) : BaseViewModel() {
    val uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val data = MutableStateFlow<User>(User())
    val isShowGenderDialog = MutableStateFlow<Boolean>(false)

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            userRepository.userDetail(MyAppState.userId)
                .asResult()
                .collectLatest { userDetail ->
                    if (userDetail.isSuccess) {
                        data.emit(userDetail.getOrThrow().data!!)
                        uiState.emit(ProfileUiState.Success)
                    } else {
                        uiState.emit(
                            ProfileUiState.Error(
                                throwable = userDetail.exceptionOrNull()!!.localException()
                            )
                        )
                    }
                }
        }
    }

    fun onUserValueChanged(param: User) {
        data.value = param
    }

    fun onSaveClick() {
        viewModelScope.launch {
            userRepository.updateUser(data.value)
                .asResult()
                .collectLatest { result ->
                    if (result.isSuccess) {
                        //再更新本地存储
                        userDataRepository.setUser(data.value.toPreferences())

                        setTipSuccessRes(R.string.save_success)
                        finish.value = true
                    } else {
                        setTipError(result.exceptionOrNull()!!.localException().tipString!!)
                    }
                }
        }
    }

    fun dismissGenderDialog() {
        isShowGenderDialog.value = false
    }

    fun showGenderDialog() {
        isShowGenderDialog.value = true
    }

    fun updateIcon(uri: Uri) {
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
                BuildConfig.FLAVOR.toRequestBody(MimeTypes.MULTIPART_FORM_DATA.toMediaType())

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