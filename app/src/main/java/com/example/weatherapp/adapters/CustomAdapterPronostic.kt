package com.example.weatherapp.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.weatherapp.R
import com.example.weatherapp.interfaces.ClickListener
import com.example.weatherapp.models.CityPronostic
import java.util.*
import kotlin.collections.ArrayList

class CustomAdapterPronostic(items: ArrayList<CityPronostic>, var clickListener: ClickListener, val context: Context): RecyclerView.Adapter<CustomAdapterPronostic.ViewHolder>() {
    var cities: ArrayList<CityPronostic>? = null
    var viewHolder: ViewHolder? = null

    init {
        this.cities = items
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.template_recyclerview_pronostic, parent, false)
        viewHolder = ViewHolder(view, clickListener)
        return viewHolder!!
    }

    override fun getItemCount(): Int {
        return this.cities?.count()!!
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = cities?.get(position)
        holder.txtDatePronostic.text = item?.date
        holder.txtTemperaturePronostic.text = item?.temperture
        Glide.with(context).load(item?.image!!).circleCrop().transition(
            DrawableTransitionOptions.withCrossFade(250)).into(holder.imageCity)
        holder.txtMinTempPron.text = item.min
        holder.txtMaxTempPron.text = item.max
        holder.txtDesc.text = item.description
        val rnd = Random()
        var currentColor = Color.argb(89, rnd.nextInt(160), rnd.nextInt(160), rnd.nextInt(255))
        holder.cardView.setCardBackgroundColor(currentColor)
    }

    class ViewHolder(view: View, listener: ClickListener): RecyclerView.ViewHolder(view), View.OnClickListener {
        var imageCity: ImageView = view.findViewById(R.id.imageViewPronostic)
        var txtDatePronostic: TextView = view.findViewById(R.id.txtDatePronostic)
        var txtTemperaturePronostic: TextView = view.findViewById(R.id.txtTemperaturePronostic)
        var txtMinTempPron: TextView = view.findViewById(R.id.txtMin)
        var txtMaxTempPron: TextView = view.findViewById(R.id.txtMax)
        var txtDesc: TextView = view.findViewById(R.id.txtDescription)
        var cardView: CardView = view.findViewById(R.id.cardViewPronostic)
        var listener: ClickListener = listener
        init {
            view.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            listener.onClick(v!!, adapterPosition)
        }
    }
}