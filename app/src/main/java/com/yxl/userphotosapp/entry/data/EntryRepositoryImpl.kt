package com.yxl.userphotosapp.entry.data

import com.yxl.userphotosapp.core.Api
import com.yxl.userphotosapp.entry.model.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.yxl.userphotosapp.core.Result
import com.yxl.userphotosapp.entry.model.UserResponse

class EntryRepositoryImpl : EntryRepository {
    override suspend fun login(user: User): Flow<Result<UserResponse>> {
        val resp = Api().login(user)
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
        val resp = Api().register(user)
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