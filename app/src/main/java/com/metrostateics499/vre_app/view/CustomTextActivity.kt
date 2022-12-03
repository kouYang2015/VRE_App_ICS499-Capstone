package com.metrostateics499.vre_app.view

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.metrostateics499.vre_app.R
import com.metrostateics499.vre_app.model.Passing
import com.metrostateics499.vre_app.model.data.CustomTextMessage
import com.metrostateics499.vre_app.utility.CustomTextPopUps
import com.metrostateics499.vre_app.view.adapters.CustomTextAdapter
import kotlinx.android.synthetic.main.activity_contacts.*
import kotlinx.android.synthetic.main.activity_custom_text_message_menu.*

class CustomTextActivity : AppCompatActivity(), CustomTextPopUps.Listener {

    private var buttonAdd: Button? = null
    private var buttonEdit: Button? = null
    private var buttonDelete: Button? = null
    private var buttonViewEntireText: Button? = null
    private var textViewSelected: String = ""
    private var textViewSelectedBoolean: Boolean = false
    private var viewSelected: View? = null
    private lateinit var customTextObjectSelected: CustomTextMessage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_text_message_menu)

        buttonAdd = findViewById<View>(R.id.buttonAdd) as Button
//        buttonAdd!!.setOnClickListener { openPopUp(textViewSelected, "add") }
        buttonEdit = findViewById<View>(R.id.buttonEdit) as Button
//        buttonEdit!!.setOnClickListener { checkSelectForEdit() }
        buttonDelete = findViewById<View>(R.id.buttonDelete) as Button
//        buttonDelete!!.setOnClickListener { checkSelectForDelete() }
        buttonViewEntireText = findViewById<View>(R.id.buttonViewEntireText) as Button
        refreshList()
    }

    private fun refreshList() {
        val customTextAdapterAdapter = CustomTextAdapter(
            Passing.customTextMessageList, this
        )

        recyclerViewCustomText.layoutManager = LinearLayoutManager(this)
        recyclerViewCustomText.adapter = customTextAdapterAdapter

        buttonAdd!!.setOnClickListener {
            openPopUp(textViewSelected, "add")
        }
        buttonEdit!!.setOnClickListener {
            textViewSelected = customTextAdapterAdapter.titleSelectedString
            textViewSelectedBoolean = customTextAdapterAdapter.viewSelectedBoolean
            viewSelected = customTextAdapterAdapter.viewSelected
            checkSelectForEdit(textViewSelectedBoolean, textViewSelected)
        }
        buttonDelete!!.setOnClickListener {
            textViewSelected = customTextAdapterAdapter.titleSelectedString
            textViewSelectedBoolean = customTextAdapterAdapter.viewSelectedBoolean
            viewSelected = customTextAdapterAdapter.viewSelected
            checkSelectForDelete(textViewSelectedBoolean, textViewSelected)
        }
        buttonViewEntireText!!.setOnClickListener {
            openPopUp("null", "viewEntireText")
        }

//        textViewSelectedBoolean = false
//        textViewSelected = ""
//
//        val listview: ListView = findViewById(R.id.recyclerViewCustomText)
//        val arrayAdapter = ArrayAdapter(
//            this,
//            android.R.layout.simple_list_item_1,
//            Passing.customTextMessageList
//        )
//        listview.adapter = arrayAdapter
//        listview.setOnItemClickListener { parent, view, position, _ ->
//            viewSelected?.setBackgroundResource(
//                androidx.appcompat.R.drawable
//                    .abc_item_background_holo_light
//            )
//            Toast.makeText(
//                this@CustomTextActivity,
//                "You have selected " +
//                    parent.getItemAtPosition(position),
//                Toast.LENGTH_SHORT
//            ).show()
//            textViewSelected = parent.getItemAtPosition(position).toString()
//            customTextObjectSelected = parent.getItemAtPosition(position) as CustomTextMessage
//            viewSelected = view
//            textViewSelectedBoolean = true
//            view.setBackgroundResource(androidx.appcompat.R.drawable.abc_list_pressed_holo_dark)
//        }
    }

    private fun checkSelectForDelete(textViewSelectedBoolean: Boolean, textViewSelected: String) {
        if (textViewSelectedBoolean) {
            openPopUp(textViewSelected, "delete")
        } else {
            Toast.makeText(
                this@CustomTextActivity, "You must select a text message first",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun checkSelectForEdit(textViewSelectedBoolean: Boolean, textViewSelected: String) {
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

    override fun addCustomTextMessage(customTextTitle: String, customTextString: String) {
        if (customTextString.trim().isEmpty() || customTextTitle.trim().isEmpty()) {
            Toast.makeText(
                this@CustomTextActivity,
                "Please enter both field(s)",
                Toast.LENGTH_SHORT
            ).show()
            openPopUp(textViewSelected, "add")
        } else {
            Passing.customTextMessageList.add(CustomTextMessage(customTextTitle, customTextString
                .trim()))
            Toast.makeText(
                this@CustomTextActivity,
                "New Custom Text Message Successfully " +
                    "Added",
                Toast.LENGTH_SHORT
            ).show()
            refreshList()
        }
    }

    override fun editCustomTextMessage(customTextTitle: String, customTextString: String) {
        if (customTextString.trim().isEmpty() || customTextTitle.trim().isEmpty()) {
            Toast.makeText(
                this@CustomTextActivity,
                "field(s) can't be empty",
                Toast.LENGTH_SHORT
            ).show()
            openPopUp(textViewSelected, "edit")
        } else if (customTextString == textViewSelected &&
            customTextTitle == Passing.selectedCustomTextObject.title
        ) {
            Toast.makeText(
                this@CustomTextActivity, "Make a change or click cancel",
                Toast.LENGTH_SHORT
            ).show()
            openPopUp(textViewSelected, "edit")
        } else if (customTextString.trim().isNotEmpty() && customTextTitle.isNotEmpty()) {
            Passing.selectedCustomTextObject.title = customTextTitle
            Passing.selectedCustomTextObject.textMessage = customTextString
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
            Passing.customTextMessageList.remove(Passing.selectedCustomTextObject)
            Toast.makeText(
                this@CustomTextActivity,
                "You have deleted custom text message: " +
                    textViewSelected,
                Toast.LENGTH_SHORT
            ).show()
            for (item in Passing.emergencyMessageSetupList) {
                Passing.selectedCustomTextObject.let {
                    item.selectedCustomTextMessages.remove(it)
                }
            }
            refreshList()
        }
    }

    private fun checkUniqueness(customTextString: String): Boolean {
        for (item in Passing.customTextMessageList) {
            if (item.textMessage == customTextString) {
                return false
            }
        }
        return true
    }
}
