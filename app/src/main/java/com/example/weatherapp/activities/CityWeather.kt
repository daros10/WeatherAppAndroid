package com.example.weatherapp.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.weatherapp.R
import com.example.weatherapp.adapters.CustomAdapterPronostic
import com.example.weatherapp.interfaces.ClickListener
import com.example.weatherapp.interfaces.CurrentWeatherApi
import com.example.weatherapp.interfaces.PronosticWeather
import com.example.weatherapp.models.CityPronostic
import com.example.weatherapp.models.PronosticDaysAgoWeatherModel
import com.example.weatherapp.models.PronosticWeatherModel
import com.example.weatherapp.models.WeatherModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_city_weather.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*
import kotlin.collections.ArrayList

class CityWeather : AppCompatActivity() {

    private lateinit var retrofit: Retrofit
    private lateinit var cityName: String
    private val currentDateTime: LocalDateTime = LocalDateTime.now()
    private lateinit var recyclerView: RecyclerView
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var adapter: CustomAdapterPronostic
    private var viewFiveDaysAgo: Boolean = false

    private val baseUrl: String = "https://api.openweathermap.org/data/2.5/"
    private val baserUrlIcon: String = "https://openweathermap.org/img/wn/"
    private val baseUrlPronosticWeather: String = "https://api.openweathermap.org/data/2.5/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city_weather)
        val intent: Intent = intent
        cityName = intent.getStringExtra("name")
        toolbarCity?.title = cityName
        getDataFromApiForLoadFirstContainer()
    }

    private fun getDataFromApiForLoadFirstContainer(){
        retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        var currentWeather: CurrentWeatherApi = retrofit.create(CurrentWeatherApi::class.java)
        var call: Call<WeatherModel> = currentWeather.getWeatherData(cityName)

        call.enqueue(object : Callback<WeatherModel> {
            override fun onFailure(call: Call<WeatherModel>, t: Throwable) {
                Toast.makeText(applicationContext, "Failure: ${t.message}", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<WeatherModel>, response: Response<WeatherModel>) {
                if (!response.isSuccessful) {
                    Toast.makeText(applicationContext, "Service Error Code: ${response.code()}", Toast.LENGTH_SHORT).show()
                    return
                }

                progressBarMain.visibility = View.GONE
                var responseData: WeatherModel = response.body()!!
                var cityTemperature = "${responseData.main.temp} °C"
                var iconTemperature = "${responseData.weather[0].icon}"

                getPronosticSevenDays(responseData.coord.lon, responseData.coord.lat)
                floatingActionButton.setOnClickListener{
                    viewFiveDaysAgo = !viewFiveDaysAgo
                    if ( viewFiveDaysAgo ) {
                        getPronosticFiveDays(responseData.coord.lon, responseData.coord.lat)
                        Snackbar.make(it, "Five days ago", Snackbar.LENGTH_LONG).show()

                    } else {
                        getPronosticSevenDays(responseData.coord.lon, responseData.coord.lat)
                        Snackbar.make(it, "Seven days later", Snackbar.LENGTH_LONG).show()
                    }
                }

                txtDate.text = currentDateTime.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
                txtTemperature.text = cityTemperature
                Glide.with(applicationContext).load("$baserUrlIcon$iconTemperature@4x.png").circleCrop().transition(
                    DrawableTransitionOptions.withCrossFade(250)).into(imageViewWeather)
            }
        })
    }

    private fun getPronosticSevenDays(lon: Double, lat: Double ){
        retrofit = Retrofit.Builder()
            .baseUrl(baseUrlPronosticWeather)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        var pronosticSevenDays: PronosticWeather = retrofit.create(PronosticWeather::class.java)
        var call: Call<PronosticWeatherModel> = pronosticSevenDays.getSevenDaysAfter(lat, lon)

        call.enqueue(object : Callback<PronosticWeatherModel> {
            override fun onFailure(call: Call<PronosticWeatherModel>, t: Throwable) {
                Toast.makeText(applicationContext, "Failure: ${t.message}", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<PronosticWeatherModel>, response: Response<PronosticWeatherModel>) {
                if (!response.isSuccessful) {
                    Toast.makeText(applicationContext, "Service Error Code: ${response.code()}", Toast.LENGTH_SHORT).show()
                    return
                }

                progressBarSeconday.visibility = View.GONE
                val responseData: PronosticWeatherModel = response.body()!!
                val cities: ArrayList<CityPronostic> = ArrayList()
                val simpleDateFormat = java.text.SimpleDateFormat("MMM dd")

                for ( item in responseData.daily  ) {
                    val date = java.util.Date(item.dt * 1000)
                    cities.add(CityPronostic(simpleDateFormat.format(date),  "$baserUrlIcon${item.weather[0].icon}@4x.png"  , " ${item.temp.day} °C","Min: "+item.temp.min.toString()+" °C", "Max: "+item.temp.max.toString()+" °C", item.weather[0].description ))
                }
                cities.removeAt(0)

                recyclerView = findViewById(R.id.reciclerVIewPronostic)
                layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
                adapter = CustomAdapterPronostic(cities, object : ClickListener {
                    override fun onClick(view: View, position: Int) {
                    }
                }, applicationContext)
                recyclerView.layoutManager = layoutManager
                recyclerView.adapter = adapter
            }
        })
    }

    private fun getPronosticFiveDays(lon: Double, lat: Double ){
        retrofit = Retrofit.Builder()
            .baseUrl(baseUrlPronosticWeather)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        var simpleDateFormat = SimpleDateFormat("yyyy MM dd")
        val dayAgo1 = SimpleDateFormat("yyyy MM dd").parse("${simpleDateFormat.format(getDaysAgo(1))}")
        val dayAgo2 = SimpleDateFormat("yyyy MM dd").parse("${simpleDateFormat.format(getDaysAgo(2))}")
        val dayAgo3 = SimpleDateFormat("yyyy MM dd").parse("${simpleDateFormat.format(getDaysAgo(3))}")
        val dayAgo4 = SimpleDateFormat("yyyy MM dd").parse("${simpleDateFormat.format(getDaysAgo(4))}")
        val dayAgo5 = SimpleDateFormat("yyyy MM dd").parse("${simpleDateFormat.format(getDaysAgo(5))}")

        var daysAgo: ArrayList<String> = ArrayList();
        daysAgo.add("${dayAgo1.time / 1000}")
        daysAgo.add("${dayAgo2.time / 1000}")
        daysAgo.add("${dayAgo3.time / 1000}")
        daysAgo.add("${dayAgo4.time / 1000}")
        daysAgo.add("${dayAgo5.time / 1000}")

        var pronosticSevenDays: PronosticWeather = retrofit.create(PronosticWeather::class.java)
        var dataDaysAgo: ArrayList<CityPronostic> = ArrayList()

        for ( item in daysAgo ) {
            var call: Call<PronosticDaysAgoWeatherModel> = pronosticSevenDays.getFiveDaysBefore(lat, lon, item)

            call.enqueue(object : Callback<PronosticDaysAgoWeatherModel> {
                override fun onFailure(call: Call<PronosticDaysAgoWeatherModel>, t: Throwable) {
                    Toast.makeText(applicationContext, "Failure: ${t.message}", Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<PronosticDaysAgoWeatherModel>, response: Response<PronosticDaysAgoWeatherModel>) {
                    if (!response.isSuccessful) {
                        Toast.makeText(applicationContext, "Service Error Code: ${response.code()}", Toast.LENGTH_SHORT).show()
                        return
                    }

                    progressBarSeconday.visibility = View.GONE
                    val responseData: PronosticDaysAgoWeatherModel = response.body()!!
                    val simpleDateFormat = java.text.SimpleDateFormat("MMM dd")

                    for ( item in responseData.current.weather  ) {
                        val date = java.util.Date(responseData.current.dt * 1000)
                        dataDaysAgo.add(CityPronostic(simpleDateFormat.format(date),  "$baserUrlIcon${item.icon}@4x.png" , " ${responseData.current.temp} °C", item.main , item.description, "" ))
                    }

                    if ( dataDaysAgo.size == 5 ) {
                        recyclerView = findViewById(R.id.reciclerVIewPronostic)
                        layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
                        adapter = CustomAdapterPronostic(dataDaysAgo, object : ClickListener {
                            override fun onClick(view: View, position: Int) {
                            }
                        }, applicationContext)
                        recyclerView.layoutManager = layoutManager
                        recyclerView.adapter = adapter

                    }
                }
            })
        }
    }

    private fun getDaysAgo(daysAgo: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -daysAgo)
        return calendar.time
    }
}