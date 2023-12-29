package com.yxl.userphotosapp.core.db.comments

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
@Entity(tableName = "comments")
class CommentEntity(
    @PrimaryKey
    val id: Int,
    val date: Long,
    val text: String
)