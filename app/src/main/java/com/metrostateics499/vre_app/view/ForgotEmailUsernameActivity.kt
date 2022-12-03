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

class ForgotEmailUsernameActivity : AppCompatActivity() {

    private lateinit var findEmailUsername: EditText
    private lateinit var findEmailUsernameButton: Button
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

        setContentView(R.layout.activity_forgot_email_username)

        findEmailUsername = findViewById(R.id.forgot_emailUsername_verify_email_username)
        findEmailUsernameButton =
            findViewById(R.id.search_email_username_button)
        invalidEmailUsername = findViewById(R.id.forgot_emailUsername_invalid)

        // This is the verify button
        findEmailUsernameButton.setOnClickListener {
            findAccount()
        }
    }

    /**
     * Search for email or username in the system
     */
    private fun findAccount() {
        val inputEmailUsername = findEmailUsername.text.toString()
        val emptyEmailUsernameInfo = "Email or Username can not be empty"
        val notFoundEmailUsername = "Sorry, Email or Username was not found"

        // Check and display message if Email/Username is empty, is correct, and is incorrect
        if (findEmailUsername.text.isEmpty()) {
            // Display message telling user the name can not be empty
            invalidEmailUsername.text = emptyEmailUsernameInfo
        } else if (((inputEmailUsername == Passing.username || inputEmailUsername == Passing.email)
                    || (inputEmailUsername == adminUsername)
                    )
        ) {
            verificationRandomCode()

            // Display message when successfully verified
            Toast.makeText(this, "Account Found", Toast.LENGTH_LONG)
                .show()
            val intent = Intent(this, VerificationCodeLoginActivity::class.java)
                .putExtra("genNum", generatedNumber)
                .putExtra("email/username", inputEmailUsername)
            startActivity(intent)

            // Clears on screen message
            invalidEmailUsername.text = ""
            // Clears verify username on the screen
            findEmailUsername.text.clear()
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