package com.quick.app.core.database.di

import android.content.Context
import androidx.room.Room
import com.quick.app.MyAppState
import com.quick.app.core.database.MyDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    /**
     * 提供数据库实例
     */
    @Provides
    fun providesMyDatabase(
        @ApplicationContext context: Context,
    ): MyDatabase {
        if (MyAppState.myDatabase == null) {
            val databaseName = "my_database_${MyAppState.userId}"
            MyAppState.myDatabase = Room.databaseBuilder(
                context,
                MyDatabase::class.java,
                databaseName,
            ).build()
        }
        return MyAppState.myDatabase!!
    }
}