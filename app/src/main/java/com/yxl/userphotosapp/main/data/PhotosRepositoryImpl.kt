package com.yxl.userphotosapp.main.data

import com.yxl.userphotosapp.core.Api
import com.yxl.userphotosapp.main.model.PhotoResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.yxl.userphotosapp.core.Result
import com.yxl.userphotosapp.core.db.photo.PhotoDAO
import com.yxl.userphotosapp.core.db.photo.PhotoEntity
import com.yxl.userphotosapp.main.model.Comment
import com.yxl.userphotosapp.main.model.Comments
import com.yxl.userphotosapp.main.model.ImageDtoIn
import com.yxl.userphotosapp.main.model.PhotosResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PhotosRepositoryImpl(private val photoDAO: PhotoDAO) : PhotosRepository {

    override suspend fun getPhotos(page: Int, token: String): Flow<Result<PhotosResponse>> {
        val resp = Api().getPhotos(token, page)
        return if(resp.isSuccessful){
            flow{
                resp.body()?.data?.let {
                    val photos = it.map { photo ->
                        PhotoEntity(
                            photo.id,
                            photo.url,
                            photo.date,
                            photo.lat,
                            photo.lng
                        )
                    }
                    withContext(Dispatchers.IO){
                        photoDAO.insertPhotos(photos)
                    }
                }

                emit(Result.Success(resp.body()))
            }
        }else{
            flow{
                emit(Result.Error(message = resp.message()))
            }
        }
    }

    override fun getPhotosFromDb(): Flow<List<PhotoEntity>>{
        return photoDAO.getPhotos()
    }

    override suspend fun postPhoto(img: ImageDtoIn, token: String): Flow<Result<PhotoResponse>> {
        val resp = Api().postPhoto(token, img)
        return if(resp.isSuccessful){
            flow{
                emit(Result.Success(resp.body()))
            }
        }else{
            flow{
                emit(Result.Error(message = resp.message()))
            }
        }
    }

    override suspend fun deletePhoto(token: String, imageId: Int): Flow<Result<PhotoResponse>> {
        val resp = Api().deletePhoto(token, imageId)
        return if(resp.isSuccessful){
            flow{
                emit(Result.Success(resp.body()))
            }
        }else{
            flow{
                emit(Result.Error(message = resp.message()))
            }
        }
    }

    override suspend fun getComments(
        token: String,
        imageId: Int,
        page: Int
    ): Flow<Result<Comments>> {
        val resp = Api().getComments(token, imageId, page)
        return if(resp.isSuccessful){
            flow {
                emit(Result.Success(resp.body()))
            }
        }else{
            flow{
                emit(Result.Error(message = resp.message()))
            }
        }
    }

    override suspend fun postComment(
        token: String,
        commentDtoIn: String,
        imageId: Int
    ): Flow<Result<Comment>> {
        val resp = Api().postComment(token, commentDtoIn, imageId)
        return if(resp.isSuccessful){
            flow {
                emit(Result.Success(resp.body()))
            }
        }else{
            flow{
                emit(Result.Error(message = resp.message()))
            }
        }
    }
}