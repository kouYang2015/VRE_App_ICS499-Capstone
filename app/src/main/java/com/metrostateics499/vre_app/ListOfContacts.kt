package com.metrostateics499.vre_app

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

/**
 * List of contacts
 *
 * @constructor Create empty List of contacts
 */
class ListOfContacts : AppCompatActivity() {

    private lateinit var textName: TextView
    private lateinit var textPhone: TextView
    private lateinit var callBtn: ImageButton
    private lateinit var messageBtn: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_of_contacts)

        callBtn = findViewById(R.id.btnCall)
        messageBtn = findViewById(R.id.btnMessage)
        textName = findViewById(R.id.textName)
        textPhone = findViewById(R.id.textPhone)

        val contact = intent.getSerializableExtra("key") as Contacts
        textName.text = contact.names
        textPhone.text = contact.phone

        callBtn.setOnClickListener {
        }

        messageBtn.setOnClickListener {
        }
    }
}
