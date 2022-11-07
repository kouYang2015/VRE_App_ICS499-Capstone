package com.metrostateics499.vre_app.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.metrostateics499.vre_app.R
import com.metrostateics499.vre_app.model.Passing

class MenuActivity : AppCompatActivity() {

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

        val goToKeyPhraseMenu = findViewById<Button>(R.id.customKeyWords)
        goToKeyPhraseMenu.setOnClickListener {
            val intent = Intent(this, KeyPhraseActivity::class.java)
            // start your next activity
            startActivity(intent)
        }

        val goToCustomTextMessagesMenu = findViewById<Button>(R.id.customTextMessageContent)
        goToCustomTextMessagesMenu.setOnClickListener {
            val intent2 = Intent(this, CustomTextActivity::class.java)
            // start your next activity
            startActivity(intent2)
        }

        val goToEmergencyMessageSetup = findViewById<Button>(R.id.emergencyMessageSetup)
        goToEmergencyMessageSetup.setOnClickListener {
            startActivity(Intent(this, EmergencyMessageSetupActivity::class.java))
        }

        val goToContacts = findViewById<Button>(R.id.contactList)
        goToContacts.setOnClickListener {
            startActivity(Intent(this, ContactActivity::class.java))
        }
    }
    override fun onPostResume() {
        super.onPostResume()
        Passing.selectedEmergencyMessageSetup = null
    }
}