package com.metrostateics499.vre_app.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.metrostateics499.vre_app.R
import com.metrostateics499.vre_app.model.Passing

class ForgotPasswordActivity : AppCompatActivity() {

    // DELETE AFTER DONE
    // private lateinit var verifyEmailUsername: EditText

    // MIGHT USE THIS, NOT SURE YET
    // private lateinit var verifyEmail: EditText   // Implement in milestone 4
    private lateinit var verifyUsername: EditText
    private lateinit var verifyEmailUsernameButton: Button
    private var adminUsername: String = "username"
    private var generatedNumber: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // This is to hide the action bar
        supportActionBar?.hide()

        setContentView(R.layout.activity_forgot_password)

        //verifyEmail = findViewById(R.id.verify_email_username)  // Implement in milestone 4
        verifyUsername = findViewById(R.id.verify_email_username)
        verifyEmailUsernameButton = findViewById(R.id.verify_email_username_button)

        verifyEmailUsernameButton.setOnClickListener {
            verifyAccount()
        }
    }

    // Verifying if email or username is in the system
    // This is only taking username for now
    private fun verifyAccount() {
        val inputUsername = verifyUsername.text.toString()
        // val inputEmail = verifyEmail.text.toString() // Implement in milestone 4

        // Check and display message if Email/Username is empty, is correct, and is incorrect
        if (verifyUsername.text.toString().isEmpty()) {
            Toast.makeText(
                this,
                "Username can not be empty. Please enter and Username.",
                Toast.LENGTH_SHORT
            ).show()
        } else if ((
                    (inputUsername == Passing.username) ||
                            (inputUsername == adminUsername)
                    )
        ) {
            verificationRandomCode()

            Toast.makeText(this, "Account Successfully Verified", Toast.LENGTH_LONG).show()
            val intent = Intent(this, VerificationCodeActivity::class.java)
                .putExtra("genNum", generatedNumber)
                .putExtra("email/username", inputUsername)
            startActivity(intent)
        } else {
            Toast.makeText(
                this,
                "Sorry, Username was not found",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    // Generate a random 6 number total for verification
    private fun verificationRandomCode() {
        val randomNumber = (123457..987654).shuffled().last()

        generatedNumber = randomNumber.toString()
    }
}