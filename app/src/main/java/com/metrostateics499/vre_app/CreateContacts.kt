package com.metrostateics499.vre_app

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

/**
 * Create contacts
 *
 * @constructor Create empty Create contacts
 */
class CreateContacts : AppCompatActivity() {
    private lateinit var btnAddContact: Button
    private lateinit var fullName: EditText
    private lateinit var phoneNum: EditText
    private lateinit var listContact: ListView
    private lateinit var btnDelete: Button
    private lateinit var btnEdit: Button

    // Create the ArrayList and ArrayAdapter
    private lateinit var listNames: ArrayList<String>
    private lateinit var listOfContact: ArrayList<Contacts>
    private lateinit var createAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_contacts)

        btnAddContact = findViewById(R.id.btnAddContact)
        fullName = findViewById(R.id.editTextPersonName)
        phoneNum = findViewById(R.id.editTextPhone)
        listContact = findViewById(R.id.contactList)
        btnDelete = findViewById(R.id.btnDeleteContact)
        btnEdit = findViewById(R.id.editButton)

        listNames = ArrayList()
        listOfContact = ArrayList()
        createAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_single_choice, listNames)
        listContact.adapter = createAdapter

        btnAddContact.setOnClickListener {
            addContacts()
        }
        // Adding the toast message to the list when an item on the list is pressed
        listContact.setOnItemClickListener { adapterView, view, i, l ->
            Toast.makeText(this, "You Selected " + listNames.get(i), Toast.LENGTH_SHORT).show()
            createAdapter.notifyDataSetChanged()
        }
        // Delete contact list
        btnDelete.setOnClickListener {
            deleteContacts()
        }

        btnEdit.setOnClickListener {
            editContacts()
        }

        listContact.setOnItemLongClickListener { adapterView, view, i, l ->
            val contactclick = listOfContact[i]
            createAdapter.notifyDataSetChanged()
            // Next Activity for User Info
            val intent = Intent(this, ListOfContacts::class.java)
            saveData()
            intent.putExtra("key", contactclick)
            startActivity(intent)
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

    private fun addContacts() {
        if (fullName.text.toString().isNotEmpty() && phoneNum.text.toString().isNotEmpty()) {
            if (fullName.length() in 2..36) {
                if (phoneNum.length() in 3..10) {
                    // Add Info to the View List
                    val contact = Contacts(fullName.text.toString(), phoneNum.text.toString())
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
                fullName.setError("Full Name must be between 2 to 36 character long")
            }
        } else {
            Toast.makeText(this, "Need Information", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteContacts() {
        val position: SparseBooleanArray = listContact.checkedItemPositions
        val count = listContact.count
        var item = count - 1
        while (item >= 0) {
            if (position.get(item)) {
                createAdapter.remove(listNames.get(item))
            }
            item--
        }
        position.clear()
        createAdapter.notifyDataSetChanged()
        Toast.makeText(this, "Contact Delete", Toast.LENGTH_SHORT).show()
    }

    private fun editContacts() {
        // Inflate the dialog with custom view
        val dialogView = LayoutInflater.from(this).inflate(R.layout.activity_edit_contacts, null)
        // AlertDialogBuilder
        val builder = AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle("Edit Contact")
            .setNegativeButton("Cancel") { dialogInterface, i ->
                Log.d("Test", "Cancel Button has been click")
            }
            .setPositiveButton("Save") { dialogInterface, i ->
                val contact = Contacts(fullName.text.toString(), phoneNum.text.toString())
                listOfContact.add(contact)
                listNames.add(contact.names)
                createAdapter.notifyDataSetChanged()
                Toast.makeText(this, "Contact Edit", Toast.LENGTH_SHORT).show()
                Log.d("Test", "Cancel Button has been click")
            }
        builder.create()
        // Show Dialog
        builder.show()
    }
}
