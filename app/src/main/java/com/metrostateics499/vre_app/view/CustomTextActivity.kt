package com.metrostateics499.vre_app.view

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.metrostateics499.vre_app.R
import com.metrostateics499.vre_app.model.Passing
import com.metrostateics499.vre_app.model.data.CustomTextMessage
import com.metrostateics499.vre_app.utility.CustomTextPopUps

class CustomTextActivity : AppCompatActivity(), CustomTextPopUps.Listener {

    private var buttonAdd: Button? = null
    private var buttonEdit: Button? = null
    private var buttonDelete: Button? = null
    private var textViewSelected: String = ""
    private var textViewSelectedBoolean: Boolean = false
    private var viewSelected: View? = null
    private lateinit var customTextObjectSelected: CustomTextMessage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_text_message_menu)

        buttonAdd = findViewById<View>(R.id.buttonAdd) as Button
        buttonAdd!!.setOnClickListener { openPopUp(textViewSelected, "add") }
        buttonEdit = findViewById<View>(R.id.buttonEdit) as Button
        buttonEdit!!.setOnClickListener { checkSelectForEdit() }
        buttonDelete = findViewById<View>(R.id.buttonDelete) as Button
        buttonDelete!!.setOnClickListener { checkSelectForDelete() }
        refreshList()
    }

    private fun refreshList() {
        textViewSelectedBoolean = false
        textViewSelected = ""

        val listview: ListView = findViewById(R.id.listView)
        val arrayAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            Passing.customTextMessageList
        )
        listview.adapter = arrayAdapter
        listview.setOnItemClickListener { parent, view, position, id ->
            viewSelected?.setBackgroundResource(
                androidx.appcompat.R.drawable
                    .abc_item_background_holo_light
            )
            Toast.makeText(
                this@CustomTextActivity,
                "You have selected " +
                    parent.getItemAtPosition(position),
                Toast.LENGTH_SHORT
            ).show()
            textViewSelected = parent.getItemAtPosition(position).toString()
            customTextObjectSelected = parent.getItemAtPosition(position) as CustomTextMessage
            viewSelected = view
            textViewSelectedBoolean = true
            view.setBackgroundResource(androidx.appcompat.R.drawable.abc_list_pressed_holo_dark)
        }
    }

    private fun checkSelectForDelete() {
        if (textViewSelectedBoolean) {
            openPopUp(textViewSelected, "delete")
        } else {
            Toast.makeText(
                this@CustomTextActivity, "You must select a text message first",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun checkSelectForEdit() {
        if (textViewSelectedBoolean) {
            openPopUp(textViewSelected, "edit")
        } else {
            Toast.makeText(
                this@CustomTextActivity, "You must select a text message first",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun openPopUp(textViewSelected: String, buttonType: String) {
        val customTextPopUp = CustomTextPopUps(textViewSelected, buttonType)
        customTextPopUp.show(supportFragmentManager, "example dialog")
    }

    override fun addCustomTextMessage(customTextString: String) {
        if (customTextString.trim().isEmpty()) {
            Toast.makeText(
                this@CustomTextActivity,
                "Please enter a custom text message",
                Toast.LENGTH_SHORT
            ).show()
            openPopUp(textViewSelected, "add")
        } else if (customTextString.trim().isNotEmpty() &&
            checkUniqueness(customTextString.trim())
        ) {
            Passing.customTextMessageList.add(CustomTextMessage(customTextString.trim()))
            Toast.makeText(
                this@CustomTextActivity,
                "New Custom Text Message Successfully " +
                    "Added",
                Toast.LENGTH_SHORT
            ).show()
            refreshList()
        } else {
            Toast.makeText(
                this@CustomTextActivity,
                "That custom text message already exists. Type something else or click cancel",
                Toast.LENGTH_SHORT
            ).show()
            openPopUp(textViewSelected, "add")
        }
    }

    override fun editCustomTextMessage(customTextString: String) {
        if (customTextString.trim().isEmpty()) {
            Toast.makeText(
                this@CustomTextActivity,
                "Custom text message can't be empty",
                Toast.LENGTH_SHORT
            ).show()
            openPopUp(textViewSelected, "edit")
        } else if (customTextString == textViewSelected) {
            Toast.makeText(
                this@CustomTextActivity, "Make a change or click cancel",
                Toast.LENGTH_SHORT
            ).show()
            openPopUp(textViewSelected, "edit")
        } else if (customTextString.trim().isNotEmpty()) {
            customTextObjectSelected.textMessage = customTextString
            Toast.makeText(
                this@CustomTextActivity, "Successfully Edited",
                Toast.LENGTH_SHORT
            ).show()
            refreshList()
        } else {
            Toast.makeText(
                this@CustomTextActivity,
                "That custom text message already exists. " +
                    "Try something else or click cancel.",
                Toast.LENGTH_SHORT
            ).show()
            openPopUp(textViewSelected, "edit")
        }
    }

    override fun deleteCustomTextMessage(customTextString: String) {
        if (textViewSelected.isNotEmpty()) {
            Passing.customTextMessageList.remove(customTextObjectSelected)
            Toast.makeText(
                this@CustomTextActivity,
                "You have deleted custom text message: " +
                    textViewSelected,
                Toast.LENGTH_SHORT
            ).show()
            refreshList()
        }
    }

    fun checkUniqueness(customTextString: String): Boolean {
        for (item in Passing.customTextMessageList) {
            if (item.textMessage == customTextString) {
                return false
            }
        }
        return true
    }
}
