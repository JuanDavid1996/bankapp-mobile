package com.example.bankapp.repository.weather

import com.example.bankapp.network.models.Result
import com.example.bankapp.network.utils.SafeRequest
import com.example.bankapp.repository.weather.models.Weather

class WeatherRepository {
    private val provider = WeatherProvider()
    suspend fun getWeather(lat: Double, lng: Double): Result<Weather> {
        return SafeRequest.safeRequest {
            val call = provider.getWeather(lat, lng)
            if (call.isSuccessful) {
                return@safeRequest Result.Success(call.body()!!)
            }
            return@safeRequest Result.Error(Exception("No se pudo obtener el estado del tiempo"))
        }
    }
}