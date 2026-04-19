package com.example.restaurantfinder

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    val restaurants = mutableListOf<Restaurant>()
    val displayRestaurants = mutableListOf<Restaurant>()
    private lateinit var repository: RestaurantRepository
    private lateinit var searchBox: EditText
    private lateinit var adapter: RestaurantAdapter
    private lateinit var mapView: com.google.android.gms.maps.MapView
    private var googleMap: com.google.android.gms.maps.GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val tabLayout = findViewById<com.google.android.material.tabs.TabLayout>(R.id.tab_layout)
        tabLayout.addOnTabSelectedListener(object : com.google.android.material.tabs.TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: com.google.android.material.tabs.TabLayout.Tab?) {
                if (tab?.position == 1) {
                    startActivity(android.content.Intent(this@MainActivity, MapActivity::class.java))
                    tabLayout.selectTab(tabLayout.getTabAt(0))
                }
            }
            override fun onTabUnselected(tab: com.google.android.material.tabs.TabLayout.Tab?) {}
            override fun onTabReselected(tab: com.google.android.material.tabs.TabLayout.Tab?) {}
        })

        repository = RestaurantRepository(this)

        val recyclerView = findViewById<RecyclerView>(R.id.list_restaurants)
        adapter = RestaurantAdapter(
            restaurants,
            displayRestaurants,
            onEdit = { restaurant -> showEditDialog(restaurant) },
            onDelete = { restaurant ->
                restaurants.remove(restaurant)
                repository.save(restaurants)
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

        mapView = findViewById(R.id.mapView3)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync { map ->
            googleMap = map
            updateMapMarkers()
        }

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

    private fun updateMapMarkers() {
        googleMap?.clear()
        for (restaurant in restaurants) {
            googleMap?.addMarker(
                com.google.android.gms.maps.model.MarkerOptions()
                    .position(com.google.android.gms.maps.model.LatLng(restaurant.lat, restaurant.lng))
                    .title(restaurant.name)
                    .snippet(restaurant.address)
            )
        }
        googleMap?.moveCamera(
            com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom(
                com.google.android.gms.maps.model.LatLng(51.5074, -0.1278), 12f
            )
        )
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
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

    private fun showEditDialog(restaurant: Restaurant) {
        val index = restaurants.indexOf(restaurant)
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
                    repository.update(restaurants, index, Restaurant(name, address))
                    filterRestaurants(searchBox.text.toString())
                } else {
                    Toast.makeText(this, "Name and address cannot be empty.", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }


    }
