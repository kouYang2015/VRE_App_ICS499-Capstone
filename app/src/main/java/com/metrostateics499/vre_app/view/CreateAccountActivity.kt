package com.metrostateics499.vre_app.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.metrostateics499.vre_app.R
import com.metrostateics499.vre_app.model.Passing

class CreateAccountActivity : AppCompatActivity() {
    private lateinit var btnCreateAccount: Button
    private lateinit var userName: EditText
    private lateinit var fullName: EditText
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var confirmPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        btnCreateAccount = findViewById(R.id.buttonCreateAccount)
        email = findViewById(R.id.editTextEmailAddress)
        confirmPassword = findViewById(R.id.editConfirmPassword)
        password = findViewById(R.id.editPassword)
        userName = findViewById(R.id.editTextUserName)
        fullName = findViewById(R.id.editTextPersonName)

        btnCreateAccount.setOnClickListener {
            // Calls this to verify
            validateEmptyForm()
        }
    }

    private fun validateEmptyForm() {
        if (fullName.text.toString().isNotEmpty() && userName.text.toString()
                    .isNotEmpty() && email.text.toString().isNotEmpty() && password.text.toString()
                    .isNotEmpty() && confirmPassword.text.toString().isNotEmpty()
        ) {
            // Need a valid Email to work (ex. abc123@gmail.net)
            if (email.length() in 6..319 && email.text.toString()
                        .matches(Regex("[a-zA-Z0-9._]+@[a-z].+[a-z]"))
            ) {
                // Maximum length for the full name, username, password, email and confirm password
                if (fullName.length() in 6..36 && userName.length() in 4..36 &&
                        password.length() in 8..36 && confirmPassword.length() in 8..36
                ) {
                    if (password.text.toString() == confirmPassword.text.toString()) {
                        Toast.makeText(
                            this,
                            "Created Account Successful!",
                            Toast.LENGTH_LONG
                        )
                            .show()

                        // Setting the Username and Password (for the Login)
                        Passing.setUsername(userName.text.toString())
                        Passing.setPassword(password.text.toString())
                        Passing.setEmail(email.text.toString())

                        // Calls this method to save info from user
                        saveData()

                        // Goes to new Activity (Login Activity)
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)

                        // Clearing the info for next user
                        fullName.text.clear()
                        userName.text.clear()
                        email.text.clear()
                        password.text.clear()
                        confirmPassword.text.clear()

                        // Message from the system that password doesn't match
                    } else if (password.text.toString() != confirmPassword.text.toString()) {
                        password.setError("Password must match to confirm password!")
                        confirmPassword.setError("Confirm Password must match to password!")
                    }
                    // Create an error message if criteria  does not match
                } else {
                    fullName.setError("Full Name must be between 6 to 36 character long")
                    userName.setError("User Name must be between 4 to 36 character long")
                    password.setError("Password must be between 8 to 36 character long")
                    confirmPassword.setError(
                        "Confirm Password must be between 8 to 36 character character long"
                    )
                }
                // Create an error message if e-mail the user didn't input '@'
            } else {
                email.setError("Please Enter a Valid Email")
            }
            // Create an error message if user did not write anything
        } else {
            Toast.makeText(this, "Need to All Information", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveData() {
        // Save user Info, but not stored
        // (UserAccount.xml) is the file where the user is being saved from
        val sharedPref = getSharedPreferences("UserAccount", Context.MODE_PRIVATE)
        val edit = sharedPref.edit()
        // Save their name,username,email and password
        edit.putString("Name", fullName.text.toString())
        edit.putString("User Name", userName.text.toString())
        edit.putString("Email", email.text.toString())
        edit.putString("Password", password.text.toString())
        edit.apply()

        Toast.makeText(this, "Data has been saved", Toast.LENGTH_LONG).show()
        sharedPref.getString("Name", "User Name")?.let { Log.d("Debug", it) }
    }
}
