package com.metrostateics499.vre_app.view

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.metrostateics499.vre_app.R
import com.metrostateics499.vre_app.model.Passing
import com.metrostateics499.vre_app.utility.EditEmergencyMessagePopUps
import kotlinx.android.synthetic.main.activity_edit_emergency_message.*
import kotlinx.android.synthetic.main.activity_edit_emergency_message.view.*
import kotlinx.android.synthetic.main.row.view.*

class EditEmergencyMessageActivity : AppCompatActivity(), EditEmergencyMessagePopUps.Listener {

    private var textViewSelected: String = ""
//    private var emergencyMessageSelected = Passing.selectedEmergencyMessageSetup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_emergency_message)

        refreshRelativeLayout()
        refreshRelativeLayout2()
        refreshRelativeLayout3()
        refreshRelativeLayout4()
        refreshRelativeLayout6()

        val relativeLayout: RelativeLayout = findViewById(R.id.relativeLayout)
        val relativeLayout2: RelativeLayout = findViewById(R.id.relativeLayout2)
        val relativeLayout3: RelativeLayout = findViewById(R.id.relativeLayout3)
        val relativeLayout4: RelativeLayout = findViewById(R.id.relativeLayout4)

        relativeLayout.setOnClickListener {
            textViewSelected = Passing.selectedEmergencyMessageSetup.title
            openPopUp("title")
        }

        relativeLayout2.setOnClickListener {
//            textViewSelected = Passing.selectedEmergencyMessageSetup.keyPhrase.phrase
//            openPopUp("keyphrase")
            goToKeyPhraseMenu()
        }

        relativeLayout3.setOnClickListener {
//            textViewSelected = Passing.selectedEmergencyMessageSetup.keyPhrase.phrase
//            openPopUp("keyphrase")
            goToCustomTextMenu()
        }

//        relativeLayout3.setOnClickListener {
//            textViewSelected = Passing.selectedEmergencyMessageSetup.customTextMessage.toString()
//            openPopUp("customTextMessage")
//        }

        relativeLayout4.setOnClickListener {
//            textViewSelected = Passing.selectedEmergencyMessageSetup.customTextMessage.toString()
            goToContactsMenu()
        }

