package com.quick.app.core.datastore.serializer

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import com.quick.app.core.datastore.UserDataPreferences
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

/**
 * DataStore protobuf序列器
 */
class UserDataPreferencesSerializer @Inject constructor(): Serializer<UserDataPreferences>{
    override var defaultValue: UserDataPreferences = UserDataPreferences.getDefaultInstance()

    /**
     * 从输入流中读取数据
     */
    override suspend fun readFrom(input: InputStream): UserDataPreferences =
        try {
            // readFrom is already called on the data store background thread
            UserDataPreferences.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }

    /**
     * 将数据写入输出流
     */
    override suspend fun writeTo(t: UserDataPreferences, output: OutputStream) {
        t.writeTo(output)
    }
}