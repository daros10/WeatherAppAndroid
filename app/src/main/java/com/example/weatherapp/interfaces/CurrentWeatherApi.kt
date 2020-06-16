package com.example.weatherapp.interfaces

import com.example.weatherapp.models.WeatherModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrentWeatherApi {
    @GET("weather")
    fun getWeatherData(@Query("q") q: String, @Query("appid") appid: String = "bf79c6246cd574b350bc52b872c68605", @Query("units") units: String = "metric") : Call<WeatherModel>
}