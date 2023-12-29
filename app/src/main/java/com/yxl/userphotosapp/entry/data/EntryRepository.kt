package com.yxl.userphotosapp.entry.data

import com.yxl.userphotosapp.core.Result
import com.yxl.userphotosapp.entry.model.User
import com.yxl.userphotosapp.entry.model.UserResponse
import kotlinx.coroutines.flow.Flow

interface EntryRepository {

    suspend fun login(user: User) : Flow<Result<UserResponse>>
    suspend fun register(user: User) : Flow<Result<UserResponse>>
}