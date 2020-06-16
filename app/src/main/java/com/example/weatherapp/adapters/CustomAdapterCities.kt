package com.example.weatherapp.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.interfaces.ClickListener
import com.example.weatherapp.models.City
import java.util.*
import kotlin.collections.ArrayList

class CustomAdapterCities(items: ArrayList<City>, var clickListener: ClickListener): RecyclerView.Adapter<CustomAdapterCities.ViewHolder>() {
    var cities: ArrayList<City>? = null
    var viewHolder: ViewHolder? = null

    init {
        this.cities = items
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.template_recyclerview_cities, parent, false)
        viewHolder = ViewHolder(view, clickListener)
        return viewHolder!!
    }

    override fun getItemCount(): Int {
       return this.cities?.count()!!
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = cities?.get(position)
        holder.txtCity.text = item?.name
        holder.imageCity.setImageResource(item?.image!!)
    }

    class ViewHolder(view: View, listener: ClickListener): RecyclerView.ViewHolder(view), View.OnClickListener {
        var imageCity: ImageView = view.findViewById(R.id.imageViewCity)
        var txtCity: TextView = view.findViewById(R.id.txtCity)

        var listener: ClickListener = listener
        init {
            view.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            listener.onClick(v!!, adapterPosition)
        }
    }
}