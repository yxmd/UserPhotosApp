package com.yxl.userphotosapp.entry.data

import com.yxl.userphotosapp.core.Api
import com.yxl.userphotosapp.entry.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.yxl.userphotosapp.core.Result
import com.yxl.userphotosapp.entry.model.UserResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EntryRepositoryImpl @Inject constructor(private val api: Api) : EntryRepository {
    override suspend fun login(user: User): Flow<Result<UserResponse>> {
        val resp = api.login(user)
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

    override suspend fun register(user: User): Flow<Result<UserResponse>> {
        val resp = api.register(user)
        return if(resp.isSuccessful){
            flow{
                emit(Result.Success(resp.body()))
            }
        }else{
            flow{
                emit(Result.Error(message = "error"))
            }
        }
    }
}