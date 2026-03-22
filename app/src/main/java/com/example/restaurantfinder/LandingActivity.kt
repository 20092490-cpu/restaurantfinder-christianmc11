package com.example.restaurantfinder

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.view.View

class LandingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_landing)

        val myButton = findViewById<Button>(R.id.button_go_home)

        myButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View?) {

                val myIntent = Intent(this@LandingActivity, MainActivity::class.java)

                startActivity(myIntent)
            }
        })
    }
}