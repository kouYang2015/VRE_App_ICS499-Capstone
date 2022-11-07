package com.metrostateics499.vre_app

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
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

        val keyphrase1 = KeyPhrase("keyphrase1")
        val customTextMessage1 = CustomTextMessage("customtext1")
        val emergencyMessageSetup1 = EmergencyMessageSetup("Title1", keyphrase1, customTextMessage1)
        val keyphrase2 = KeyPhrase("keyphrase2")
        val customTextMessage2 = CustomTextMessage("customtext2")
        val emergencyMessageSetup2 = EmergencyMessageSetup("Title2", keyphrase2, customTextMessage2)
        val keyphrase3 = KeyPhrase("keyphrase3")
        val customTextMessage3 = CustomTextMessage("customtext3")
        val emergencyMessageSetup3 = EmergencyMessageSetup("Title3", keyphrase3, customTextMessage3)

        Passing.emergencyMessageSetupList.addEmergencyMessageSetup(emergencyMessageSetup1)
        Passing.emergencyMessageSetupList.addEmergencyMessageSetup(emergencyMessageSetup2)
        Passing.emergencyMessageSetupList.addEmergencyMessageSetup(emergencyMessageSetup3)

//        val emergencyMessageSetupAdapter = EmergencyMessageSetupAdapter(Passing.emergencyMessageSetupList.emergencyMessageSetups, this)
//
//        recyclerViewEmergencyMessageSetup.layoutManager = LinearLayoutManager(this)
//        recyclerViewEmergencyMessageSetup.adapter = emergencyMessageSetupAdapter

        buttonAdd = findViewById<View>(R.id.buttonAdd) as Button

        buttonEdit = findViewById<View>(R.id.buttonEdit) as Button

        buttonDelete = findViewById<View>(R.id.buttonDelete) as Button

        refreshList()
    }

    private fun refreshList() {
        val emergencyMessageSetupAdapter = EmergencyMessageSetupAdapter(
            Passing.emergencyMessageSetupList.emergencyMessageSetups, this)

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

    fun checkSelectForDelete(viewSelectedBoolean: Boolean, titleSelectedString: String) {
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

    override fun editEmergencyMessageSetup(customTextString: String) {
        TODO("Not yet implemented")
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

    override fun addEmergencyMessageSetup(customTextString: String) {
        if (customTextString.isEmpty()) {
            Toast.makeText(
                this@EmergencyMessageSetupActivity,
                "Please enter a key phrase",
                Toast.LENGTH_SHORT
            ).show()
            openPopUp(titleSelectedString, "add")
        } else if (customTextString.isNotEmpty() &&
            Passing.emergencyMessageSetupList.addEmergencyMessageSetup(
                    EmergencyMessageSetup(
                            customTextString,
                            KeyPhrase("null"),
                            CustomTextMessage("null")
                        )
                )
        ) {
            Toast.makeText(
                this@EmergencyMessageSetupActivity,
                "New Key Phrase Successfully " +
                    "Added",
                Toast.LENGTH_SHORT
            ).show()
            Passing.selectedEmergencyMessageSetup =
                Passing.emergencyMessageSetupList.findEmergencyMessageSetup(customTextString)
            refreshList()
        } else {
            Toast.makeText(
                this@EmergencyMessageSetupActivity,
                "That Key Phrase already exists. Try something else or click cancel",
                Toast.LENGTH_SHORT
            ).show()
            openPopUp(titleSelectedString, "add")
        }
    }

    override fun goToEditPage() {
        startActivity(Intent(this, EditEmergencyMessageActivity()::class.java))
    }
}