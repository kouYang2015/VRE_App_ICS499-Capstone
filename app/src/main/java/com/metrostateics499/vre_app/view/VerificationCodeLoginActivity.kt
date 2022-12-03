package com.metrostateics499.vre_app.view

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.metrostateics499.vre_app.R

class VerificationCodeLoginActivity : AppCompatActivity() {

    private lateinit var verificationCode: EditText
    private lateinit var userInputPassword: EditText
    private lateinit var textEmailUsernameHint: TextView
    private lateinit var codeLoginContButton: Button
    private lateinit var invalidCredentials: TextView
    private var adminUsername: String = "username"
    private var adminPassword: String = "password"
    private var emailUsername: String = ""

    /**
     * This overrides the onCreate of this activity
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // This is to hide the action bar
        supportActionBar?.hide()

        setContentView(R.layout.activity_verification_code_login)
    }
}