package com.yxl.userphotosapp.core.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.yxl.userphotosapp.core.db.comments.CommentDAO
import com.yxl.userphotosapp.core.db.comments.CommentEntity
import com.yxl.userphotosapp.core.db.photo.PhotoDAO
import com.yxl.userphotosapp.core.db.photo.PhotoEntity

@Database(entities = [PhotoEntity::class, CommentEntity::class], version = 1, exportSchema = false)
abstract class PhotoDatabase : RoomDatabase() {

    abstract fun photoDao(): PhotoDAO
    abstract fun commentDao(): CommentDAO
}