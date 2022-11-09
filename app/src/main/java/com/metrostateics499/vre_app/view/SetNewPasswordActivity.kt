package com.metrostateics499.vre_app.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.metrostateics499.vre_app.R
import com.metrostateics499.vre_app.model.Passing

class SetNewPasswordActivity : AppCompatActivity() {

    private lateinit var newPassword: EditText
    private lateinit var confirmNewPassword: EditText
    private lateinit var textEmailUsernameHint: TextView
    private lateinit var newPasswordContButton: Button
    private var adminUsername: String = "username"
    private var username: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // This is to hide the action bar
        supportActionBar?.hide()

        setContentView(R.layout.activity_set_new_password)

        newPassword = findViewById(R.id.new_password)
        confirmNewPassword = findViewById(R.id.confirm_new_password)
        newPasswordContButton = findViewById(R.id.new_password_cont_button)
        textEmailUsernameHint = findViewById(R.id.set_password_email_username_display)

        // Get the Email/Username from ForgotPasswordActivity
        username = intent.getStringExtra("email/username").toString()

        // Display username on user interface xml
        textEmailUsernameHint.text = username

        // The continue button on user interface
        newPasswordContButton.setOnClickListener {
            verifyReplacePassword()
        }
    }

    private fun verifyReplacePassword() {
        val inputNewPassword = newPassword.text.toString()
        val inputConfirmNewPassword = confirmNewPassword.text.toString()

        /**
         * Check and display message if new password or confirm new password is empty, are equal,
         * or does not match
         */
        if (newPassword.text.toString().isEmpty() && confirmNewPassword.text.toString().isEmpty()) {
            Toast.makeText(
                this,
                "Password is needed to continue",
                Toast.LENGTH_SHORT
            ).show()
        } else if ((inputNewPassword == inputConfirmNewPassword && username == Passing.username) ||
            (inputNewPassword == inputConfirmNewPassword && username == adminUsername)
        ) {
            replacingPassword()
            saveData()

            Toast.makeText(this, "Successfully Changed Password", Toast.LENGTH_LONG).show()

            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        } else {
            Toast.makeText(
                this,
                "Password does not match. Try Again",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    // Replace old password with new password
    private fun replacingPassword() {
        Passing.setUsername(username)
        Passing.setPassword(confirmNewPassword.text.toString())
    }

    // Save data to file
    private fun saveData() {
        // Save user Info, but not stored
        // (UserAccount.xml) is the file where the user is being saved from
        val sharedPref = getSharedPreferences("UserAccount", Context.MODE_PRIVATE)
        val edit = sharedPref.edit()

        // Save their username and password
        edit.putString("User Name", username)
        edit.putString("Password", confirmNewPassword.text.toString())
        edit.apply()

        Toast.makeText(this, username.plus(" your password has been saved"), Toast.LENGTH_LONG)
            .show()
    }
}