package com.example.weatherapp.interfaces

import com.example.weatherapp.models.PronosticDaysAgoWeatherModel
import com.example.weatherapp.models.PronosticWeatherModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PronosticWeather {
    @GET("onecall")
    fun getSevenDaysAfter(@Query("lat") lat: Double,
                          @Query("lon") lon: Double,
                          @Query("exclude") exclude: String = "minutely,hourly,current",
                          @Query("appid") appid: String = "bf79c6246cd574b350bc52b872c68605",
                          @Query("units") units: String = "metric") : Call<PronosticWeatherModel>

    @GET("onecall/timemachine")
    fun getFiveDaysBefore(@Query("lat") lat: Double,
                          @Query("lon") lon: Double,
                          @Query("dt") dt: String,
                          @Query("appid") appid: String = "bf79c6246cd574b350bc52b872c68605",
                          @Query("units") units: String = "metric") : Call<PronosticDaysAgoWeatherModel>
}
