package com.quick.app.core.database.di

import com.quick.app.core.database.MyDatabase
import com.quick.app.core.database.dao.SearchHistoryDao
//import com.quick.app.core.database.dao.SearchHistoryDao
import com.quick.app.core.database.dao.SongDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DaosModule {
////    @Provides
////    fun providesAreaDao(
////        database: MyCityDatabase,
////    ): CityDao = database.cityDao()
////
@Provides
fun providesSearchHistoryDao(
    database: MyDatabase,
): SearchHistoryDao = database.searchHistoryDao()

    @Provides
    fun providesSongDao(
        database: MyDatabase,
    ): SongDao = database.songDao()
}