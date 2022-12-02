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
            checkSelectForEdit(viewSelectedBoolean)
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

    private fun checkSelectForEdit(viewSelectedBoolean: Boolean) {
        if (viewSelectedBoolean) {
//            Passing.selectedEmergencyMessageSetup =
//                Passing.emergencyMessageSetupList.findEmergencyMessageSetup(titleSelectedString)
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
        Passing.emergencyMessageSetupList.remove(Passing.selectedEmergencyMessageSetup)
        Toast.makeText(
            this@EmergencyMessageSetupActivity,
            "You have deleted: " +
                customTextString,
            Toast.LENGTH_SHORT
        ).show()
        refreshList()
    }

    override fun addEmergencyMessageSetup(
        inputTitle: String,
        inputPhrase: String,
        inputText: String
    ) {
        val newContactList: MutableList<Contact> = mutableListOf()

        if (inputTitle.isEmpty() || inputPhrase.isEmpty() || inputText.isEmpty()) {
            Toast.makeText(
                this@EmergencyMessageSetupActivity,
                "Please enter all fields",
                Toast.LENGTH_SHORT
            ).show()
            openPopUp(titleSelectedString, "add")
        } else if (!checkTitleUniqueness(inputTitle) && !checkKeyPhraseUniqueness(inputPhrase)) {
            Toast.makeText(
                this@EmergencyMessageSetupActivity,
                "That Title and Key Phrase Already Exists. Try again.",
                Toast.LENGTH_SHORT
            ).show()
            openPopUp(titleSelectedString, "add")
        } else if (!checkKeyPhraseUniqueness(inputPhrase)) {
            Toast.makeText(
                this@EmergencyMessageSetupActivity,
                "That Key Phrase Already Exists. Try again.",
                Toast.LENGTH_SHORT
            ).show()
            openPopUp(titleSelectedString, "add")
        } else if (checkTitleUniqueness(inputTitle) && checkKeyPhraseUniqueness(inputPhrase)) {
            val newEmergencyMessageSetup = EmergencyMessageSetup(
                inputTitle,
                KeyPhrase(inputPhrase),
                CustomTextMessage(inputText),
                newContactList
            )
            Passing.emergencyMessageSetupList.add(newEmergencyMessageSetup)
            Toast.makeText(
                this@EmergencyMessageSetupActivity,
                "New Emergency Message Successfully " +
                    "Added",
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

    private fun checkKeyPhraseUniqueness(keyPhrase: String,): Boolean {
        for (item in Passing.emergencyMessageSetupList) {
            if (item.keyPhrase.phrase.equals(keyPhrase, true)) {
                return false
            }
        }
        return true
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