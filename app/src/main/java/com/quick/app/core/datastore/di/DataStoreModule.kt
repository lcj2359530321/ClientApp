package com.quick.app.core.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.quick.app.core.datastore.UserDataPreferences
import com.quick.app.core.datastore.serializer.UserDataPreferencesSerializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataStoreModule {

    @Provides
    @Singleton
    internal fun providesUserPreferencesDataStore(
        @ApplicationContext context: Context,
        userDataPreferencesSerializer: UserDataPreferencesSerializer,
    ): DataStore<UserDataPreferences> =
        DataStoreFactory.create(
            serializer = userDataPreferencesSerializer,
            scope = CoroutineScope(Dispatchers.IO),
//            migrations = listOf(
//                IntToStringIdsMigration,
//            ),
        ) {
            context.dataStoreFile("user_preferences.pb")
        }
}