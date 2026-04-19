package com.example.restaurantfinder

import org.junit.Test
import org.junit.Assert.*

class RestaurantTest {

    @Test
    fun restaurant_hasCorrectName() {
        val restaurant = Restaurant("Kamboat", "The Quay")
        assertEquals("Kamboat", restaurant.name)
    }

    @Test
    fun restaurant_hasCorrectAddress() {
        val restaurant = Restaurant("Kamboat", "The Quay")
        assertEquals("The Quay", restaurant.address)
    }

    @Test
    fun restaurant_hasDefaultCoordinates() {
        val restaurant = Restaurant("Kamboat", "The Quay")
        assertEquals(51.5074, restaurant.lat, 0.0001)
        assertEquals(-0.1278, restaurant.lng, 0.0001)
    }

    @Test
    fun restaurant_listAdd() {
        val list = mutableListOf<Restaurant>()
        list.add(Restaurant("Kamboat", "The Quay"))
        assertEquals(1, list.size)
    }

    @Test
    fun restaurant_listDelete() {
        val list = mutableListOf<Restaurant>()
        val restaurant = Restaurant("Kamboat", "The Quay")
        list.add(restaurant)
        list.remove(restaurant)
        assertEquals(0, list.size)
    }

    @Test
    fun restaurant_listEdit() {
        val list = mutableListOf<Restaurant>()
        list.add(Restaurant("AA", "AA"))
        list[0] = Restaurant("New Name", "New Address")
        assertEquals("New Name", list[0].name)
    }

    @Test
    fun restaurant_searchFilter() {
        val list = mutableListOf(
            Restaurant("AA", "AA")
        )
        val filtered = list.filter { it.name.lowercase().contains("kam") }
        assertEquals(1, filtered.size)
        assertEquals("Kamboat", filtered[0].name)
    }

    @Test
    fun restaurant_sortAZ() {
        val list = mutableListOf(
            Restaurant("AA", "AA")
        )
        list.sortBy { it.name }
        assertEquals("AA", list[0].name)
    }
}