package com.barry.color_imgage_kotlin.network

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface APIService {
    @GET("photos")
    suspend fun getPhoto(
        @Query("_start") start: String?,
        @Query("_limit") size: String?
    ): Response<List<ColorImgModel>>
}
