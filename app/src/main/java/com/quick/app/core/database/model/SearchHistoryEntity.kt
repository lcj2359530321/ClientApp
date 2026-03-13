package com.quick.app.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "search_history")
data class SearchHistoryEntity(
    @PrimaryKey val title: String,

    val created: Long,

    /**
     * 0:商城；10：课堂
     */
    @ColumnInfo val app: Int = 0,
)