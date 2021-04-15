package com.example.footballfanapp

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.decode.SvgDecoder
import coil.util.CoilUtils
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.OkHttpClient

@HiltAndroidApp
class MyApplication: Application(), ImageLoaderFactory {

    override fun newImageLoader(): ImageLoader {

        return ImageLoader.Builder(applicationContext)
            .crossfade(true)
            .componentRegistry {
                add(SvgDecoder(applicationContext))
            }
            .okHttpClient {
                OkHttpClient.Builder()
                    .cache(CoilUtils.createDefaultCache(applicationContext))
                    .build()
            }
            .build()
    }
}