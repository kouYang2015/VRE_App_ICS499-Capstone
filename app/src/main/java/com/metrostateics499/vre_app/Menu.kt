package com.metrostateics499.vre_app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class Menu : AppCompatActivity() {

    private lateinit var speechButton: Button
    private lateinit var keyPhraseButton: Button
    private lateinit var contactButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // This is to hide the action bar
        supportActionBar?.hide()
        setContentView(R.layout.activity_menu)

        speechButton = findViewById(R.id.speechRecognition)
        speechButton?.setOnClickListener() {
            startActivity(Intent(this, ListenSpeechActivity::class.java))
        }
        keyPhraseButton = findViewById(R.id.customKeyWords)
        keyPhraseButton.setOnClickListener {
            val intent = Intent(this, KeyPhraseActivity::class.java)
            // start your next activity
            startActivity(intent)
        }

        contactButton = findViewById(R.id.contactList)
        contactButton.setOnClickListener {
            val intent = Intent(this, CreateContacts::class.java)
            startActivity(intent)
        }
    }
}