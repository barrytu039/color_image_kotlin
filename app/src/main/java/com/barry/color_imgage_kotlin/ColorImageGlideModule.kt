package com.barry.color_imgage_kotlin

import android.content.Context
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.engine.bitmap_recycle.LruArrayPool
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator
import com.bumptech.glide.load.engine.executor.GlideExecutor
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import okhttp3.OkHttpClient
import java.io.InputStream
import java.util.concurrent.TimeUnit


/**
 * Created by Barry Tu on 2024/2/28.
 */
@GlideModule
class ColorImageGlideModule: AppGlideModule()  {
    override fun applyOptions(context: Context, builder: GlideBuilder) {
        val calculator = MemorySizeCalculator.Builder(context)
            .build()
        val bitMapPoolSize = calculator.bitmapPoolSize
        val arrayPoolSize = calculator.arrayPoolSizeInBytes
        val processSize = Runtime.getRuntime().availableProcessors()
        builder.setLogLevel(Log.ERROR)
            .setSourceExecutor(GlideExecutor.newSourceBuilder().setThreadCount((processSize/2)).build())
            .setMemoryCache(LruResourceCache(calculator.memoryCacheSize.toLong()))
            .setDiskCache(InternalCacheDiskCacheFactory(context))
            .setSourceExecutor(GlideExecutor.newSourceExecutor())
            .setDiskCacheExecutor(GlideExecutor.newDiskCacheExecutor())
            .setBitmapPool(LruBitmapPool((bitMapPoolSize/1.5).toLong()))
            .setArrayPool(LruArrayPool((arrayPoolSize/1.5).toInt()))
    }

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        super.registerComponents(context, glide, registry);
        val client: OkHttpClient = OkHttpClient.Builder()
            .readTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
            .build()

        val factory = OkHttpUrlLoader.Factory(client)

        registry.replace(
            GlideUrl::class.java,
            InputStream::class.java,
            factory
        )
    }

    override fun isManifestParsingEnabled(): Boolean {
        return false
    }
}