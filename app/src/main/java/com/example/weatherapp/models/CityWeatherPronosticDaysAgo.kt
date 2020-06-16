package com.example.weatherapp.models

data class PronosticDaysAgoWeatherModel (
    val lat: Double,
    val lon: Double,
    val timezone: String,
    val timezoneOffset: Long,
    val current: Current,
    val hourly: List<Current>
)

data class Current (
    val dt: Long,
    val sunrise: Long? = null,
    val sunset: Long? = null,
    val temp: Double,
    val feelsLike: Double,
    val pressure: Long,
    val humidity: Long,
    val dewPoint: Double,
    val uvi: Double? = null,
    val clouds: Long,
    val windSpeed: Double,
    val windDeg: Long,
    val weather: List<Weather>
)
