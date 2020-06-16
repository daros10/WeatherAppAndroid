package com.example.weatherapp.models

data class WeatherModel (
    val coord: Coord,
    val weather: List<Weather>,
    val main: Main
)

data class Coord (
    val lon: Double,
    val lat: Double
)

data class Main (
    val temp: Double,
    val feelsLike: Double,
    val tempMin: Long,
    val tempMax: Long,
    val pressure: Long,
    val humidity: Long
)

data class Weather (
    val id: Long,
    val main: String,
    val description: String,
    val icon: String
)
