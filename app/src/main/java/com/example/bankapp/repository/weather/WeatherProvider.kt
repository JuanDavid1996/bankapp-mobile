package com.example.bankapp.repository.weather

import com.example.bankapp.repository.weather.models.Weather
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class WeatherProvider {
    suspend fun getWeather(lat: Double, lng: Double): Response<Weather> {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().apply {
            readTimeout(20, TimeUnit.SECONDS)
            writeTimeout(20, TimeUnit.SECONDS)
            connectTimeout(20, TimeUnit.SECONDS)
            addInterceptor(interceptor)
            addInterceptor { chain ->
                var request = chain.request()
                val httpOriginal = request.url
                val url = httpOriginal.newBuilder()
                    .addQueryParameter("q", "$lat,$lng").build()

                request = request.newBuilder()
                    .url(url)
                    .build()
                val response = chain.proceed(request)
                response
            }
        }

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.weatherapi.com")
            .client(client.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        //TODO: REMOVE THIS KEY FROM HERE
        val key = "33e600018d424605aa325537210609"
        return retrofit.create(WeatherService::class.java).getWeatherStatus(key)
    }
}