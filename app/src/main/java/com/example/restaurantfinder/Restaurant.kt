package com.example.restaurantfinder

data class Restaurant(
    val name: String,
    val address: String,
    val lat: Double = 51.5074,
    val lng: Double = -0.1278
)