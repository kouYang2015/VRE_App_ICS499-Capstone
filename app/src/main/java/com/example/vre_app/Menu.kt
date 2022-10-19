package com.example.vre_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class Menu : AppCompatActivity() {

    private lateinit var speechButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // This is to hide the action bar
        supportActionBar?.hide()
        setContentView(R.layout.activity_menu)

        speechButton = findViewById(R.id.speechRecognition)
        speechButton?.setOnClickListener() {
            startActivity(Intent(this, ListenSpeechActivity::class.java))
        }
    }
}