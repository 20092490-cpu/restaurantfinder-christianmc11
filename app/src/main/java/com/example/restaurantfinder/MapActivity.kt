package com.example.restaurantfinder

import android.os.Bundle
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

        findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_map).let {
            setSupportActionBar(
                it
            )
        }
        supportActionBar?.title = "Map"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        repository = RestaurantRepository(this)
        restaurants = repository.load()

        mapView = findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        val searchBox = findViewById<EditText>(R.id.edit_map_search)
        searchBox.setOnEditorActionListener { _, _, _ ->
            val query = searchBox.text.toString()
            if (query.isNotBlank()) {
                val geocoder = android.location.Geocoder(this)
                val results = geocoder.getFromLocationName(query, 1)
                if (results != null && results.isNotEmpty()) {
                    val location = results[0]
                    googleMap?.moveCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            LatLng(location.latitude, location.longitude), 12f
                        )
                    )
                }
            }
            true
        }
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        updateMarkers(restaurants)
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