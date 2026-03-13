package com.quick.app.feature.main

import com.quick.app.R
import com.quick.app.feature.discovery.DISCOVERY_ROUTE
import com.quick.app.feature.feed.FEED_ROUTE
import com.quick.app.feature.me.ME_ROUTE
import com.quick.app.feature.shortvideo.SHORT_VIDEO_ROUTE

enum class ToplevelDestination(
    val selectedIcon:Int,
    val unselectedIcon:Int,
    val titleTextId: Int,
    val route:String,
) {
    //发现界面
    DISCOVERY(
        selectedIcon = R.drawable.tab_mall_main_selected,
        unselectedIcon = R.drawable.tab_mall_main,
        titleTextId = R.string.home,
        route = DISCOVERY_ROUTE,
    ),
    SHORT_VIDEO(
        selectedIcon = R.drawable.tab_mall_video_selected,
        unselectedIcon = R.drawable.tab_mall_video,
        titleTextId = R.string.video,
        route = SHORT_VIDEO_ROUTE,
    ),
    ME(
        selectedIcon = R.drawable.tab_mall_me_selected,
        unselectedIcon = R.drawable.tab_mall_me,
        titleTextId = R.string.me,
        route = ME_ROUTE,
    ),
    FEED(
        selectedIcon = R.drawable.tab_mall_cart_selected,
        unselectedIcon = R.drawable.tab_mall_cart,
        titleTextId = R.string.feed,
        route = FEED_ROUTE,
    ),
}