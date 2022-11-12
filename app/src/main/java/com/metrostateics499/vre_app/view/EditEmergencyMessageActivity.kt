package com.metrostateics499.vre_app.view

import android.content.Intent
import android.os.Bundle
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.metrostateics499.vre_app.R
import com.metrostateics499.vre_app.model.Passing
import com.metrostateics499.vre_app.utility.EditEmergencyMessagePopUps

class EditEmergencyMessageActivity() : AppCompatActivity(), EditEmergencyMessagePopUps.Listener {

    private var textViewSelected: String = ""
//    private var emergencyMessageSelected = Passing.selectedEmergencyMessageSetup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_emergency_message)

        refreshRelativeLayout()
        refreshRelativeLayout2()
        refreshRelativeLayout3()
        refreshRelativeLayout4()

        val relativeLayout: RelativeLayout = findViewById(R.id.relativeLayout)
        val relativeLayout2: RelativeLayout = findViewById(R.id.relativeLayout2)
        val relativeLayout3: RelativeLayout = findViewById(R.id.relativeLayout3)
        val relativeLayout4: RelativeLayout = findViewById(R.id.relativeLayout4)

        relativeLayout.setOnClickListener {
//            relativeLayout.setBackgroundResource(
//                androidx.appcompat.R.drawable
//                    .abc_item_background_holo_light
//            )
            textViewSelected = Passing.selectedEmergencyMessageSetup.title
            openPopUp("title")
        }

        relativeLayout2.setOnClickListener {
//            relativeLayout2.setBackgroundResource(
//                androidx.appcompat.R.drawable
//                    .abc_item_background_holo_light
//            )
            textViewSelected = Passing.selectedEmergencyMessageSetup.keyPhrase.phrase
            openPopUp("keyphrase")
        }
        relativeLayout3.setOnClickListener {
//            relativeLayout3.setBackgroundResource(
//                androidx.appcompat.R.drawable
//                    .abc_item_background_holo_light
//            )
            textViewSelected = Passing.selectedEmergencyMessageSetup.customTextMessage.toString()
            openPopUp("customTextMessage")
        }
        relativeLayout4.setOnClickListener {
//            relativeLayout3.setBackgroundResource(
//                androidx.appcompat.R.drawable
//                    .abc_item_background_holo_light
//            )
            textViewSelected = Passing.selectedEmergencyMessageSetup.customTextMessage.toString()
            goToContactsMenu()
        }
    }
    private fun goToContactsMenu() {
        startActivity(Intent(this, ContactActivity::class.java))
    }

    private fun refreshRelativeLayout() {
        val textView: TextView = findViewById(R.id.text_view_em)
        textView.text = (Passing.selectedEmergencyMessageSetup.title)
    }
    private fun refreshRelativeLayout2() {
        val textView2: TextView = findViewById(R.id.text_view_keyphrase)
        if (Passing.selectedEmergencyMessageSetup.keyPhrase.phrase.isNotEmpty()) {
            textView2.text = (
                (
                    Passing.selectedEmergencyMessageSetup.keyPhrase.phrase
                    )
                )
        }
    }

    private fun refreshRelativeLayout3() {
        if (Passing.selectedEmergencyMessageSetup.customTextMessage.textMessage
            .isNotEmpty()
        ) {
            val textView3: TextView = findViewById(R.id.text_custom_text)
            textView3.text =
                (
                Passing.selectedEmergencyMessageSetup.customTextMessage.textMessage
                )
        }
    }

    private fun refreshRelativeLayout4() {
        if (Passing.selectedEmergencyMessageSetup.selectedContactList.isNotEmpty())
        {
            val textView4: TextView = findViewById(R.id.text_contact_list)
            textView4.text =
                (
                    Passing.selectedEmergencyMessageSetup.getContactListNames()
                    )
        } else if (Passing.selectedEmergencyMessageSetup.selectedContactList.isEmpty())
        {
            val textView4: TextView = findViewById(R.id.text_contact_list)
            textView4.text = "Select Contact(s)"
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
        } else if (inputTitle.isNotEmpty()
            && checkTitleUniqueness(inputTitle))
        {
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

    override fun editEmergencyMessageSetupKeyPhrase(inputPhrase: String) {
        if (inputPhrase.isEmpty()) {
            Toast.makeText(
                this@EditEmergencyMessageActivity,
                "Key phrase can't be empty",
                Toast.LENGTH_SHORT
            ).show()
            openPopUp("keyphrase")
        } else if (inputPhrase == textViewSelected) {
            Toast.makeText(
                this@EditEmergencyMessageActivity, "Make a change or click cancel",
                Toast.LENGTH_SHORT
            ).show()
            openPopUp("keyphrase")
        } else if (inputPhrase.isNotEmpty()
            && checkKeyPhraseUniqueness(inputPhrase))
        {
            Passing.selectedEmergencyMessageSetup.keyPhrase.phrase = inputPhrase
            Toast.makeText(
                this@EditEmergencyMessageActivity, "Successfully Edited",
                Toast.LENGTH_SHORT
            ).show()
            refreshRelativeLayout2()
        } else {
            Toast.makeText(
                this@EditEmergencyMessageActivity,
                "That Key Phrase already exists. " +
                    "Try something else or click cancel.",
                Toast.LENGTH_SHORT
            ).show()
            openPopUp("keyphrase")
        }
    }

    override fun editEmergencyMessageSetupCustomTextMessage(inputText: String) {
        if (inputText.isEmpty()) {
            Toast.makeText(
                this@EditEmergencyMessageActivity,
                "Custom text message can't be empty",
                Toast.LENGTH_SHORT
            ).show()
            openPopUp("customTextMessage")
        } else if (inputText == textViewSelected) {
            Toast.makeText(
                this@EditEmergencyMessageActivity, "Make a change or click cancel",
                Toast.LENGTH_SHORT
            ).show()
            openPopUp("customTextMessage")
        } else if (inputText.isNotEmpty())
        {
            Passing.selectedEmergencyMessageSetup.customTextMessage.textMessage = inputText
            Toast.makeText(
                this@EditEmergencyMessageActivity, "Successfully Edited",
                Toast.LENGTH_SHORT
            ).show()
            refreshRelativeLayout3()
        } else {
            Toast.makeText(
                this@EditEmergencyMessageActivity,
                "That custom text message already exists. " +
                    "Try something else or click cancel.",
                Toast.LENGTH_SHORT
            ).show()
            openPopUp("customTextMessage")
        }
    }
    override fun onPostResume() {
        super.onPostResume()
        refreshRelativeLayout4()
    }

    private fun checkTitleUniqueness(title: String): Boolean {
        for (item in Passing.emergencyMessageSetupList) {
            if (item.title.equals(title, true)) {
                return false
            }
        }
        return true
    }
    private fun checkKeyPhraseUniqueness(phrase: String): Boolean {
        for (item in Passing.emergencyMessageSetupList) {
            if (item.keyPhrase.phrase.equals(phrase, true)) {
                return false
            }
        }
        return true
    }
}