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
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    val restaurants = mutableListOf<Restaurant>()
    val displayRestaurants = mutableListOf<Restaurant>()
    private lateinit var repository: RestaurantRepository
    private lateinit var searchBox: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        repository = RestaurantRepository(this)

        val myListView = findViewById<ListView>(R.id.list_restaurants)
        val adapter = RestaurantAdapter()
        myListView.adapter = adapter as android.widget.ListAdapter

        searchBox = findViewById<EditText>(R.id.edit_search)
        searchBox.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: android.text.Editable?) {
                filterRestaurants(s.toString(), adapter)
            }
        })
        myListView.adapter = adapter as android.widget.ListAdapter
        myListView.setOnItemLongClickListener { _, _, position, _ ->
            showEditDialog(position, adapter as RestaurantAdapter)
            true
        }

        loadInitialData(adapter)

        findViewById<Button>(R.id.button_add_favourite).setOnClickListener { showAddDialog(adapter) }

        findViewById<Button>(R.id.button_sort).setOnClickListener {
            restaurants.sortBy { it.name }
            repository.save(restaurants)
            filterRestaurants(searchBox.text.toString(), adapter as RestaurantAdapter)
        }
    }

    private fun loadInitialData(adapter: RestaurantAdapter) {
        val saved = repository.load()
        if (saved.isNotEmpty()) {
            restaurants.addAll(saved)
        }

        if (restaurants.isEmpty()) {
            repository.save(restaurants)
        }
        displayRestaurants.addAll(restaurants)
        adapter.notifyDataSetChanged()
    }

    private fun filterRestaurants(query: String, adapter: RestaurantAdapter) {
        displayRestaurants.clear()
        if (query.isBlank()) {
            displayRestaurants.addAll(restaurants)
        } else {
            for (r in restaurants) {
                if (r.name.lowercase().contains(query.lowercase()) ||
                    r.address.lowercase().contains(query.lowercase())) {
                    displayRestaurants.add(r)
                }
            }
        }
        adapter.notifyDataSetChanged()
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
                if (name.isNotBlank() && address.isNotBlank()) {
                    restaurants.add(Restaurant(name, address))
                    repository.save(restaurants)
                    filterRestaurants(searchBox.text.toString(), adapter as RestaurantAdapter)
                } else {
                    Toast.makeText(this, "Name and address cannot be empty.", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showEditDialog(position: Int, adapter: RestaurantAdapter) {
        val restaurant = restaurants[position]
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_add_restaurant, null)
        val nameInput = view.findViewById<EditText>(R.id.edit_name)
        val addressInput = view.findViewById<EditText>(R.id.edit_address)
        nameInput.setText(restaurant.name)
        addressInput.setText(restaurant.address)

        AlertDialog.Builder(this)
            .setTitle("Edit Restaurant")
            .setView(view)
            .setPositiveButton("Save") { _, _ ->
                val name = nameInput.text.toString()
                val address = addressInput.text.toString()
                if (name.isNotBlank() && address.isNotBlank()) {
                    restaurants[position] = Restaurant(name, address)
                    repository.save(restaurants)
                    filterRestaurants(searchBox.text.toString(), adapter as RestaurantAdapter)
                } else {
                    Toast.makeText(this, "Name and address cannot be empty.", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private inner class RestaurantAdapter : ArrayAdapter<Restaurant>(this, 0, displayRestaurants) {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_restaurant, parent, false)
            val restaurant = getItem(position)!!
            view.findViewById<TextView>(R.id.text_name).text = restaurant.name
            view.findViewById<TextView>(R.id.text_address).text = restaurant.address
            view.findViewById<Button>(R.id.button_edit).setOnClickListener {
                showEditDialog(position, this)
            }
            view.findViewById<Button>(R.id.button_delete).setOnClickListener {
                restaurants.removeAt(position)
                repository.save(restaurants)
                filterRestaurants(searchBox.text.toString(), this)
            }
            return view
        }
    }
}