package com.example.restaurantfinder

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RestaurantRepository(private val context: Context) {

    private val prefs = context.getSharedPreferences("restaurants", Context.MODE_PRIVATE)
    private val gson = Gson()
    private val key = "list"

    fun save(list: MutableList<Restaurant>) {
        val json = gson.toJson(list)
        prefs.edit().putString(key, json).apply()
    }

    fun load(): MutableList<Restaurant> {
        val json = prefs.getString(key, null) ?: return mutableListOf()
        val type = object : TypeToken<MutableList<Restaurant>>() {}.type
        return gson.fromJson(json, type)
    }
}