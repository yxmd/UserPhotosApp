package com.yxl.userphotosapp.main.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PhotosResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("data")
    val data: List<PhotoResponse>,
) : Serializable

data class PhotoResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("url")
    val url: String,
    @SerializedName("date")
    val date: Long,
    @SerializedName("lat")
    val lat: Double,
    @SerializedName("lng")
    val lng: Double
) : Serializable