package com.example.bankapp.repository.weather.models

import com.google.gson.annotations.SerializedName


class Weather(
    @SerializedName("location") val location: WeatherLocation,
    @SerializedName("current") val current: Current
)

data class WeatherLocation(
    @SerializedName("name") val name: String,
    @SerializedName("region") val region: String,
    @SerializedName("country") val country: String,
)

data class Condition(
    @SerializedName("text") val text: String,
)

data class Current(
    @SerializedName("temp_c") val tempC: Double,
    @SerializedName("condition") val condition: Condition,
)
