package com.example.vre_app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class Login : AppCompatActivity() {
    private lateinit var loginButton: Button
    private lateinit var createAccountButton: Button
    private lateinit var forgotPasswordTextClickable: TextView
    private lateinit var username: EditText
    private lateinit var password: EditText

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

        loginButton.setOnClickListener {
            authenticateLogin()
        }

        createAccountButton.setOnClickListener {
            val text = "Create an Account"
            val duration = Toast.LENGTH_LONG
            val toast = Toast.makeText(this, text, duration)

            toast.show()

            val intent = Intent(this, CreateAccount::class.java)
            startActivity(intent)
        }

        forgotPasswordTextClickable.setOnClickListener {
            val text = "Forgot your password?"
            val duration = Toast.LENGTH_LONG
            val toast = Toast.makeText(this, text, duration)

            toast.show()

            val intent = Intent(this, ForgotPassword::class.java)
            startActivity(intent)
        }

    }

    // This is the login for username and password
    private fun authenticateLogin() {
        val inputUsername = username.text.toString()
        val inputPassword = password.text.toString()

        if((inputUsername == Passing.username && inputPassword == Passing.password) ||
            (inputUsername == "username" && inputPassword == "password")) {
            Toast.makeText(this, "Successfully Logged In", Toast.LENGTH_LONG).show()
            val intent = Intent(this, Menu::class.java)
            startActivity(intent)
        }
        else {
            val text = "Email/Username or Password is incorrect, Try Again"
            val duration = Toast.LENGTH_LONG
            val toast = Toast.makeText(this, text, duration)
            toast.show()
        }



    }
}