package com.example.restaurantfinder

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Button
import android.app.AlertDialog
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.TextView
import android.view.ViewGroup
import android.view.View

class MainActivity : AppCompatActivity() {

    val restaurants = mutableListOf<Restaurant>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)

        val myListView = findViewById<ListView>(R.id.list_restaurants)
        val adapter = RestaurantAdapter()
        myListView.adapter = adapter as android.widget.ListAdapter

        loadInitialData(adapter)

        findViewById<Button>(R.id.button_add_favourite).setOnClickListener { showAddDialog(adapter) }
    }

    private fun loadInitialData(adapter: RestaurantAdapter) {
        if (restaurants.isEmpty()) {
            restaurants.add(Restaurant("Kamboat", "The Quay"))
            restaurants.add(Restaurant("Indian Ocean", "456 Street"))
            restaurants.add(Restaurant("Pearls", "Main Street"))
        }
        adapter.clear()
        adapter.notifyDataSetChanged()
        adapter.addAll(restaurants)
    }

    private fun showAddDialog(adapter: RestaurantAdapter) {
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_add_restaurant, null)
        val nameInput = view.findViewById<EditText>(R.id.edit_name)
        val addressInput = view.findViewById<EditText>(R.id.edit_address)

        AlertDialog.Builder(this)
            .setTitle("Add Favourite")
            .setView(view)
            .setPositiveButton("Add") { _, _ ->
                val name = nameInput.text.toString()
                val address = addressInput.text.toString()
                if (name != "" && address != "") {
                    restaurants.add(Restaurant(name, address))
                    adapter.notifyDataSetChanged()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private inner class RestaurantAdapter : ArrayAdapter<Restaurant>(this, 0, restaurants) {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = convertView ?: LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_2, parent, false)
            val restaurant = getItem(position)!!
            view.findViewById<TextView>(android.R.id.text1).text = restaurant.name
            view.findViewById<TextView>(android.R.id.text2).text = restaurant.address
            return view
        }
    }
}