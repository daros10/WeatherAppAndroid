package com.example.weatherapp.models


data class CityPronostic(
    val date:String,
    val image: String,
    val temperture: String,
    val min: String,
    val max: String,
    val description: String
)