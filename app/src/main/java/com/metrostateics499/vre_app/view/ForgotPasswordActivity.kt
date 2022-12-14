package com.metrostateics499.vre_app.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.metrostateics499.vre_app.R
import com.metrostateics499.vre_app.model.Passing

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var verifyEmailUsername: EditText
    private lateinit var verifyEmailUsernameButton: Button
    private lateinit var invalidEmailUsername: TextView
    private var adminUsername: String = "username"
    private var generatedNumber: String = ""

    /**
     *  This overrides the onCreate of this activity
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // This is to hide the action bar
        supportActionBar?.hide()

        setContentView(R.layout.activity_forgot_password)

        verifyEmailUsername = findViewById(R.id.verify_email_username)
        verifyEmailUsernameButton = findViewById(R.id.verify_email_username_button)
        invalidEmailUsername = findViewById(R.id.invalid_username)

        // This is the verify button
        verifyEmailUsernameButton.setOnClickListener {
            verifyAccount()
        }
    }

    /**
     * Verifying if username is in the system
     */
    private fun verifyAccount() {
        val inputEmailUsername = verifyEmailUsername.text.toString()
        val emptyEmailUsernameInfo = "Email or Username can not be empty"
        val notFoundEmailUsername = "Sorry, Email or Username was not found"

        // Check and display message if Email/Username is empty, is correct, and is incorrect
        if (verifyEmailUsername.text.isEmpty()) {
            // Display message telling user the name can not be empty
            invalidEmailUsername.text = emptyEmailUsernameInfo
        } else if ((
            (inputEmailUsername == Passing.username || inputEmailUsername == Passing.email) ||
                (inputEmailUsername == adminUsername)
            )
        ) {
            verificationRandomCode()

            // Display message when successfully verified
            Toast.makeText(this, "Account Successfully Verified", Toast.LENGTH_LONG)
                .show()
            val intent = Intent(this, VerificationCodeActivity::class.java)
                .putExtra("genNum", generatedNumber)
                .putExtra("email/username", inputEmailUsername)
            startActivity(intent)

            // Clears on screen message
            invalidEmailUsername.text = ""
            // Clears verify username on the screen
            verifyEmailUsername.text.clear()
        } else {
            // Display message telling user username is not found
            invalidEmailUsername.text = notFoundEmailUsername
        }
    }

    /**
     * Generate a random 6 number total for verification
     */
    private fun verificationRandomCode() {
        val randomNumber = (123457..987654).shuffled().last()

        generatedNumber = randomNumber.toString()
    }
}