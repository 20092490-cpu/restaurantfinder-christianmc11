package com.example.restaurantfinder

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.ArrayAdapter
import android.widget.ListView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val restaurantsList = arrayOf(
            "Kamboat - The Quay",
            "Indian Ocean -456 Street",
            "Pearls - Main Street"
        )

        val myListView = findViewById<ListView>(R.id.list_restaurants)

        val myAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, restaurantsList)

        myListView.adapter = myAdapter
    }
}