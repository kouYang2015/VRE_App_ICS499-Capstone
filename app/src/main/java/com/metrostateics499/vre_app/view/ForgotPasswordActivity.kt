package com.metrostateics499.vre_app.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.metrostateics499.vre_app.R

class ForgotPasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // This is to hide the action bar
        supportActionBar?.hide()

        setContentView(R.layout.activity_forgot_password)
    }
}