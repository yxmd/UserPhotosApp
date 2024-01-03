package com.yxl.userphotosapp.core

import android.content.Context
import androidx.room.Room
import com.yxl.userphotosapp.core.Api.Companion.BASE_URL
import com.yxl.userphotosapp.core.db.PhotoDatabase
import com.yxl.userphotosapp.core.db.photo.PhotoDAO
import com.yxl.userphotosapp.entry.data.EntryRepositoryImpl
import com.yxl.userphotosapp.main.data.PhotosRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providePhotoDatabase(@ApplicationContext context: Context): PhotoDatabase{
        return Room.databaseBuilder(
            context,
            PhotoDatabase::class.java,
            "photo_db"
        ).build()
    }

    @Singleton
    @Provides
    fun providePhotoDAO(db: PhotoDatabase) = db.photoDao()

    @Singleton
    @Provides
    fun provideCommentDAO(db: PhotoDatabase) = db.commentDao()

    @Singleton
    @Provides
    fun provideRetrofit() = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(ApiWorker.gsonConverter)
        .client(ApiWorker.client)
        .build()

    @Singleton
    @Provides
    fun provideApi(retrofit: Retrofit) = retrofit.create(Api::class.java)

    @Singleton
    @Provides
    fun providePhotoRepository(dao: PhotoDAO, api: Api) = PhotosRepositoryImpl(dao, api)

    @Singleton
    @Provides
    fun provideEntryRepository(api: Api) = EntryRepositoryImpl(api)

}