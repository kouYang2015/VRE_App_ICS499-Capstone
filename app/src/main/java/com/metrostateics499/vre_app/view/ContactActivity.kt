package com.metrostateics499.vre_app.view

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.metrostateics499.vre_app.R
import com.metrostateics499.vre_app.model.Passing
import com.metrostateics499.vre_app.model.data.Contact
import com.metrostateics499.vre_app.model.data.CustomTextMessage
import com.metrostateics499.vre_app.model.data.EmergencyMessageSetup
import com.metrostateics499.vre_app.model.data.KeyPhrase
import com.metrostateics499.vre_app.view.adapters.ContactAdapter
import com.metrostateics499.vre_app.view.adapters.EmergencyMessageSetupAdapter
import kotlinx.android.synthetic.main.activity_contacts.*
import kotlinx.android.synthetic.main.activity_emergency_message_setup_menu.*

class ContactActivity: AppCompatActivity(), ContactPopUps.Listener {
    private var buttonAdd: Button? = null
    private var buttonEdit: Button? = null
    private var buttonDelete: Button? = null

    private var textViewSelected: String = ""
    private var viewSelectedBoolean: Boolean = false
    private var viewSelected: View? = null

    val selectedContact : Contact = Contact("","")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts)

        buttonAdd = findViewById<View>(R.id.buttonAdd) as Button
        buttonEdit = findViewById<View>(R.id.buttonEdit) as Button
        buttonDelete = findViewById<View>(R.id.buttonDelete) as Button

        val contact1 = Contact("Jon Doe1", "651-333-6341")
        val contact2 = Contact("Jon Doe2", "651-333-6342")
        val contact3 = Contact("Jon Doe3", "651-333-6343")

        Passing.contactList.addContact(contact1)
        Passing.contactList.addContact(contact2)
        Passing.contactList.addContact(contact3)

        refreshList()
    }

    override fun editContact(contactName: String, contactPhone: String) {
        if (contactName.isEmpty() && contactPhone.isEmpty()) {
            Toast.makeText(
                this@ContactActivity,
                "Please enter fields",
                Toast.LENGTH_SHORT
            ).show()
            openPopUp(textViewSelected, "edit")

        } else if (contactName.isEmpty() && contactPhone.isNotEmpty()) {
            Toast.makeText(
                this@ContactActivity,
                "Please enter a name",
                Toast.LENGTH_SHORT
            ).show()
            openPopUp(textViewSelected, "edit")

        } else if (contactName.isNotEmpty() && contactPhone.isEmpty()) {
            Toast.makeText(
                this@ContactActivity,
                "Please enter a phone number",
                Toast.LENGTH_SHORT
            ).show()
            openPopUp(textViewSelected, "edit")
        } else if (contactName.isNotEmpty() &&
            Passing.selectedContact?.let {
                Passing.contactList.editContact(
                    it,
                    contactName,
                    contactPhone
                )
            } == true
        ) {
            Toast.makeText(
                this@ContactActivity, "Successfully Edited",
                Toast.LENGTH_SHORT
            ).show()
            refreshList()
        } else {
            Toast.makeText(
                this@ContactActivity,
                "That Name already exists. " +
                        "Try something else or click cancel.",
                Toast.LENGTH_SHORT
            ).show()
            openPopUp(textViewSelected, "edit")
        }
    }

    override fun deleteContact(contactName: String) {
        if (textViewSelected.isNotEmpty()) {
            Passing.contactList.deleteContact(Passing.selectedContact, textViewSelected)
            Toast.makeText(
                this@ContactActivity,
                "You have deleted contact: " +
                        textViewSelected,
                Toast.LENGTH_SHORT
            ).show()
            refreshList()
        }
    }

    override fun addContact(contactName: String, contactPhone: String) {
        if (contactName.isEmpty() && contactPhone.isEmpty()) {
            Toast.makeText(
                this@ContactActivity,
                "Please enter fields",
                Toast.LENGTH_SHORT
            ).show()
            openPopUp(textViewSelected, "add")

        } else if (contactName.isEmpty() && contactPhone.isNotEmpty()) {
                Toast.makeText(
                    this@ContactActivity,
                    "Please enter a name",
                    Toast.LENGTH_SHORT
                ).show()
                openPopUp(textViewSelected, "add")

        } else if (contactName.isNotEmpty() && contactPhone.isEmpty()) {
            Toast.makeText(
                this@ContactActivity,
                "Please enter a phone number",
                Toast.LENGTH_SHORT
            ).show()
            openPopUp(textViewSelected, "add")
        } else if (contactName.isNotEmpty() && contactPhone.isNotEmpty() &&
            Passing.contactList.addContact(
                Contact(
                    contactName,
                    contactPhone
                )
            )
        ) {
            Toast.makeText(
                this@ContactActivity,
                "Contact Successfully " +
                        "Added",
                Toast.LENGTH_SHORT
            ).show()
            Passing.selectedContact =
                Passing.contactList.findContact(contactName)
            refreshList()
        } else {
            Toast.makeText(
                this@ContactActivity,
                "That Name already exists. Try something else or click cancel",
                Toast.LENGTH_SHORT
            ).show()
            openPopUp(textViewSelected, "add")
        }
    }

    override fun refreshList() {
        val contactAdapter = ContactAdapter(
            Passing.contactList.contacts, this
        )

        recyclerViewContacts.layoutManager = LinearLayoutManager(this)
        recyclerViewContacts.adapter = contactAdapter

        buttonAdd!!.setOnClickListener {
            textViewSelected = contactAdapter.titleSelectedString
            viewSelectedBoolean = contactAdapter.viewSelectedBoolean
            viewSelected = contactAdapter.viewSelected
            openPopUp(textViewSelected, "add")
        }
        buttonEdit!!.setOnClickListener {
            textViewSelected = contactAdapter.titleSelectedString
            viewSelectedBoolean = contactAdapter.viewSelectedBoolean
            viewSelected = contactAdapter.viewSelected
            checkSelectForEdit(viewSelectedBoolean, textViewSelected)
        }
        buttonDelete!!.setOnClickListener {
            textViewSelected = contactAdapter.titleSelectedString
            viewSelectedBoolean = contactAdapter.viewSelectedBoolean
            viewSelected = contactAdapter.viewSelected
            checkSelectForDelete(viewSelectedBoolean, textViewSelected)
        }
    }

    private fun openPopUp(textViewSelected: String, buttonType: String) {
        val contactPopUps = ContactPopUps(textViewSelected, buttonType)
        contactPopUps.show(supportFragmentManager, "example dialog")
    }

    fun checkSelectForDelete(viewSelectedBoolean: Boolean, titleSelectedString: String) {
        if (viewSelectedBoolean) {
            openPopUp(titleSelectedString, "delete")
        } else {
            Toast.makeText(
                this@ContactActivity, "You must select something first",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun checkSelectForEdit(viewSelectedBoolean: Boolean, titleSelectedString: String) {
        if (viewSelectedBoolean) {
            Passing.selectedEmergencyMessageSetup =
                Passing.emergencyMessageSetupList.findEmergencyMessageSetup(titleSelectedString)
//            openPopUp(titleSelectedString,"edit")
            openPopUp(titleSelectedString, "edit")
        } else {
            Toast.makeText(
                this@ContactActivity, "You must select something first",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}


