package com.yxl.userphotosapp.core

import com.yxl.userphotosapp.entry.model.User
import com.yxl.userphotosapp.entry.model.UserResponse
import com.yxl.userphotosapp.main.model.Comment
import com.yxl.userphotosapp.main.model.Comments
import com.yxl.userphotosapp.main.model.ImageDtoIn
import com.yxl.userphotosapp.main.model.PhotoResponse
import com.yxl.userphotosapp.main.model.PhotosResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface Api {

    @Headers("Content-Type: application/json")
    @POST("/api/account/signin")
    suspend fun login(
        @Body user: User
    ): Response<UserResponse>

    @Headers("Content-Type: application/json")
    @POST("/api/account/signup")
    suspend fun register(
        @Body user: User
    ): Response<UserResponse>

    @GET("/api/image")
    suspend fun getPhotos(
        @Header("Access-Token") token: String,
        @Query("page") page: Int
    ): Response<PhotosResponse>

    @POST("/api/image")
    suspend fun postPhoto(
        @Header("Access-Token") token: String,
        @Body img: ImageDtoIn
    ): Response<PhotoResponse>
    //error on server
    @DELETE("/api/image/{id}")
    suspend fun deletePhoto(
        @Header("Access-Token") token: String,
        @Path("id") id: Int
    ): Response<PhotoResponse>

    @GET("/api/image/{imageId}/comment")
    suspend fun getComments(
        @Header("Access-Token") token: String,
        @Path("imageId") imageId: Int,
        @Query("page") page: Int
    ): Response<Comments>

    @POST("/api/image/{imageId}/comment")
    suspend fun postComment(
        @Header("Access-Token") token: String,
        @Body commentDtoIn: String,
        @Path("imageId") imageId: Int
    ): Response<Comment>

    companion object{

        const val BASE_URL = "https://junior.balinasoft.com/"

    }
}