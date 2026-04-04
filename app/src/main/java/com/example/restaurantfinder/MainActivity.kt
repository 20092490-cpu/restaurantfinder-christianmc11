package com.example.restaurantfinder

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Button
import android.app.AlertDialog
import android.view.LayoutInflater
import android.widget.EditText

class MainActivity : AppCompatActivity() {

    val restaurants = mutableListOf<Restaurant>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar).let { setSupportActionBar(it) }

        val myListView = findViewById<ListView>(R.id.list_restaurants)
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1)
        myListView.adapter = adapter

        if (restaurants.isEmpty()) {
            restaurants.add(Restaurant("Kamboat", "The Quay"))
            restaurants.add(Restaurant("Indian Ocean", "456 Street"))
            restaurants.add(Restaurant("Pearls", "Main Street"))
        }

        adapter.clear()
        for (r in restaurants) adapter.add(r.name)

        findViewById<Button>(R.id.button_add_favourite).setOnClickListener { showAddDialog(adapter) }
    }

    fun showAddDialog(adapter: ArrayAdapter<String>) {
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
                    adapter.add(name)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}