package com.yxl.userphotosapp.core.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photos")
class PhotoEntity(
    @PrimaryKey
    val id: Int,
    @ColumnInfo("URL")
    val url: String,
    @ColumnInfo("DATE")
    val date: Long,
    @ColumnInfo("LAT")
    val lat: Double,
    @ColumnInfo("LNG")
    val lng: Double
)