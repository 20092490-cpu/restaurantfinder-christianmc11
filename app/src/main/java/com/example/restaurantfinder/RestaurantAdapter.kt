package com.example.restaurantfinder

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView

class RestaurantAdapter(
    context: Context,
    private val restaurants: MutableList<Restaurant>,
    private val displayRestaurants: MutableList<Restaurant>,
    private val onEdit: (Int) -> Unit,
    private val onDelete: (Int) -> Unit
) : ArrayAdapter<Restaurant>(context, 0, displayRestaurants) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_restaurant, parent, false)
        val restaurant = getItem(position)!!
        view.findViewById<TextView>(R.id.text_name).text = restaurant.name
        view.findViewById<TextView>(R.id.text_address).text = restaurant.address
        view.findViewById<Button>(R.id.button_edit).setOnClickListener {
            onEdit(position)
        }
        view.findViewById<Button>(R.id.button_delete).setOnClickListener {
            onDelete(position)
        }
        return view
    }
}