package com.quick.app.feature.musicappwidget

import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import com.quick.app.core.media.MediaServiceConnection
import com.quick.app.feature.lyric.LyricManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * 音乐桌面微件
 *
 * 运行在其他应用（一般是桌面）内
 *
 * https://developer.android.com/develop/ui/compose/glance/create-app-widget?hl=zh-cn#define-glanceappwidget
 */
@AndroidEntryPoint
class MusicAppWidgetReceiver : GlanceAppWidgetReceiver(){
    @Inject
    lateinit var mediaServiceConnection: MediaServiceConnection

    @Inject
    lateinit var lyricManager: LyricManager

    //    override val glanceAppWidget: GlanceAppWidget = MusicAppWidget(musicServiceConnection)
    override val glanceAppWidget: GlanceAppWidget
        get() = MusicAppWidget(mediaServiceConnection, lyricManager)
}