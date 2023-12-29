package com.yxl.userphotosapp.core.db.comments

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
@Dao
interface CommentDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComments(comments: List<CommentEntity>)

    @Query("SELECT * FROM comments")
    fun getComments(): Flow<List<CommentEntity>>
}