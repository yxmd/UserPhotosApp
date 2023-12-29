package com.yxl.userphotosapp.main.model


import com.google.gson.annotations.SerializedName

data class Comment(
    @SerializedName("id")
    val id: Int,
    @SerializedName("date")
    val date: Long,
    @SerializedName("text")
    val text: String
)

data class Comments(
    @SerializedName("data")
    val data: List<Comment>,
)