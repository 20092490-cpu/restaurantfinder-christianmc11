package com.example.restaurantfinder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RestaurantAdapter(
    private val restaurants: MutableList<Restaurant>,
    private val displayRestaurants: MutableList<Restaurant>,
    private val onEdit: (Restaurant) -> Unit,
    private val onDelete: (Restaurant) -> Unit
) : RecyclerView.Adapter<RestaurantAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textName: TextView = view.findViewById(R.id.text_name)
        val textAddress: TextView = view.findViewById(R.id.text_address)
        val buttonEdit: Button = view.findViewById(R.id.button_edit)
        val buttonDelete: Button = view.findViewById(R.id.button_delete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_restaurant, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val restaurant = displayRestaurants[position]
        holder.textName.text = restaurant.name
        holder.textAddress.text = restaurant.address
        holder.buttonEdit.setOnClickListener { onEdit(restaurant) }
        holder.buttonDelete.setOnClickListener { onDelete(restaurant) }
    }

    override fun getItemCount() = displayRestaurants.size
}