package com.quick.app.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.quick.app.core.database.dao.SearchHistoryDao
import com.quick.app.core.database.dao.SongDao
import com.quick.app.core.database.model.SearchHistoryEntity
import com.quick.app.core.database.model.SongEntity

@Database(
    entities = [
        SongEntity::class,
        SearchHistoryEntity::class,
    ],
    version = 2,
//    autoMigrations = [
//        AutoMigration(from = 1, to = 2),
//        AutoMigration(from = 2, to = 3, spec = DatabaseMigrations.Schema2to3::class),
//        AutoMigration(from = 3, to = 4),
//        AutoMigration(from = 4, to = 5),
//        AutoMigration(from = 5, to = 6),
//        AutoMigration(from = 6, to = 7),
//        AutoMigration(from = 7, to = 8),
//        AutoMigration(from = 8, to = 9),
//        AutoMigration(from = 9, to = 10),
//        AutoMigration(from = 10, to = 11, spec = DatabaseMigrations.Schema10to11::class),
//        AutoMigration(from = 11, to = 12, spec = DatabaseMigrations.Schema11to12::class),
//        AutoMigration(from = 12, to = 13),
//        AutoMigration(from = 13, to = 14),
//    ],
    exportSchema = true,
)
//@TypeConverters(
//    InstantConverter::class,
//)
abstract class MyDatabase : RoomDatabase() {
    abstract fun songDao(): SongDao
    abstract fun searchHistoryDao(): SearchHistoryDao
}