        relativeLayout6.switch5.setOnClickListener {
            if (switch5.isChecked &&
                Passing.selectedEmergencyMessageSetup.selectedContactList.isNotEmpty() &&
                Passing.selectedEmergencyMessageSetup.selectedKeyPhraseList.isNotEmpty()
            ) {
                textViewActive.setTextColor(Color.parseColor("#1BB100"))
                textViewActive.text = "Active"
                Passing.selectedEmergencyMessageSetup.activeEMS = true
                Toast.makeText(
                    this@EditEmergencyMessageActivity,
                    "You have activated VRE service for this EMS",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (switch5.isChecked &&
                Passing.selectedEmergencyMessageSetup.selectedContactList.isEmpty() &&
                Passing.selectedEmergencyMessageSetup.selectedKeyPhraseList.isNotEmpty()
            ) {
                switch5.isChecked = false
                Toast.makeText(
                    this@EditEmergencyMessageActivity,
                    "You must add a contact in order to activate",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (switch5.isChecked &&
                Passing.selectedEmergencyMessageSetup.selectedContactList.isNotEmpty() &&
                Passing.selectedEmergencyMessageSetup.selectedKeyPhraseList.isEmpty()
            ) {
                switch5.isChecked = false
                Toast.makeText(
                    this@EditEmergencyMessageActivity,
                    "You must add a key phrase in order to activate",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (switch5.isChecked &&
                Passing.selectedEmergencyMessageSetup.selectedContactList.isEmpty() &&
                Passing.selectedEmergencyMessageSetup.selectedKeyPhraseList.isEmpty()
            ) {
                switch5.isChecked = false
                Toast.makeText(
                    this@EditEmergencyMessageActivity,
                    "You must add a key phrase and a contact in order to activate",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                textViewActive.setTextColor(Color.parseColor("#B50909"))
                textViewActive.text = "Inactive"
                Passing.selectedEmergencyMessageSetup.activeEMS = false
                Toast.makeText(
                    this@EditEmergencyMessageActivity,
                    "You have deactivated VRE service for this EMS",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun goToKeyPhraseMenu() {
        startActivity(Intent(this, KeyPhraseActivity::class.java))
    }

    private fun goToContactsMenu() {
        startActivity(Intent(this, ContactActivity::class.java))
    }

    private fun goToCustomTextMenu() {
        startActivity(Intent(this, CustomTextActivity::class.java))
    }

    private fun refreshRelativeLayout() {
        val textView: TextView = findViewById(R.id.text_view_em)
        textView.text = (Passing.selectedEmergencyMessageSetup.title)
    }
    private fun refreshRelativeLayout2() {
        if (Passing.selectedEmergencyMessageSetup.selectedKeyPhraseList.isNotEmpty()) {
            val textView2: TextView = findViewById(R.id.text_view_keyphrase)
            text_view_keyphrase_required.text = ""
            textView2.text =
                (
                    Passing.selectedEmergencyMessageSetup.getKeyPhraseListString()
                    )
        } else if (Passing.selectedEmergencyMessageSetup.selectedKeyPhraseList.isEmpty()) {
            val textView2: TextView = findViewById(R.id.text_view_keyphrase)
            textView2.text = "Choose or Create Key Phrase(s)"
            text_view_keyphrase_required.text = "*"
        }

//        val textView2: TextView = findViewById(R.id.text_view_keyphrase)
//        if (Passing.selectedEmergencyMessageSetup.keyPhrase.phrase.isNotEmpty()) {
//            textView2.text = (
//                (
//                    Passing.selectedEmergencyMessageSetup.keyPhrase.phrase
//                    )
//                )
//        }
    }

    private fun refreshRelativeLayout3() {
        if (Passing.selectedEmergencyMessageSetup.selectedCustomTextMessages
            .isNotEmpty()
        ) {
            val textView3: TextView = findViewById(R.id.text_custom_text)
            textView3.text =
                (
                    Passing.selectedEmergencyMessageSetup.getCustomTextListString()
                    )
        } else if (Passing.selectedEmergencyMessageSetup.selectedCustomTextMessages.isEmpty()) {
            val textView3: TextView = findViewById(R.id.text_custom_text)
            textView3.text = "Choose or Create Custom Text Messages"
        }
    }

    private fun refreshRelativeLayout4() {
        if (Passing.selectedEmergencyMessageSetup.selectedContactList.isNotEmpty()) {
            text_view_contact_required.text = ""
            val textView4: TextView = findViewById(R.id.text_contact_list)
            textView4.text =
                (
                    Passing.selectedEmergencyMessageSetup.getContactListNames()
                    )
        } else if (Passing.selectedEmergencyMessageSetup.selectedContactList.isEmpty()) {
            val textView4: TextView = findViewById(R.id.text_contact_list)
            textView4.text = "Choose or Create Contacts"
            text_view_contact_required.text = "*"
        }
    }

    private fun refreshRelativeLayout6() {
        if (Passing.selectedEmergencyMessageSetup.activeEMS &&
            Passing.selectedEmergencyMessageSetup.selectedKeyPhraseList.isNotEmpty() &&
            Passing.selectedEmergencyMessageSetup.selectedContactList.isNotEmpty()
        ) {
            switch5.isChecked = true
            textViewActive.setTextColor(Color.parseColor("#1BB100"))
            textViewActive.text = "Active"
        } else {
            Passing.selectedEmergencyMessageSetup.activeEMS = false
            switch5.isChecked = false
            textViewActive.setTextColor(Color.parseColor("#B50909"))
            textViewActive.text = "Inactive"
        }
    }

    private fun openPopUp(buttonType: String) {
        val editEmergencyMessagePopUps = EditEmergencyMessagePopUps(buttonType)
        editEmergencyMessagePopUps.show(supportFragmentManager, "example dialog")
    }

    override fun editEmergencyMessageSetupTitle(inputTitle: String) {
        if (inputTitle.isEmpty()) {
            Toast.makeText(
                this@EditEmergencyMessageActivity,
                "Key phrase can't be empty",
                Toast.LENGTH_SHORT
            ).show()
            openPopUp("title")
        } else if (inputTitle == textViewSelected) {
            Toast.makeText(
                this@EditEmergencyMessageActivity, "Make a change or click cancel",
                Toast.LENGTH_SHORT
            ).show()
            openPopUp("title")
        } else if (inputTitle.isNotEmpty() &&
            checkTitleUniqueness(inputTitle)
        ) {
            Passing.selectedEmergencyMessageSetup.title = inputTitle
            Toast.makeText(
                this@EditEmergencyMessageActivity, "Successfully Edited",
                Toast.LENGTH_SHORT
            ).show()
            refreshRelativeLayout()
        } else {
            Toast.makeText(
                this@EditEmergencyMessageActivity,
                "That Key Phrase already exists. " +
                    "Try something else or click cancel.",
                Toast.LENGTH_SHORT
            ).show()
            openPopUp("title")
        }
    }

//    override fun editEmergencyMessageSetupKeyPhrase(inputPhrase: String) {
//        if (inputPhrase.isEmpty()) {
//            Toast.makeText(
//                this@EditEmergencyMessageActivity,
//                "Key phrase can't be empty",
//                Toast.LENGTH_SHORT
//            ).show()
//            openPopUp("keyphrase")
//        } else if (inputPhrase == textViewSelected) {
//            Toast.makeText(
//                this@EditEmergencyMessageActivity, "Make a change or click cancel",
//                Toast.LENGTH_SHORT
//            ).show()
//            openPopUp("keyphrase")
//        } else if (inputPhrase.isNotEmpty() &&
//            checkKeyPhraseUniqueness(inputPhrase)
//        ) {
//            Passing.selectedEmergencyMessageSetup.keyPhrase.phrase = inputPhrase
//            Toast.makeText(
//                this@EditEmergencyMessageActivity, "Successfully Edited",
//                Toast.LENGTH_SHORT
//            ).show()
//            refreshRelativeLayout2()
//        } else {
//            Toast.makeText(
//                this@EditEmergencyMessageActivity,
//                "That Key Phrase already exists. " +
//                    "Try something else or click cancel.",
//                Toast.LENGTH_SHORT
//            ).show()
//            openPopUp("keyphrase")
//        }
//    }

//    override fun editEmergencyMessageSetupCustomTextMessage(inputText: String) {
//        if (inputText.isEmpty()) {
//            Toast.makeText(
//                this@EditEmergencyMessageActivity,
//                "Custom text message can't be empty",
//                Toast.LENGTH_SHORT
//            ).show()
//            openPopUp("customTextMessage")
//        } else if (inputText == textViewSelected) {
//            Toast.makeText(
//                this@EditEmergencyMessageActivity, "Make a change or click cancel",
//                Toast.LENGTH_SHORT
//            ).show()
//            openPopUp("customTextMessage")
//        } else if (inputText.isNotEmpty()) {
//            Passing.selectedEmergencyMessageSetup.customTextMessage.textMessage = inputText
//            Toast.makeText(
//                this@EditEmergencyMessageActivity, "Successfully Edited",
//                Toast.LENGTH_SHORT
//            ).show()
//            refreshRelativeLayout3()
//        } else {
//            Toast.makeText(
//                this@EditEmergencyMessageActivity,
//                "That custom text message already exists. " +
//                    "Try something else or click cancel.",
//                Toast.LENGTH_SHORT
//            ).show()
//            openPopUp("customTextMessage")
//        }
//    }
    override fun onPostResume() {
        super.onPostResume()
        refreshRelativeLayout2()
        refreshRelativeLayout3()
        refreshRelativeLayout4()
        refreshRelativeLayout6()
    }

    private fun checkTitleUniqueness(title: String): Boolean {
        for (item in Passing.emergencyMessageSetupList) {
            if (item.title.equals(title, true)) {
                return false
            }
        }
        return true
    }
//    private fun checkKeyPhraseUniqueness(phrase: String): Boolean {
//        for (item in Passing.emergencyMessageSetupList) {
//            if (item.keyPhrase.phrase.equals(phrase, true)) {
//                return false
//            }
//        }
//        return true
//    }
}