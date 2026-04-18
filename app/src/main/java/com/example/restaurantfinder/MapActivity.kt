package com.example.restaurantfinder

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mapView: MapView
    private var googleMap: GoogleMap? = null
    private lateinit var repository: RestaurantRepository
    private var restaurants = mutableListOf<Restaurant>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_map).let { setSupportActionBar(it) }
        supportActionBar?.title = "Map"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        repository = RestaurantRepository(this)
        restaurants = repository.load()

        mapView = findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        val searchBox = findViewById<EditText>(R.id.edit_map_search)
        searchBox.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                filterMarkers(s.toString())
            }
        })
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        updateMarkers(restaurants)
    }

    private fun filterMarkers(query: String) {
        val filtered = if (query.isBlank()) {
            restaurants
        } else {
            restaurants.filter {
                it.name.lowercase().contains(query.lowercase()) ||
                        it.address.lowercase().contains(query.lowercase())
            }
        }
        updateMarkers(filtered)
    }

    private fun updateMarkers(list: List<Restaurant>) {
        googleMap?.clear()
        for (restaurant in list) {
            googleMap?.addMarker(
                MarkerOptions()
                    .position(LatLng(restaurant.lat, restaurant.lng))
                    .title(restaurant.name)
                    .snippet(restaurant.address)
            )
        }
        if (list.isNotEmpty()) {
            googleMap?.moveCamera(
                CameraUpdateFactory.newLatLngZoom(
                    LatLng(list[0].lat, list[0].lng), 12f
                )
            )
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
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
}