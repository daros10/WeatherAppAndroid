package com.example.weatherapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.activities.CityWeather
import com.example.weatherapp.adapters.CustomAdapterCities
import com.example.weatherapp.interfaces.ClickListener
import com.example.weatherapp.models.City

class MainActivity : AppCompatActivity() {
    private lateinit var cities: ArrayList<City>
    private lateinit var recyclerView: RecyclerView
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var adapter: CustomAdapterCities

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadCities()
        setPropertiesRecyclerView()
    }

    private fun setPropertiesRecyclerView() {
        recyclerView = findViewById(R.id.recyclerVIewCities)
        layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
        adapter = CustomAdapterCities(cities, object : ClickListener {
            override fun onClick(view: View, position: Int) {
                goToCityWeather(position)
            }
        } )
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
    }

    fun goToCityWeather(position: Int) {
        var cityName: String = cities?.get(position).name
        val intent = Intent(applicationContext, CityWeather::class.java)
        intent.putExtra("name", cityName)
        startActivity(intent)
    }

    private fun loadCities() {
        cities = ArrayList()
        cities.add(City("Quito", R.drawable.quito))
        cities.add(City("Guayaquil", R.drawable.guayaquil))
        cities.add(City("Cuenca", R.drawable.cuenca))
        cities.add(City("Loja", R.drawable.loja))
        cities.add(City("Riobamba", R.drawable.riobamba))
        cities.add(City("Galápagos", R.drawable.galapagos))
        cities.add(City("Manta", R.drawable.manta))
        cities.add(City("Salinas", R.drawable.salinas))
        cities.add(City("Ambato", R.drawable.ambato))
        cities.add(City("Guaranda", R.drawable.guaranda))
    }
}