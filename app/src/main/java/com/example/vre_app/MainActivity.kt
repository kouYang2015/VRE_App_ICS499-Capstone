package com.example.vre_app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button = findViewById<Button>(R.id.openSpeechListener)

        // operations to be performed when user tap on the button
        button?.setOnClickListener() {
            val intent = Intent(this, ListenSpeechActivity::class.java)
            startActivity(intent)
        }

    }
}