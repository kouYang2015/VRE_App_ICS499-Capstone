package com.metrostateics499.vre_app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class ForgotPassword : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // This is to hide the action bar
        supportActionBar?.hide()

        setContentView(R.layout.activity_forgot_password)
    }
}