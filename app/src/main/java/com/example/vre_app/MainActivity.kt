package com.example.vre_app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val goToKeyPhraseMenu = findViewById<Button>(R.id.goToKeyPhraseMenu)

        goToKeyPhraseMenu.setOnClickListener {
            val intent = Intent(this, KeyWordsActivity::class.java)
            // start your next activity
            startActivity(intent)

        }
    }
}




