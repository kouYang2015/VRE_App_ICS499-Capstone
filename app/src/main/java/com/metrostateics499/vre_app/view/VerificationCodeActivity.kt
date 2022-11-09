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
    private var adminCode: String = "123455"
    private var codeHint: String = ""
    private var username: String = ""

    /**
     * This overrides the onCreate of this activity
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // This is to hide the action bar
        supportActionBar?.hide()

        setContentView(R.layout.activity_verification_code)

        enterCode = findViewById(R.id.enter_code)
        textEmailUsernameHint = findViewById(R.id.verify_email_username_display)
        enterCodeButton = findViewById(R.id.verify_code_button)
        invalidCode = findViewById(R.id.invalid_verification_code)

        // Get the generate number from previous activity and stores it to this value
        codeHint = intent.getStringExtra("genNum").toString()
        // Get the username from previous activity and stores it to this value
        username = intent.getStringExtra("email/username").toString() // Use to display username

        // A message to display on the screen about the user and the generate code
        textEmailUsernameHint.text =
            username.plus(" ").plus(codeHint) // Change later to send to email

        // The verify button located on screen
        enterCodeButton.setOnClickListener {
            verifyCode()
        }
    }
    
    /**
     * A function to Verify code
     */
    private fun verifyCode() {
        val inputCode = enterCode.text.toString()
        val emptyEnterCode = "Enter code can not be empty"
        val incorrectEnterCode = "Sorry, code is incorrect. Try again"
        val lengthNotCorrectCode = "Code must be 6 digits"

        if (enterCode.text.toString().isNotEmpty()) {
            if (enterCode.length() == 6) {
                if ((inputCode == adminCode) || (inputCode == codeHint)) {
                    Toast.makeText(this, "Continue to Set New Password", Toast.LENGTH_LONG).show()

                    val intent =
                        Intent(this, SetNewPasswordActivity::class.java)
                            .putExtra("email/username", username)
                    startActivity(intent)

                    invalidCode.text = ""
                    enterCode.text.clear()
                } else {
                    // Display message telling user code is not correct
                    invalidCode.text = incorrectEnterCode
                }
            } else {
                // Display message telling user code needs to be 6 digits
                invalidCode.text = lengthNotCorrectCode
            }
        } else {
            // Display message if code is empty
            invalidCode.text = emptyEnterCode
        }
    }
}
