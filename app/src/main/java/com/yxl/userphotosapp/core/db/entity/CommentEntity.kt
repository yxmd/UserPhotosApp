package com.yxl.userphotosapp.core.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "comments")
class CommentEntity(
    @PrimaryKey
    val id: Int,
    @ColumnInfo("DATE")
    val date: Long,
    @ColumnInfo("MESSAGE")
    val text: String
)