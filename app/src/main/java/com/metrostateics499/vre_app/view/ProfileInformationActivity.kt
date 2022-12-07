package com.metrostateics499.vre_app.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.metrostateics499.vre_app.R
import com.metrostateics499.vre_app.model.Passing

class ProfileInformationActivity : AppCompatActivity() {

    private lateinit var displayFullName: TextView
    private lateinit var displayUserName: TextView
    private lateinit var displayEmail: TextView
    private lateinit var displayPhoneNumber: TextView
    private lateinit var editButton: Button
    private lateinit var editProfileBackButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // This is to hide the action bar
        supportActionBar?.hide()

        setContentView(R.layout.activity_profile_information)

        displayFullName = findViewById(R.id.full_name_display)
        displayUserName = findViewById(R.id.username_display)
        displayEmail = findViewById(R.id.email_display)
        displayPhoneNumber = findViewById(R.id.phone_number_display)
        editButton = findViewById(R.id.edit_profile_button)
        editProfileBackButton = findViewById(R.id.edit_profile_back_button)

        displayFullName.text = Passing.fullName
        displayUserName.text = Passing.username
        displayEmail.text = Passing.email
        displayPhoneNumber.text = Passing.phoneNumber

        editButton.setOnClickListener {
            val intent = Intent(this, EditProfileInformationActivity::class.java)
            startActivity(intent)
        }

        editProfileBackButton.setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
        }
    }
}