package com.yxl.userphotosapp.core.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.yxl.userphotosapp.core.db.dao.CommentDAO
import com.yxl.userphotosapp.core.db.entity.CommentEntity
import com.yxl.userphotosapp.core.db.dao.PhotoDAO
import com.yxl.userphotosapp.core.db.entity.PhotoEntity

@Database(entities = [PhotoEntity::class, CommentEntity::class], version = 2, exportSchema = false)
abstract class PhotoDatabase : RoomDatabase() {

    abstract fun photoDao(): PhotoDAO
    abstract fun commentDao(): CommentDAO

    companion object{
        const val DATABASE_NAME = "photos_db"
    }
}