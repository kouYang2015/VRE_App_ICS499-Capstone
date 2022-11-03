package com.metrostateics499.vre_app

import android.content.Intent
import android.net.Uri
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

        btn1.setOnClickListener {
            var i = Intent(Intent.ACTION_VIEW, Uri.parse("tel:" + contact.phone))
            startActivity(i)
        }

        btn2.setOnClickListener {

            var i = Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + contact.phone))
            startActivity(i)
        }
    }
}
