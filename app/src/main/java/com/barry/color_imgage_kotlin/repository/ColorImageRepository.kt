package com.barry.color_imgage_kotlin.repository

import com.barry.color_imgage_kotlin.network.APIManager
import com.barry.color_imgage_kotlin.network.APIService
import com.barry.color_imgage_kotlin.pagesource.ColorImageRemoteDataSource

/**
 * Created by Barry Tu on 2024/2/28.
 */
class ColorImageRepository(val pageSize: Int) {

    private val apiService: APIService get() = APIManager.INSTANCE!!.apiService

    fun getColorImageDataSource(): ColorImageRemoteDataSource {
        return ColorImageRemoteDataSource(apiService, pageSize)
    }

}