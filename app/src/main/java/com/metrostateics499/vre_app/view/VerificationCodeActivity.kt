package com.metrostateics499.vre_app.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.metrostateics499.vre_app.R

class VerificationCodeActivity : AppCompatActivity() {

    private lateinit var enterCode: EditText
    private lateinit var textEmailUsernameHint: TextView
    private lateinit var enterCodeButton: Button
    private lateinit var invalidCode: TextView
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
        invalidCode = findViewById(R.id.invalid_verification_code)

        codeHint = intent.getStringExtra("genNum").toString()
        username = intent.getStringExtra("email/username").toString() // Use to display username

        textEmailUsernameHint.text =
            username.plus(" ").plus(codeHint) // Change later to send to email

        enterCodeButton.setOnClickListener {
            verifyCode()
        }
    }

    // Verify code
    private fun verifyCode() {
        val inputCode = enterCode.text.toString()
        val emptyEnterCode = "Enter code can not be empty"
        val incorrectEnterCode = "Sorry, code is incorrect. Try again"

        if (enterCode.text.toString().isEmpty()) {
            invalidCode.text = emptyEnterCode
        } else if ((inputCode == adminCode) || (inputCode == codeHint)) {
            Toast.makeText(this, "Continue to Set New Password", Toast.LENGTH_LONG).show()

            val intent =
                Intent(this, SetNewPasswordActivity::class.java)
                    .putExtra("email/username", username)
            startActivity(intent)

            invalidCode.text = ""
            enterCode.text.clear()
        } else {
            invalidCode.text = incorrectEnterCode
        }
    }
}