package com.yxl.userphotosapp.core.db.photo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photos")
data class PhotoEntity(
    @PrimaryKey
    val id: Int,
    val url: String,
    val date: Long,
    val lat: Double,
    val lng: Double
)