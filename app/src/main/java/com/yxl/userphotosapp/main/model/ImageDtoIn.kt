package com.yxl.userphotosapp.main.model

import com.google.gson.annotations.SerializedName

data class ImageDtoIn(
    @SerializedName("base64Image")
    val base64Image: String,
    @SerializedName("date")
    val date: Long,
    @SerializedName("lat")
    val lat: Double,
    @SerializedName("lng")
    val lng: Double
)
