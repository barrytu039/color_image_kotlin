package com.barry.color_imgage_kotlin.pagesource

import androidx.paging.PagingSource
import androidx.paging.PagingSource.LoadResult.Error
import androidx.paging.PagingState
import com.barry.color_imgage_kotlin.network.APIService
import com.barry.color_imgage_kotlin.network.ColorImgModel
import java.lang.Exception

/**
 * Created by Barry Tu on 2024/2/28.
 */
class ColorImageRemoteDataSource(val service: APIService, val size: Int): PagingSource<Long, ColorImgModel>() {
    override fun getRefreshKey(state: PagingState<Long, ColorImgModel>): Long? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Long>): LoadResult<Long, ColorImgModel> {
        try {
            val page = params.key?:0L
            val result = service.getPhoto(page.toString(), size.toString())
            if (result.isSuccessful) {
                result.body()?.let {
                    return LoadResult.Page(
                        data = it,
                        prevKey = null,
                        nextKey = it.last().id.toLong()
                    )
                }
            }
            return LoadResult.Error(Throwable("unexpected error occur"))
        } catch (e: Exception) {
            return Error(e)
        }
    }
}