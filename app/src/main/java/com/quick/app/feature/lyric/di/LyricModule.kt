package com.quick.app.feature.lyric.di

import android.content.Context
import com.quick.app.core.data.repository.SongRepository
import com.quick.app.core.data.repository.UserDataRepository
import com.quick.app.core.media.MediaServiceConnection
//import com.quick.app.feature.globallyric.GlobalLyricManager
import com.quick.app.feature.lyric.LyricManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LyricModule {
    @Provides
    @Singleton
    fun providesLyricManager(
        @ApplicationContext context: Context,
        mediaServiceConnection: MediaServiceConnection,
        songRepository: SongRepository,
        userDataRepository: UserDataRepository,
    ): LyricManager {
        return LyricManager(context, mediaServiceConnection, songRepository, userDataRepository)
    }

//    @Provides
//    @Singleton
//    fun providesGlobalLyricManager(
//        @ApplicationContext context: Context,
//        mediaServiceConnection: MediaServiceConnection,
//        lyricManager: LyricManager,
//        userDataRepository: UserDataRepository,
//    ): GlobalLyricManager {
//        return GlobalLyricManager(context, mediaServiceConnection, lyricManager, userDataRepository)
//    }

}