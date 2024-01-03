package com.yxl.userphotosapp.main.photos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.yxl.userphotosapp.main.adapters.CommentsPagingSource
import com.yxl.userphotosapp.main.adapters.PhotosPagingSource
import com.yxl.userphotosapp.core.db.entity.PhotoEntity
import com.yxl.userphotosapp.main.data.PhotosRepositoryImpl
import com.yxl.userphotosapp.main.model.ImageDtoIn
import com.yxl.userphotosapp.main.model.PhotoResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotosViewModel @Inject constructor(
    private val repository: PhotosRepositoryImpl
) : ViewModel() {

    private val _token = MutableStateFlow("")
    val token: StateFlow<String> = _token
    private val _currentPhoto = MutableStateFlow<PhotoResponse?>(null)
    val currentPhoto: StateFlow<PhotoResponse?> = _currentPhoto
    private val _photosFromDb = MutableStateFlow<List<PhotoEntity>>(emptyList())
    val photosFromDb: StateFlow<List<PhotoEntity>> = _photosFromDb

    var photoListPager = Pager(PagingConfig(20)){
        PhotosPagingSource(repository, token.value)
    }.flow.cachedIn(viewModelScope)

    var commentListPager = Pager(PagingConfig(20)){
        CommentsPagingSource(repository, token.value, currentPhoto.value?.id!!)
    }.flow.cachedIn(viewModelScope)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getPhotosFromDb().collect {
                _photosFromDb.value = it
            }
        }
    }

    fun postPhoto(image: ImageDtoIn){
        viewModelScope.launch(Dispatchers.IO) {
            repository.postPhoto(image, token.value)

            photoListPager = Pager(PagingConfig(20)){
                PhotosPagingSource(repository, token.value)
            }.flow.cachedIn(viewModelScope)
        }
    }

    fun deletePhoto(){
        viewModelScope.launch(Dispatchers.IO){
            repository.deletePhoto(token.value, currentPhoto.value?.id!!)
        }
    }

    fun postComment(comment: String){
        viewModelScope.launch(Dispatchers.IO) {
            repository.postComment(token.value, comment, currentPhoto.value?.id!!)
        }
    }

    fun updateToken(value: String) {
        _token.value = value
    }

    fun setCurrentPhoto(photo: PhotoResponse){
        _currentPhoto.value = photo
        commentListPager = Pager(PagingConfig(20)) {
            CommentsPagingSource(repository, token.value, photo.id)
        }.flow.cachedIn(viewModelScope)
    }

}