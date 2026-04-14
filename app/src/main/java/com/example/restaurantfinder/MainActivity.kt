package com.example.restaurantfinder

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager
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
    private lateinit var adapter: RestaurantAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        repository = RestaurantRepository(this)

        val recyclerView = findViewById<RecyclerView>(R.id.list_restaurants)
        adapter = RestaurantAdapter(
            restaurants,
            displayRestaurants,
            onEdit = { position -> showEditDialog(position) },
            onDelete = { position ->
                repository.delete(restaurants, position)
                filterRestaurants(searchBox.text.toString())
            }
        )
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        searchBox = findViewById<EditText>(R.id.edit_search)
        searchBox.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: android.text.Editable?) {
                filterRestaurants(s.toString())
            }
        })

        loadInitialData()

        findViewById<Button>(R.id.button_add_favourite).setOnClickListener { showAddDialog(adapter) }

        findViewById<Button>(R.id.button_sort).setOnClickListener {
            restaurants.sortBy { it.name }
            repository.save(restaurants)
            filterRestaurants(searchBox.text.toString())
        }
    }

    private fun loadInitialData() {
        val saved = repository.load()
        if (saved.isNotEmpty()) {
            restaurants.addAll(saved)
        }

        if (restaurants.isEmpty()) {
            repository.save(restaurants)
        }
        filterRestaurants("")
    }

    private fun filterRestaurants(query: String) {
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
                    repository.add(restaurants, Restaurant(name, address))
                    filterRestaurants(searchBox.text.toString())
                } else {
                    Toast.makeText(this, "Name and address cannot be empty.", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showEditDialog(position: Int) {
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
                    repository.update(restaurants, position, Restaurant(name, address))
                    filterRestaurants(searchBox.text.toString())
                } else {
                    Toast.makeText(this, "Name and address cannot be empty.", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }


    }
