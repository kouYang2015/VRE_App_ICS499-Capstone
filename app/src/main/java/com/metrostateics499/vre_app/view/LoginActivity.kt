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
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var email: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // This is to hide the action bar
        supportActionBar?.hide()

        setContentView(R.layout.activity_login)

        loginButton = findViewById(R.id.login_button)
        createAccountButton = findViewById(R.id.create_account_button)
        forgotPasswordTextClickable = findViewById(R.id.forgot_password)
        username = findViewById(R.id.username)
        password = findViewById(R.id.password)
        email = findViewById(R.id.username)

        loginButton.setOnClickListener {
            authenticateLogin()
        }

        createAccountButton.setOnClickListener {
            Toast.makeText(this, "Create an Account", Toast.LENGTH_LONG).show()

            val intent = Intent(this, CreateAccountActivity::class.java)
            startActivity(intent)
        }

        forgotPasswordTextClickable.setOnClickListener {
            Toast.makeText(this, "Forgot your password? ", Toast.LENGTH_LONG).show()

            val intent = Intent(this, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }
    }

    // This is the login for username and password
    private fun authenticateLogin() {
        val inputUsername = username.text.toString()
        val inputPassword = password.text.toString()
        val inputEmail = email.text.toString()

        // Cannot able to login with an empty text
        if (username.text.toString().isEmpty() && password.text.toString()
                    .isEmpty() && email.text.isEmpty()
        ) {
            Toast.makeText(
                this,
                "Email/Username and Password is required to Login",
                Toast.LENGTH_SHORT
            ).show()
        } else if ((
                        (inputUsername == Passing.username && inputPassword == Passing.password) ||
                                (inputUsername == "username" && inputPassword == "password") ||
                                (inputEmail == Passing.email && inputPassword == Passing.password)
                        )
        ) {

            Toast.makeText(this, "Successfully Logged In", Toast.LENGTH_LONG).show()
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
        } else {
            Toast.makeText(
                this,
                "Email/Username or Password is incorrect, Try Again",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}