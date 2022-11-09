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

    // MIGHT USE THIS, NOT SURE YET
    // private lateinit var verifyEmail: EditText   // Implement in milestone 4
    private lateinit var verifyUsername: EditText
    private lateinit var verifyEmailUsernameButton: Button
    private lateinit var invalidUsername: TextView
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

        // verifyEmail = findViewById(R.id.verify_email_username)  // Implement in milestone 4
        verifyUsername = findViewById(R.id.verify_email_username)
        verifyEmailUsernameButton = findViewById(R.id.verify_email_username_button)
        invalidUsername = findViewById(R.id.invalid_username)

        // This is the verify button
        verifyEmailUsernameButton.setOnClickListener {
            verifyAccount()
        }
    }

    /**
     * Verifying if username is in the system
     */
    private fun verifyAccount() {
        val inputUsername = verifyUsername.text.toString()
        val emptyUsernameInfo = "Username can not be empty"
        val notFoundUsername = "Sorry, Username was not found"

        // val inputEmail = verifyEmail.text.toString() // Implement in milestone 4

        // Check and display message if Email/Username is empty, is correct, and is incorrect
        if (verifyUsername.text.isEmpty()) {
            // Display message telling user the name can not be empty
            invalidUsername.text = emptyUsernameInfo
        } else if (((inputUsername == Passing.username) || (inputUsername == adminUsername))) {
            verificationRandomCode()

            // Display message when successfully verified
            Toast.makeText(this, "Account Successfully Verified", Toast.LENGTH_LONG).show()
            val intent = Intent(this, VerificationCodeActivity::class.java)
                .putExtra("genNum", generatedNumber)
                .putExtra("email/username", inputUsername)
            startActivity(intent)

            // Clears on screen message
            invalidUsername.text = ""
            // Clears verify username on the screen
            verifyUsername.text.clear()
        } else {
            // Display message telling user username is not found
            invalidUsername.text = notFoundUsername
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