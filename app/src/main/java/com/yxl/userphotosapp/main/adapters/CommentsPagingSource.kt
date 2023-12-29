package com.yxl.userphotosapp.main.adapters

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.yxl.userphotosapp.core.Result
import com.yxl.userphotosapp.main.data.PhotosRepository
import com.yxl.userphotosapp.main.model.Comment

class CommentsPagingSource(
    private val repository: PhotosRepository,
    val token: String,
    val imageId: Int
) : PagingSource<Int, Comment>() {


    override fun getRefreshKey(state: PagingState<Int, Comment>): Int?{
        return state.anchorPosition?.let { anchorPosition->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1) ?:
            state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Comment> {
        val currentPage = params.key ?: 0
        val response = repository.getComments(token, imageId, currentPage)
        var result: LoadResult<Int, Comment>? = null
        response.collect { resultValue ->
            result = when (resultValue) {
                is Result.Error -> {
                    LoadResult.Error(Exception(resultValue.message!!))
                }

                is Result.Success -> {
                    val responseData = resultValue.data?.data ?: emptyList()
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


}