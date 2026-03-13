package com.quick.app.feature.publishfeed

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.quick.app.BuildConfig
import com.quick.app.MyApplication
import com.quick.app.R
import com.quick.app.core.data.repository.CommonRepository
import com.quick.app.core.data.repository.FeedRepository
import com.quick.app.core.exception.CommonException
import com.quick.app.core.exception.localException
import com.quick.app.core.model.Feed
import com.quick.app.core.model.MediaResource
import com.quick.app.core.result.asResult
import com.quick.app.ui.BaseViewModel
import com.quick.app.util.Constant
import com.quick.app.util.MediaUtil
import com.quick.app.util.MimeTypes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject


@HiltViewModel
class PublishFeedViewModel
@Inject constructor
    (
    private val feedRepository: FeedRepository,
    private val commonRepository: CommonRepository,
) : BaseViewModel() {
    private val _content = MutableStateFlow<String>("")
    val content: StateFlow<String> = _content

    private var mediasUploaded: List<MediaResource>? = null

    private val _pickedItems =
        MutableStateFlow<ArrayList<Uri>>(arrayListOf())
    val pickedItems: StateFlow<ArrayList<Uri>> = _pickedItems

    private val _medias =
        MutableStateFlow<List<Any>>(emptyList())
    val medias: StateFlow<List<Any>> = _medias

    val loadingString = MutableStateFlow<String?>(null)

    init {
        setMedias(
            emptyList()
        )
    }

    fun setMedias(data: List<Uri>) {
        viewModelScope.launch {
            _pickedItems.value = ArrayList(data)

            if (data.size < 9) {
                val r = mutableListOf<Any>()
                r.addAll(data)
                r.add(R.drawable.add_fill)
                _medias.emit(
                    r
                )

            } else {
                _medias.emit(data)
            }
        }
    }

    fun removeMedia(data: Uri) {
        viewModelScope.launch {
            _pickedItems.value = ArrayList(_pickedItems.value.filter { it != data })
            setMedias(_pickedItems.value)
        }
    }

    fun onSaveClick() {
        viewModelScope.launch {
            if (content.value.isBlank()) {
                setTipErrorRes(
                    R.string.hint_feed
                )
                return@launch
            }

            if (content.value.length > 140) {
                setTipErrorRes(
                    R.string.error_feed_length
                )
                return@launch
            }

            if (pickedItems.value.isEmpty()) {
                save()
            } else {
                uploadMedia()
            }
        }

    }

    private fun uploadMedia() {
        viewModelScope.launch {
            uploads(pickedItems.value.map {
                MediaUtil.getPathFromUri(MyApplication.instance, it)
            })
                .collectLatest {
                    it.onProgress {
                        //上传进度
                        loadingString.value = MyApplication.instance.getString(
                            R.string.loading_upload, it + 1
                        )
                    }
                    it.onSuccess {
                        //上传成功
                        mediasUploaded = it
                        loadingString.value = null
                        save()
                    }
                    it.onFailure {
                        loadingString.value = null
                        setTipError(it.localException().tipString!!)
                    }
                }
        }
    }

    fun uploads(data: List<String>): Flow<UploadResult<List<MediaResource>>> {
        return flow<UploadResult<List<MediaResource>>> {
//            emit(UploadResult.OnProgress(0))
//            delay(1000)
//            emit(UploadResult.OnProgress(1))
//            delay(1000)
//            emit(UploadResult.Success())

            //创建结果数组
            val results = mutableListOf<MediaResource>()

            data.forEachIndexed { index, it ->
                emit(UploadResult.OnProgress(index))

                val file = File(it)

                //文件表单项
                val fileBody =
                    file.asRequestBody(MimeTypes.IMAGE_ANY.toMediaType())
                val multipartBody =
                    MultipartBody.Part.createFormData("file", file.getName(), fileBody)

                //渠道项
                val flavorBody =
                    BuildConfig.FLAVOR.toRequestBody(MimeTypes.MULTIPART_FORM_DATA.toMediaType())

                val targetResult = commonRepository.uploadFileSuspend(
                    multipartBody,
                    flavorBody,
                    Constant.VALUE1.toString()
                        .toRequestBody(MimeTypes.MULTIPART_FORM_DATA.toMediaType())
                )

                if (!targetResult.isSucceeded) {
                    emit(UploadResult.Failure(CommonException(targetResult)))
                    return@flow
                }

                results.add(MediaResource(uri = targetResult.data!!.id))
            }

            emit(UploadResult.Success(results))
        }.flowOn(Dispatchers.IO) //通过flowOn方法切换到io线程
    }

    private fun save() {
        viewModelScope.launch {
            val param = Feed(
                content = content.value.trim(),
                medias = mediasUploaded,
            )
            feedRepository.createFeed(param)
                .asResult()
                .collectLatest {
                    if (it.isSuccess) {
                        finish.value = true
                    } else {
                        setTipError(it.exceptionOrNull()!!.localException().tipString!!)
                    }
                }
        }
    }

    fun onContentChanged(data: String): Unit {
        viewModelScope.launch {
            _content.value = data
        }
    }
}