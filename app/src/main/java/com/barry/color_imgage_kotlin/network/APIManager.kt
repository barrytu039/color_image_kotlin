package com.barry.color_imgage_kotlin.network

import okhttp3.OkHttpClient

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class APIManager private constructor() {
    private val endPoint: EndPoint

    private var retrofit: Retrofit? = null
    private var okHttpClient: OkHttpClient? = null
        private set
    val apiService: APIService

    // private constructor
    init {
        // init endpoint
        endPoint = EndPoint()
        // init okhttp client
        initOkhttp()
        // init retrofit
        initRetrofit()
        // create APIService
        apiService = retrofit!!.create(APIService::class.java)
    }

    // create okhttp client
    private fun initOkhttp() {
        okHttpClient = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .build()
    }

    // create retrofit
    private fun initRetrofit() {
        retrofit = Retrofit.Builder()
            .baseUrl(endPoint.baseurl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    companion object {
        @get:Synchronized
        var INSTANCE: APIManager? = null
            // singleton get func
            get() {
                if (field == null) {
                    field = APIManager()
                }
                return field
            }
            private set
    }
}
