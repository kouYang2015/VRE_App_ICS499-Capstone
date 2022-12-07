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

class LoginActivity : AppCompatActivity() {
    private lateinit var loginButton: Button
    private lateinit var createAccountButton: Button
    private lateinit var forgotPasswordTextClickable: TextView
    private lateinit var forgotEmailUsernameClickable: TextView
    private lateinit var invalidCredentials: TextView
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var email: EditText
    private var adminUsername: String = "username"
    private var adminPassword: String = "password"

    /**
     * This overrides the onCreate of this activity
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // This is to hide the action bar
        supportActionBar?.hide()

        setContentView(R.layout.activity_login)

        loginButton = findViewById(R.id.login_button)
        createAccountButton = findViewById(R.id.create_account_button)
        forgotPasswordTextClickable = findViewById(R.id.forgot_password)
        forgotEmailUsernameClickable = findViewById(R.id.forgot_email_username)
        invalidCredentials = findViewById(R.id.invalid_credentials)
        username = findViewById(R.id.username)
        password = findViewById(R.id.password)
        email = findViewById(R.id.username)

        // This is the login button
        loginButton.setOnClickListener {
            authenticateLogin()
        }

        // This is the create account button
        createAccountButton.setOnClickListener {
            Toast.makeText(this, "Create an Account", Toast.LENGTH_LONG).show()

            val intent = Intent(this, CreateAccountActivity::class.java)
            startActivity(intent)
        }

        // This is the forgot password text that is clickable
        forgotPasswordTextClickable.setOnClickListener {
            // Display message when user clicks it
            Toast.makeText(this, "Forgot your password? ", Toast.LENGTH_LONG).show()

            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

        // This is the forgot email or username text that is clickable
        forgotEmailUsernameClickable.setOnClickListener {
            // Display message when user clicks it
            Toast.makeText(this, "Forgot your Email/Username? ", Toast.LENGTH_LONG).show()

            val intent = Intent(this, ForgotEmailUsernameActivity::class.java)
            startActivity(intent)
        }
    }

    /**
     * This is the login for username and password
     */
    private fun authenticateLogin() {
        val inputUsername = username.text.toString()
        val inputPassword = password.text.toString()
        val inputEmail = email.text.toString()
        val emptyInfo = "Email/Username and Password is required to Login"
        val incorrectInfo = "Email/Username or Password is incorrect, Try Again"

        // Cannot login with an empty text
        if (username.text.toString().isEmpty() && password.text.toString()
                .isEmpty() && email.text.isEmpty()
        ) {
            // Displays a message on screen if it is empty
            invalidCredentials.text = emptyInfo
        } else if ((
                    (
                            (inputUsername == Passing.username || inputEmail == Passing.email) &&
                                    inputPassword == Passing.password
                            ) ||
                            (inputUsername == adminUsername && inputPassword == adminPassword)
                    )
        ) {
            // Displays message when successfully logged in
            Toast.makeText(this, "Successfully Logged In", Toast.LENGTH_LONG).show()
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)

            username.text.clear()
            email.text.clear()
            password.text.clear()
            invalidCredentials.text = ""
        } else {
            // Display message if email/username or password is incorrect
            invalidCredentials.text = incorrectInfo
        }
    }
}