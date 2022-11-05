package com.metrostateics499.vre_app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_list_of_contacts.*

class ListOfContacts : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_of_contacts)

        var contact = intent.getSerializableExtra("key") as Contacts
        textName.text = contact.names
        textPhone.text = contact.phone

    }
}
