package com.quick.app.util

import android.content.Context
import android.os.Environment
import android.provider.MediaStore
import java.io.File

/**
 * 文件工具类
 */
object StorageUtil {
    /**
     * 获取缓存广告目录
     *
     * @param context
     * @param data
     * @return
     */
    fun adFile(context: Context, data: String): File {
        val file = File(
            context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)!!.absolutePath + "/ads",
            data
        )
        val parentFile = file.parentFile
        if (!parentFile.exists()) {
            parentFile.mkdirs()
        }
        return file
    }

//    const val MP4 = "mp4"
//
//    /**
//     * MediaStore，ContentProviders内容提供者data列
//     */
//    private const val COLUMN_DATA = "_data"
//
//    /**
//     * mp3
//     */
//    const val MP3 = "mp3"
//
//    /**
//     * 获取应用扩展sdcard中的路径
//     *
//     * @param context
//     * @param userId  用户id
//     * @param title   标题
//     * @param suffix  后缀
//     * @return
//     */
//    fun getExternalPath(context: Context, userId: String?, title: String?, suffix: String?): File {
//        //获取下载文件类型目录
//        //该路径下的文件卸载应用后会清空
//        //在Android 10路径为：
//        // /storage/emulated/0/Android/data/com.quick.app1/files/Download/
//        val dir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
//
//        //格式化路径
//        val path = String.format("MyCloudMusic/%s/%s/%s.%s", userId, suffix, title, suffix)
//
//        //创建文件对象
//        val file = File(dir, path)
//        if (!file.parentFile.exists()) {
//            //如果上层目录不存在
//
//            //就创建
//            file.parentFile.mkdirs()
//        }
//
//        //返回文件的绝对路径
//        return file
//    }

    /**
     * 根据id获取audio content uri
     *
     * @param id
     * @return
     */
    fun getAudioContentUri(id: Long): String {
        //创建一个builder
        //并将原来的参数拷贝到新的builder
        return MediaStore.Audio.Media.EXTERNAL_CONTENT_URI.buildUpon() //添加路径
            .appendPath(id.toString()) //创建为uri
            .build() //转为字符串
            .toString()
    }

//    /**
//     * 保存图片到系统相册
//     *
//     * @param context
//     * @return
//     */
//    fun savePicture(context: Context, data: Bitmap): Uri? {
//        //创建媒体路径
//        val uri = insertPictureMediaStore(
//            context,
//            String.format("ixuea_code_%s.jpg", nowyyyyMMddHHmmss())
//        )
//            ?: //获取路径失败
//            return null
//
//        //将原来的图片保存到目标uri
//        //也就是保存到系统图片媒体库
//        return saveFile(context, data, uri)
//    }
//
//    /**
//     * 创建相册图片路径
//     *
//     * @param context
//     * @return
//     */
//    private fun insertPictureMediaStore(context: Context, name: String): Uri? {
//        //创建内容集合
//        val contentValues = ContentValues()
//
//        //媒体显示的名称
//        //设置为文件的名称
//        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, name)
//
//        //媒体类型
//        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            //拍摄媒体的时间
//            contentValues.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis())
//        }
//        return context.contentResolver.insert(
//            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//            contentValues
//        )
//    }
//
//    /**
//     * 保存文件到uri对应的路径
//     *
//     * @param context
//     * @return
//     */
//    private fun saveFile(context: Context, data: Bitmap, uri: Uri): Uri? {
//        try {
//            //获取uri对应的文件描述器
//            val parcelFileDescriptor = context.contentResolver.openFileDescriptor(uri, "w")
//            try {
//                FileOutputStream(parcelFileDescriptor!!.fileDescriptor).use { out ->
//                    //保存bitmap
//                    data.compress(Bitmap.CompressFormat.JPEG, 90, out)
//                    return uri
//                }
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        } catch (e: FileNotFoundException) {
//            e.printStackTrace()
//        }
//        return null
//    }
//
//    /**
//     * 获取MediaStore uri的路径
//     *
//     * @param context
//     * @param uri
//     * @return
//     */
//    fun getMediaStorePath(context: Context, uri: Uri): String? {
//        return getDataColumn(context, uri)
//    }
//
//    /**
//     * 获取uri对应的data列值
//     * 其实就是文件路径
//     * 这种写法支持MediaStore，ContentProviders
//     */
//    private fun getDataColumn(context: Context, uri: Uri): String? {
//        try {
//            context //获取内容提供者
//                .contentResolver //查询数据
//                /**
//                 * @param uri 以content://开通的地址
//                 * @param projection 返回哪些列
//                 * @param selection 查询条件，类似sql中where条件
//                 * @param selectionArgs 查询条件参数
//                 * @param sortOrder 排序参数
//                 */
//                .query(
//                    uri, arrayOf(COLUMN_DATA),
//                    null,
//                    null,
//                    null
//                ).use { cursor ->
//                    if (cursor != null && cursor.moveToFirst()) {
//                        //有数据
//
//                        //获取这一列的索引
//                        val index = cursor.getColumnIndexOrThrow(COLUMN_DATA)
//
//                        //获取这一列字符类型数据
//                        return cursor.getString(index)
//                    }
//                }
//        } catch (e: IllegalArgumentException) {
//            e.printStackTrace()
//        }
//        return null
//    }
//
//
//    public fun saveImage(context: Context, bitmap: Bitmap, filename: String): Uri {
//        val resolver = context.contentResolver
//        val contentValues = ContentValues().apply {
//            put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
//            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
//                put(MediaStore.MediaColumns.IS_PENDING, 1)
//            }
//        }
//
//        val contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
//
//        val imageUri = resolver.insert(contentUri, contentValues)
//
//        imageUri?.let {
//            try {
//                resolver.openOutputStream(it)?.use { outputStream ->
//                    if (!bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)) {
//                        throw IOException("Failed to save bitmap.")
//                    }
//                }
//
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                    contentValues.clear()
//                    contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
//                    resolver.update(imageUri, contentValues, null, null)
//                }
//            } catch (e: IOException) {
//                resolver.delete(imageUri, null, null)
//                throw e
//            }
//        }
//
//        return imageUri ?: throw IOException("Failed to create image file.")
//    }

}