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
import com.metrostateics499.vre_app.utility.ContactPopUps
import com.metrostateics499.vre_app.view.adapters.ContactAdapter
import kotlinx.android.synthetic.main.activity_contacts.*

class ContactActivity : AppCompatActivity(), ContactPopUps.Listener {
    private var buttonAdd: Button? = null
    private var buttonEdit: Button? = null
    private var buttonDelete: Button? = null

    private var textViewSelected: String = ""
    private var viewSelectedBoolean: Boolean = false
    private var viewSelected: View? = null

    val selectedContact: Contact = Contact("", "")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contacts)

        buttonAdd = findViewById<View>(R.id.buttonAdd) as Button
        buttonEdit = findViewById<View>(R.id.buttonEdit) as Button
        buttonDelete = findViewById<View>(R.id.buttonDelete) as Button

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
                (
                        Passing.selectedContact?.let {
                            Passing.contactList.editContact(
                                it,
                                contactName,
                                contactPhone
                            )
                        } == true
                        )
        ) {
            Toast.makeText(
                this@ContactActivity, "Contact Successfully Edited",
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
            Passing.contactList.deleteContact(Passing.selectedContact)
            Toast.makeText(
                this@ContactActivity,
                "You have deleted contact: " +
                        textViewSelected,
                Toast.LENGTH_SHORT
            ).show()
            for (item in Passing.emergencyMessageSetupList.emergencyMessageSetups) {
                Passing.selectedContact?.let {
                    item.removeContact(
                        it
                    )
                }
            }
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
            Passing.contactList.contacts
        )

        recyclerViewContacts.layoutManager = LinearLayoutManager(this)
        recyclerViewContacts.adapter = contactAdapter

        buttonAdd!!.setOnClickListener {
//            textViewSelected = contactAdapter.titleSelectedString
//            viewSelectedBoolean = contactAdapter.viewSelectedBoolean
//            viewSelected = contactAdapter.viewSelected
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

    private fun checkSelectForDelete(viewSelectedBoolean: Boolean, titleSelectedString: String) {
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
            openPopUp(titleSelectedString, "edit")
        } else {
            Toast.makeText(
                this@ContactActivity, "You must select something first",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
