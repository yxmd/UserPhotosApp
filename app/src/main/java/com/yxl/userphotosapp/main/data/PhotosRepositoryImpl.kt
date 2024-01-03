package com.yxl.userphotosapp.main.data

import com.yxl.userphotosapp.core.Api
import com.yxl.userphotosapp.main.model.PhotoResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.yxl.userphotosapp.core.Result
import com.yxl.userphotosapp.core.db.dao.PhotoDAO
import com.yxl.userphotosapp.core.db.entity.PhotoEntity
import com.yxl.userphotosapp.main.model.Comment
import com.yxl.userphotosapp.main.model.Comments
import com.yxl.userphotosapp.main.model.ImageDtoIn
import com.yxl.userphotosapp.main.model.PhotosResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PhotosRepositoryImpl @Inject constructor(
    private val photoDAO: PhotoDAO,
    private val api: Api
) : PhotosRepository {

    override suspend fun getPhotos(page: Int, token: String): Flow<Result<PhotosResponse>> {
        val resp = api.getPhotos(token, page)
        return if (resp.isSuccessful) {
            flow {
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
                    withContext(Dispatchers.IO) {
                        photoDAO.insertPhotos(photos)
                    }
                }

                emit(Result.Success(resp.body()))
            }
        } else {
            flow {
                emit(Result.Error(message = resp.message()))
            }
        }
    }

    override fun getPhotosFromDb(): Flow<List<PhotoEntity>> {
        return photoDAO.getPhotos()
    }

    override suspend fun postPhoto(img: ImageDtoIn, token: String): Flow<Result<PhotoResponse>> {
        val resp = api.postPhoto(token, img)
        return if (resp.isSuccessful) {
            flow {
                emit(Result.Success(resp.body()))
            }
        } else {
            flow {
                emit(Result.Error(message = resp.message()))
            }
        }
    }

    override suspend fun deletePhoto(token: String, imageId: Int): Flow<Result<PhotoResponse>> {
        val resp = api.deletePhoto(token, imageId)
        return if (resp.isSuccessful) {
            flow {
                emit(Result.Success(resp.body()))
            }
        } else {
            flow {
                emit(Result.Error(message = resp.message()))
            }
        }
    }

    override suspend fun getComments(
        token: String,
        imageId: Int,
        page: Int
    ): Flow<Result<Comments>> {
        val resp = api.getComments(token, imageId, page)
        return if (resp.isSuccessful) {
            flow {
                emit(Result.Success(resp.body()))
            }
        } else {
            flow {
                emit(Result.Error(message = resp.message()))
            }
        }
    }

    override suspend fun postComment(
        token: String,
        commentDtoIn: String,
        imageId: Int
    ): Flow<Result<Comment>> {
        val resp = api.postComment(token, commentDtoIn, imageId)
        return if (resp.isSuccessful) {
            flow {
                emit(Result.Success(resp.body()))
            }
        } else {
            flow {
                emit(Result.Error(message = resp.message()))
            }
        }
    }
}