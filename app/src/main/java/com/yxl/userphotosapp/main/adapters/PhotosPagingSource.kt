package com.yxl.userphotosapp.main.adapters

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.yxl.userphotosapp.core.Result
import com.yxl.userphotosapp.main.data.PhotosRepository
import com.yxl.userphotosapp.main.model.PhotoResponse

class PhotosPagingSource(
    private val repository: PhotosRepository, val token: String
) : PagingSource<Int, PhotoResponse>() {

    private val loadedData = mutableListOf<PhotoResponse>()

    override fun getRefreshKey(state: PagingState<Int, PhotoResponse>): Int?{
        return state.anchorPosition?.let { anchorPosition->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1) ?:
            state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PhotoResponse> {
        val currentPage = params.key ?: 0
        val response = repository.getPhotos(currentPage, token)
        var result: LoadResult<Int, PhotoResponse>? = null
        response.collect { resultValue ->
            result = when (resultValue) {
                is Result.Error -> {
                    LoadResult.Error(Exception(resultValue.message!!))
                }

                is Result.Success -> {
                    val responseData = resultValue.data?.data ?: emptyList()
                    loadedData.addAll(responseData)
                    LoadResult.Page(
                        data = responseData,
                        prevKey = if (currentPage == 0) null else -1,
                        nextKey = if (responseData.isEmpty()) null else currentPage + 1
                    )
                }
            }
        }
        return result ?: LoadResult.Error(Exception("No data available"))
    }

    fun getLoadedData(): List<PhotoResponse> {
        return loadedData
    }
}