package com.yxl.userphotosapp.main.data

import com.yxl.userphotosapp.main.model.PhotoResponse
import kotlinx.coroutines.flow.Flow
import com.yxl.userphotosapp.core.Result
import com.yxl.userphotosapp.core.db.entity.PhotoEntity
import com.yxl.userphotosapp.main.model.Comment
import com.yxl.userphotosapp.main.model.Comments
import com.yxl.userphotosapp.main.model.ImageDtoIn
import com.yxl.userphotosapp.main.model.PhotosResponse

interface PhotosRepository {

    suspend fun getPhotos(page: Int, token: String): Flow<Result<PhotosResponse>>
    suspend fun postPhoto(img: ImageDtoIn, token: String): Flow<Result<PhotoResponse>>
    suspend fun deletePhoto(token: String, imageId: Int): Flow<Result<PhotoResponse>>
    suspend fun getComments(token: String, imageId: Int, page: Int): Flow<Result<Comments>>
    suspend fun postComment(token: String, commentDtoIn: String, imageId: Int): Flow<Result<Comment>>
    fun getPhotosFromDb(): Flow<List<PhotoEntity>>

}