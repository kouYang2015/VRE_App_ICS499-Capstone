package com.metrostateics499.vre_app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class VerificationCode : AppCompatActivity() {

    private lateinit var enterCode: EditText
    private lateinit var textEmailUsernameHint: TextView
    private lateinit var enterCodeButton: Button
    private var adminCode: String = "123456"
    private var codeHint: String = ""
    private var username: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // This is to hide the action bar
        supportActionBar?.hide()

        setContentView(R.layout.activity_verification_code)

        enterCode = findViewById(R.id.enter_code)
        textEmailUsernameHint = findViewById(R.id.verify_email_username_display)
        enterCodeButton = findViewById(R.id.verify_code_button)

        codeHint = intent.getStringExtra("genNum").toString()
        username = intent.getStringExtra("email/username").toString() // Use to display username

        textEmailUsernameHint.text =
            username.plus(" ").plus(codeHint) // Change later to send to email

        enterCodeButton.setOnClickListener {
            verifyCode()
        }
    }

    private fun verifyCode() {
        val inputCode = enterCode.text.toString()

        if (enterCode.text.toString().isEmpty()) {
            Toast.makeText(
                this,
                "Enter code can not be empty.",
                Toast.LENGTH_SHORT
            ).show()
        } else if ((inputCode == adminCode) || (inputCode == codeHint)) {
            Toast.makeText(this, "Continue to Set New Password", Toast.LENGTH_LONG).show()

            val intent =
                Intent(this, SetNewPassword::class.java)
                    .putExtra("email/username", username)
            startActivity(intent)
        } else {
            Toast.makeText(
                this,
                "Sorry, Code is Incorrect. Try again.",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}