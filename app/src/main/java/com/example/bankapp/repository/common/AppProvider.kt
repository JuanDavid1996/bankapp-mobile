package com.example.bankapp.repository.common

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

open class AppProvider {
    companion object {
        var retrofit: Retrofit? = null
        const val BASE_URL = "http://164.90.158.112:3000/"
    }

    fun getRetrofit(): Retrofit {
        if (retrofit == null) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder().apply {
                readTimeout(20, TimeUnit.SECONDS)
                writeTimeout(20, TimeUnit.SECONDS)
                connectTimeout(20, TimeUnit.SECONDS)
                addInterceptor(interceptor)
                addInterceptor { chain ->
                    var request = chain.request()
                    request = request.newBuilder()
                        .build()
                    val response = chain.proceed(request)
                    response
                }
            }
            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client.build())

                .addConverterFactory(GsonConverterFactory.create())
                .build()

        }

        return retrofit!!
    }
}