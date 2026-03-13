
package com.quick.app.core.media.di

import android.content.ComponentName
import android.content.Context
import com.quick.app.core.data.repository.SongRepository
import com.quick.app.core.data.repository.UserDataRepository
//import com.quick.app.core.data.repository.SongRepository
//import com.quick.app.core.data.repository.UserDataRepository
import com.quick.app.core.media.MediaService
import com.quick.app.core.media.MediaServiceConnection
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object MediaModule {
    @Provides
    fun provideMediaServiceConnection(
        @ApplicationContext context: Context,
        songRepository: SongRepository,
        userDataRepository: UserDataRepository,
    ): MediaServiceConnection {
        return MediaServiceConnection.getInstance(
            context,
            ComponentName(context, MediaService::class.java),
            songRepository,
            userDataRepository,
        )
    }
}