package com.metrostateics499.vre_app.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.metrostateics499.vre_app.R
import com.metrostateics499.vre_app.model.Passing

class EditProfileInformationActivity : AppCompatActivity() {

    private lateinit var editFullName: EditText
    private lateinit var editUserName: EditText
    private lateinit var editEmail: EditText
    private lateinit var editPhoneNumber: EditText
    private lateinit var updateButton: Button
    private lateinit var cancelButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // This is to hide the action bar
        supportActionBar?.hide()

        setContentView(R.layout.activity_edit_profile_information)

        editFullName = findViewById(R.id.edit_full_name)
        editUserName = findViewById(R.id.edit_username)
        editEmail = findViewById(R.id.edit_email)
        editPhoneNumber = findViewById(R.id.edit_phone_number)
        updateButton = findViewById(R.id.update_profile_button)
        cancelButton = findViewById(R.id.cancel_button)

        editFullName.setText(Passing.fullName)
        editUserName.setText(Passing.username)
        editEmail.setText(Passing.email)
        editPhoneNumber.setText(Passing.phoneNumber)

        updateButton.setOnClickListener {
            editProfileInformation()
            saveData()

            val intent = Intent(this, ProfileInformationActivity::class.java)
            startActivity(intent)
        }

        cancelButton.setOnClickListener {
            val intent = Intent(this, ProfileInformationActivity::class.java)
            startActivity(intent)
        }
    }

    // Edit profile information
    private fun editProfileInformation() {
        Passing.setFullName(editFullName.text.toString())
        Passing.setUsername(editUserName.text.toString())
        Passing.setEmail(editEmail.text.toString())
        Passing.setPhoneNumber(editPhoneNumber.text.toString())
    }

    /**
     * The function to save data to file
     */
    private fun saveData() {
        // Save user Info, but not stored
        // (UserAccount.xml) is the file where the user is being saved from
        val sharedPref = getSharedPreferences("UserAccount", Context.MODE_PRIVATE)
        val edit = sharedPref.edit()

        // Save their name,username,email and password
        edit.putString("Name", editFullName.text.toString())
        edit.putString("User Name", editUserName.text.toString())
        edit.putString("Email", editEmail.text.toString())
        edit.putString("Phone", editPhoneNumber.text.toString())
        edit.apply()

        Toast.makeText(
            this,
            editFullName.text.toString().plus(" your profile has been updated."),
            Toast.LENGTH_LONG
        )
            .show()
    }
}