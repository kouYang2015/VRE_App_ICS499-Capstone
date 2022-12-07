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
import com.metrostateics499.vre_app.model.data.*
import com.metrostateics499.vre_app.utility.EmergencyMessagePopUps
import com.metrostateics499.vre_app.view.adapters.EmergencyMessageSetupAdapter
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
        supportActionBar?.hide()
        setContentView(R.layout.activity_emergency_message_setup_menu)

        buttonAdd = findViewById<View>(R.id.buttonAdd) as Button
        buttonEdit = findViewById<View>(R.id.buttonEdit) as Button
        buttonDelete = findViewById<View>(R.id.buttonDelete) as Button

        refreshList()
    }

    override fun refreshList() {
        val emergencyMessageSetupAdapter = EmergencyMessageSetupAdapter(
            Passing.emergencyMessageSetupList, this
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
//            if(checkIfActivePingLocation(titleSelectedString)){
//                Toast.makeText(
//                    this@EmergencyMessageSetupActivity,
//                    "This EMS is pinging it's location. " +
//                            "Disable EMS Location Pinging in Main " +
//                            "Menu before editing",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }else {
            checkSelectForEdit(viewSelectedBoolean)
        }
        buttonDelete!!.setOnClickListener {
            titleSelectedString = emergencyMessageSetupAdapter.titleSelectedString
            viewSelectedBoolean = emergencyMessageSetupAdapter.viewSelectedBoolean
            viewSelected = emergencyMessageSetupAdapter.viewSelected
            checkSelectForDelete(viewSelectedBoolean, titleSelectedString)
        }
    }

    private fun checkIfActivePingLocation(titleSelectedString: String): Boolean {
        if (Passing.emergencyMessageSetupList.isNotEmpty()) {
            for (item in Passing.emergencyMessageSetupList) {
                if (item.title == titleSelectedString) {
                    if (item.activePingLocation) {
                        return true
                    }
                }
            }
        }
        return false
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

    private fun checkSelectForEdit(viewSelectedBoolean: Boolean) {
        if (viewSelectedBoolean) {
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
        if (Passing.selectedEmergencyMessageSetup.activePingLocation) {
            Toast.makeText(
                this@EmergencyMessageSetupActivity,
                "This Emergency Message is still pinging your location. " +
                    "Deactivate EMS Pinging Location First before deleting" +
                    customTextString,
                Toast.LENGTH_LONG
            ).show()
        } else {
            Passing.emergencyMessageSetupList.remove(Passing.selectedEmergencyMessageSetup)
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
        inputTitle: String,
    ) {
        val newContactList: MutableList<Contact> = mutableListOf()
        val newKeyPhraseList: MutableList<KeyPhrase> = mutableListOf()
        val newCustomTextMessageList: MutableList<CustomTextMessage> = mutableListOf()
        val newCallMessageList: MutableList<CallMessage> = mutableListOf()
        if (inputTitle.isEmpty()) {
            Toast.makeText(
                this@EmergencyMessageSetupActivity,
                "Please enter all fields",
                Toast.LENGTH_SHORT
            ).show()
            openPopUp(titleSelectedString, "add")
        } else if (!checkTitleUniqueness(inputTitle)) {
            Toast.makeText(
                this@EmergencyMessageSetupActivity,
                "That Title Already Exists. Try again.",
                Toast.LENGTH_SHORT
            ).show()
            openPopUp(titleSelectedString, "add")
        } else if (checkTitleUniqueness(inputTitle)) {
            val newEmergencyMessageSetup = EmergencyMessageSetup(
                inputTitle,
                newKeyPhraseList,
                newCustomTextMessageList,
                newCallMessageList,
                newContactList
            )
            Passing.emergencyMessageSetupList.add(newEmergencyMessageSetup)
            Toast.makeText(
                this@EmergencyMessageSetupActivity,
                "New Emergency Message Successfully Created",
                Toast.LENGTH_SHORT
            ).show()
            Passing.selectedEmergencyMessageSetup = newEmergencyMessageSetup
            goToEditPage()
        } else {
            Toast.makeText(
                this@EmergencyMessageSetupActivity,
                "That Emergency Message Title already exists. " +
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

    private fun checkTitleUniqueness(title: String): Boolean {
        for (item in Passing.emergencyMessageSetupList) {
            if (item.title.equals(title, true)) {
                return false
            }
        }
        return true
    }
}