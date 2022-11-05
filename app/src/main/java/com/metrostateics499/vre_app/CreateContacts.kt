package com.metrostateics499.vre_app

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

/**
 * Create contacts
 *
 * @constructor Create empty Create contacts
 */
class CreateContacts : AppCompatActivity() {
    private lateinit var btnNewAccount: Button
    private lateinit var fullName: EditText
    private lateinit var phoneNum: EditText
    private lateinit var listContact: ListView
    private lateinit var btnDelete: Button

    // Create the ArrayList and ArrayAdapter
    private lateinit var listNames: ArrayList<String>
    private lateinit var listOfContact: ArrayList<Contacts>
    private lateinit var createAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_contacts)

        btnNewAccount = findViewById(R.id.btnAddContact)
        fullName = findViewById(R.id.editTextPersonName)
        phoneNum = findViewById(R.id.editTextPhone)
        listContact = findViewById(R.id.contactList)
        btnDelete = findViewById(R.id.btnDeleteContact)

        listNames = ArrayList()
        listOfContact = ArrayList()
        createAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listNames)

        listContact.adapter = createAdapter

        btnNewAccount.setOnClickListener {
            if (fullName.text.toString().isNotEmpty() && phoneNum.text.toString().isNotEmpty()) {
                if (fullName.length() in 2..36) {
                    if (phoneNum.length() in 3..10) {
                        // Add Info to the View List
                        var contact = Contacts(fullName.text.toString(), phoneNum.text.toString())
                        listOfContact.add(contact)
                        listNames.add(contact.names)
                        saveData()
                        createAdapter.notifyDataSetChanged()
                        Toast.makeText(this, "Contact Added", Toast.LENGTH_SHORT).show()

                        // Clear the inputs
                        fullName.text.clear()
                        phoneNum.text.clear()
                    } else {
                        phoneNum.setError("Phone Name must be between 3 to 10 character long")
                    }
                } else {
                    fullName.setError("Full Name must be between 6 to 36 character long")
                }
            } else {
                Toast.makeText(this, "Need Information", Toast.LENGTH_SHORT).show()
            }
        }
        // Adding the toast message to the list when an item on the list is pressed
        listContact.setOnItemClickListener { adapterView, view, i, l ->
            Toast.makeText(this, "You Selected " + listNames.get(i), Toast.LENGTH_SHORT).show()
            // Delete contact list
            btnDelete.setOnClickListener {
                listNames.remove(listNames.get(i))
                saveData()
                createAdapter.notifyDataSetChanged()
                Toast.makeText(this, "Contact Delete", Toast.LENGTH_SHORT).show()
            }
        }

        listContact.setOnItemLongClickListener { adapterView, view, i, l ->
            var contact = Contacts(fullName.text.toString(), phoneNum.text.toString())
            listOfContact.add(contact)
            listNames.add(contact.names)

            var contactclick = listOfContact[i]
            // Next Activity for User Info
            var i = Intent(this, ListOfContacts::class.java)
            saveData()
            i.putExtra("key", contactclick)
            startActivity(i)
            return@setOnItemLongClickListener true
        }
    }

    private fun saveData() {
        // Save user Info, but not stored
        // (UserAccount.xml) is the file where the user is being saved from
        val sharedPref = getSharedPreferences("UserAccount", Context.MODE_PRIVATE)
        val edit = sharedPref.edit()
        // Save their name,username,email and password
        edit.putString("Full Name", fullName.text.toString())
        edit.putString("Phone", phoneNum.text.toString())
        edit.apply()

        sharedPref.getString("Name", "Phone")?.let { Log.d("Debug", it) }
    }
}
