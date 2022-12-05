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

class VerificationCodeLoginActivity : AppCompatActivity() {

    private lateinit var verificationCode: EditText
    private lateinit var password: EditText
    private lateinit var textEmailUsernameHint: TextView
    private lateinit var codeLoginContButton: Button
    private lateinit var invalidCredentials: TextView
    private var adminCode: String = "000000"
    private var adminPassword: String = "password"
    private var codeHint: String = ""
    private var emailUsername: String = ""

    /**
     * This overrides the onCreate of this activity
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // This is to hide the action bar
        supportActionBar?.hide()

        setContentView(R.layout.activity_verification_code_login)

        verificationCode = findViewById(R.id.code_login_verification_code)
        password = findViewById(R.id.code_login_password)
        codeLoginContButton = findViewById(R.id.code_login_cont_button)
        textEmailUsernameHint = findViewById(R.id.code_login_email_username_display)
        invalidCredentials = findViewById(R.id.code_login_invalid)

        // Get the generate number from previous activity and stores it to this value
        codeHint = intent.getStringExtra("genNum").toString()

        // Get the Email/Username from ForgotPasswordActivity
        emailUsername = intent.getStringExtra("email/username").toString()

        // A message to display on the screen about the user and the generate code
        textEmailUsernameHint.text =
            emailUsername.plus(" ").plus(codeHint) // Change later to send to text

        // The continue button on user interface
        codeLoginContButton.setOnClickListener {
            verifyCodeLogin()
        }
    }

    private fun verifyCodeLogin() {
        val userInputCode = verificationCode.text.toString()
        val userInputPassword = password.text.toString()
        val emptyInfo = "Verification Code and Password is required to continue"
        val incorrectInfo = "Verification Code or Password is incorrect. \nTry Again"
        val lengthNotCorrectCode = "Code must be 6 digits"

        if (verificationCode.text.toString().isNotEmpty() && password.text.toString()
                .isNotEmpty()
        ) {
            if (verificationCode.length() == 6) {
                if ((
                            (
                                    (userInputCode == codeHint) && (
                                            emailUsername == Passing.username ||
                                                    emailUsername == Passing.email
                                            )
                                    ) &&
                                    userInputPassword == Passing.password
                            ) ||
                    ((userInputCode == codeHint || userInputCode == adminCode)
                            && (userInputPassword == adminPassword))
                ) {
                    // Displays message when successfully logged in
                    Toast.makeText(
                        this, "Successfully Logged In",
                        Toast.LENGTH_LONG
                    ).show()
                    val intent = Intent(this, MenuActivity::class.java)
                    startActivity(intent)

                    verificationCode.text.clear()
                    password.text.clear()
                    invalidCredentials.text = ""
                } else {
                    // Display message if verification code or password is incorrect
                    invalidCredentials.text = incorrectInfo
                }
            } else {
                // Display message if user enter 6 digit or less code
                invalidCredentials.text = lengthNotCorrectCode
            }
        } else {
            // Display message if code is empty
            invalidCredentials.text = emptyInfo
        }
    }
}