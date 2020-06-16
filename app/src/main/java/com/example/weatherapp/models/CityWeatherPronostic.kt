package com.example.weatherapp.models

data class PronosticWeatherModel (
    val lat: Double,
    val lon: Double,
    val timezone: String,
    val timezoneOffset: Long,
    val daily: List<Daily>
)

data class Daily (
    val dt: Long,
    val sunrise: Long,
    val sunset: Long,
    val temp: Temp,
    val feelsLike: FeelsLike,
    val pressure: Long,
    val humidity: Long,
    val dewPoint: Double,
    val windSpeed: Double,
    val windDeg: Long,
    val weather: List<Weather>,
    val clouds: Long,
    val uvi: Double
)

data class FeelsLike (
    val day: Double,
    val night: Double,
    val eve: Double,
    val morn: Double
)

data class Temp (
    val day: Double,
    val min: Double,
    val max: Double,
    val night: Double,
    val eve: Double,
    val morn: Double
)
