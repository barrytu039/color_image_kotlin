package com.barry.color_imgage_kotlin.main

import android.app.Application
import android.graphics.drawable.Drawable
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import androidx.savedstate.SavedStateRegistryOwner
import com.barry.color_imgage_kotlin.GlideApp
import com.barry.color_imgage_kotlin.network.ColorImgModel
import com.barry.color_imgage_kotlin.repository.ColorImageRepository
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


/**
 * Created by Barry Tu on 2024/2/28.
 */

class MainViewModelFactory (
    private val owner: SavedStateRegistryOwner,
    private val application: Application
): AbstractSavedStateViewModelFactory(owner, null) {
    override fun <T : ViewModel> create(key: String, modelClass: Class<T>, state: SavedStateHandle) =
        MainViewModel(state, application) as T
}

class MainViewModel(private val savedStateHandle: SavedStateHandle, private val application: Application): ViewModel() {
    private val PAGE_SIZE = 200
    private val colorImageRepository: ColorImageRepository = ColorImageRepository(PAGE_SIZE)

    val colorImageDataFlow: Flow<PagingData<ColorImgModel>> = Pager(PagingConfig(pageSize = PAGE_SIZE, enablePlaceholders = false, prefetchDistance = 400)) {
        colorImageRepository.getColorImageDataSource()
    }.flow.cachedIn(viewModelScope)

}