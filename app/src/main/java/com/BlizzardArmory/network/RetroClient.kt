package com.BlizzardArmory.network

import com.BlizzardArmory.BlizzardArmory
import com.google.gson.GsonBuilder
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit


object RetroClient {
    var BASE_URL: String = URLConstants.HEROKU_AUTHENTICATE
    fun getClient(): NetworkServices {
        val gson = GsonBuilder().create()

        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS)
        val client = OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .cache(Cache(
                        directory = File(BlizzardArmory.instance?.cacheDir, "http_cache"),
                        maxSize = 60 * 1024 * 1024
                ))
                .addInterceptor(RewriteRequestInterceptor())
                .addNetworkInterceptor(RewriteResponseCacheControlInterceptor())
                .addInterceptor(interceptor)
                .build()

        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()

        return retrofit.create(NetworkServices::class.java)
    }
}