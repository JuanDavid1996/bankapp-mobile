package com.example.bankapp.repository.weather

import com.example.bankapp.repository.weather.models.Weather
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("/v1/current.json?")
    suspend fun getWeatherStatus(
        @Query("key") key: String
//        @Query("q") q: String
    ): Response<Weather>
}