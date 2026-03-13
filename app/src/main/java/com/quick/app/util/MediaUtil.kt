package com.quick.app.util

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import com.quick.app.core.exception.CommonException
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException

object MediaUtil {
    /**
     * 获取媒体库，内容提供者类型地址文件绝对路径
     *
     * @param uri content://media/external/images/media/1000000223
     */
    fun getPathFromUri(context: Context, uri: Uri): String {
        var cursor: Cursor? = null
        return try {
            val proj = arrayOf(MediaStore.Images.Media.DATA)
            cursor = context.contentResolver.query(uri, proj, null, null, null)
            val dataColumnIndex: Int =
                cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA) ?: 0
            cursor?.moveToFirst()
            cursor?.getString(dataColumnIndex)!!
        } finally {
            cursor?.close()
        }
    }

    /**
     * 获取fileprovider地址绝对地址
     *
     * @param uri content://com.quick.app.fileprovider/emo_public_2/emo_photo/emo_photo_299106698200429.jpeg
     */
    fun getPathForFileProviderUri(context: Context, uri: Uri): String {
//        if (uri.authority == context.packageName + ".fileprovider") {
        val contentResolver = context.contentResolver
        try {
            val file = File.createTempFile("temp", ".png", context.cacheDir)

            val source = contentResolver.openInputStream(uri)
            FileUtils.copyInputStreamToFile(source, file)

            return file.absolutePath
        } catch (e: IOException) {
            throw CommonException(throwable = e)
        }
    }
//    }
}