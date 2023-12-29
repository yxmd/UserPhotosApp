package com.yxl.userphotosapp.entry.model

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("login")
    val login: String,
    @SerializedName("password")
    val password: String
)

data class UserResponse(
    @SerializedName("data")
    val data: Data
)

data class Data(
    @SerializedName("userId")
    val userId: Long,
    @SerializedName("login")
    val login: String,
    @SerializedName("token")
    val token: String
)