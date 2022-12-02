package com.metrostateics499.vre_app.view

import android.content.Intent
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
import com.metrostateics499.vre_app.utility.EmergencyMessagePopUps
import com.metrostateics499.vre_app.view.adapters.EmergencyMessageSetupAdapter
import java.util.LinkedList
import kotlinx.android.synthetic.main.activity_emergency_message_setup_menu.*

class EmergencyMessageSetupActivity : AppCompatActivity(), EmergencyMessagePopUps.Listener {
    private var buttonAdd: Button? = null
    private var buttonEdit: Button? = null
    private var buttonDelete: Button? = null

    private var titleSelectedString: String = ""
    private var viewSelectedBoolean: Boolean = false
    private var viewSelected: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emergency_message_setup_menu)

        buttonAdd = findViewById<View>(R.id.buttonAdd) as Button
        buttonEdit = findViewById<View>(R.id.buttonEdit) as Button
        buttonDelete = findViewById<View>(R.id.buttonDelete) as Button

        refreshList()
    }

    override fun refreshList() {
        val emergencyMessageSetupAdapter = EmergencyMessageSetupAdapter(
            Passing.emergencyMessageSetupList.emergencyMessageSetups, this
        )

        recyclerViewEmergencyMessageSetup.layoutManager = LinearLayoutManager(this)
        recyclerViewEmergencyMessageSetup.adapter = emergencyMessageSetupAdapter

        buttonAdd!!.setOnClickListener {
            titleSelectedString = emergencyMessageSetupAdapter.titleSelectedString
            viewSelectedBoolean = emergencyMessageSetupAdapter.viewSelectedBoolean
            viewSelected = emergencyMessageSetupAdapter.viewSelected
            openPopUp(titleSelectedString, "add")
        }
        buttonEdit!!.setOnClickListener {
            titleSelectedString = emergencyMessageSetupAdapter.titleSelectedString
            viewSelectedBoolean = emergencyMessageSetupAdapter.viewSelectedBoolean
            viewSelected = emergencyMessageSetupAdapter.viewSelected
            checkSelectForEdit(viewSelectedBoolean, titleSelectedString)
        }
        buttonDelete!!.setOnClickListener {
            titleSelectedString = emergencyMessageSetupAdapter.titleSelectedString
            viewSelectedBoolean = emergencyMessageSetupAdapter.viewSelectedBoolean
            viewSelected = emergencyMessageSetupAdapter.viewSelected
            checkSelectForDelete(viewSelectedBoolean, titleSelectedString)
        }
    }

    private fun checkSelectForDelete(viewSelectedBoolean: Boolean, titleSelectedString: String) {
        if (viewSelectedBoolean) {
            openPopUp(titleSelectedString, "delete")
        } else {
            Toast.makeText(
                this@EmergencyMessageSetupActivity, "You must select something first",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun checkSelectForEdit(viewSelectedBoolean: Boolean, titleSelectedString: String) {
        if (viewSelectedBoolean) {
            Passing.selectedEmergencyMessageSetup =
                Passing.emergencyMessageSetupList.findEmergencyMessageSetup(titleSelectedString)
//            openPopUp(titleSelectedString,"edit")
            goToEditPage()
        } else {
            Toast.makeText(
                this@EmergencyMessageSetupActivity, "You must select something first",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun openPopUp(textViewSelected: String, buttonType: String) {
        val emergencyMessagePopUps = EmergencyMessagePopUps(textViewSelected, buttonType)
        emergencyMessagePopUps.show(supportFragmentManager, "example dialog")
    }

    override fun deleteEmergencyMessageSetup(customTextString: String) {
        if (customTextString.isNotEmpty()) {
            Passing.emergencyMessageSetupList.deleteEmergencyMessageSetup((customTextString))
            Toast.makeText(
                this@EmergencyMessageSetupActivity,
                "You have deleted: " +
                    customTextString,
                Toast.LENGTH_SHORT
            ).show()
            refreshList()
        }
    }

    override fun addEmergencyMessageSetup(
        titleName: String,
        keyPhrase: String,
        customText: String
    ) {
        val newContactList: MutableList<Contact> = LinkedList()
        if (titleName.isEmpty() || keyPhrase.isEmpty() || customText.isEmpty()) {
            Toast.makeText(
                this@EmergencyMessageSetupActivity,
                "Please enter all fields",
                Toast.LENGTH_SHORT
            ).show()
            openPopUp(titleSelectedString, "add")
        } else if (Passing.emergencyMessageSetupList.checkKeyPhraseDuplicate(keyPhrase.trim())) {
            Toast.makeText(
                this@EmergencyMessageSetupActivity,
                "That Key Phrase Already Exists. Try again.",
                Toast.LENGTH_SHORT
            ).show()
            openPopUp(titleSelectedString, "add")
        } else if (Passing.emergencyMessageSetupList.addEmergencyMessageSetup(
                EmergencyMessageSetup(
                        titleName.trim(),
                        KeyPhrase(keyPhrase.trim()),
                        CustomTextMessage(customText.trim()),
                        newContactList
                    )
            )
        ) {
            Toast.makeText(
                this@EmergencyMessageSetupActivity,
                "New Emergency Message Successfully " +
                    "Added",
                Toast.LENGTH_SHORT
            ).show()
            Passing.selectedEmergencyMessageSetup =
                Passing.emergencyMessageSetupList.findEmergencyMessageSetup(titleName)
            goToEditPage()
        } else {
            Toast.makeText(
                this@EmergencyMessageSetupActivity,
                "That Emergency Message already exists. " +
                    "Try something else or click cancel",
                Toast.LENGTH_SHORT
            ).show()
            openPopUp(titleSelectedString, "add")
        }
    }

    override fun goToEditPage() {
        startActivity(Intent(this, EditEmergencyMessageActivity()::class.java))
    }

    override fun onPostResume() {
        super.onPostResume()
        refreshList()
    }
}