package com.BlizzardArmory.network

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class RewriteResponseCacheControlInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val maxStale = 60 * 60 * 24 * 5
        val originalResponse = try {
            chain.proceed(chain.request())
        } catch (e: Exception) {
            val responseBuilder = Response.Builder()
            responseBuilder.code(500)
            responseBuilder.message("Timeout")
            responseBuilder.build()
        }


        return originalResponse.newBuilder()
                .header("Cache-Control", "public, max-age=120, max-stale=$maxStale").build()
    }
}