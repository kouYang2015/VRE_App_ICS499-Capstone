package com.metrostateics499.vre_app.view

import android.os.Bundle
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.metrostateics499.vre_app.R
import com.metrostateics499.vre_app.model.Passing

class EditEmergencyMessageActivity() : AppCompatActivity(), EditEmergencyMessagePopUps.Listener {

    private var textViewSelected: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_emergency_message)

        refreshRelativeLayout()
        refreshRelativeLayout2()
        refreshRelativeLayout3()

        val relativeLayout: RelativeLayout = findViewById(R.id.relativeLayout)
        val relativeLayout2: RelativeLayout = findViewById(R.id.relativeLayout2)
        val relativeLayout3: RelativeLayout = findViewById(R.id.relativeLayout3)

        relativeLayout.setOnClickListener {
//            relativeLayout.setBackgroundResource(
//                androidx.appcompat.R.drawable
//                    .abc_item_background_holo_light
//            )
            textViewSelected = Passing.selectedEmergencyMessageSetup?.title ?: toString()
            openPopUp("title")
        }

        relativeLayout2.setOnClickListener {
//            relativeLayout2.setBackgroundResource(
//                androidx.appcompat.R.drawable
//                    .abc_item_background_holo_light
//            )
            textViewSelected = Passing.selectedEmergencyMessageSetup?.keyPhrase?.keyPhrase
                ?: toString()
            openPopUp("keyphrase")
        }
        relativeLayout3.setOnClickListener {
//            relativeLayout3.setBackgroundResource(
//                androidx.appcompat.R.drawable
//                    .abc_item_background_holo_light
//            )
            textViewSelected = Passing.selectedEmergencyMessageSetup?.customTextMessage.toString()
            openPopUp("customTextMessage")
        }

//        buttonEdit!!.setOnClickListener {
//            when (){
//
//            }
//        }
    }

    private fun refreshRelativeLayout() {
        val textView: TextView = findViewById(R.id.text_view_em)
        textView.text = (Passing.selectedEmergencyMessageSetup?.title ?: String) as CharSequence?
    }
    private fun refreshRelativeLayout2() {
        val textView2: TextView = findViewById(R.id.text_view_keyphrase)
        if (Passing.selectedEmergencyMessageSetup?.keyPhrase?.keyPhrase?.isNotEmpty() == true) {
            textView2.text = (
                (
                    Passing.selectedEmergencyMessageSetup?.keyPhrase?.keyPhrase
                        ?: toString()
                    )
                )
        }
    }

    private fun refreshRelativeLayout3() {
        if (Passing.selectedEmergencyMessageSetup?.customTextMessage?.customTextMessage
            ?.isNotEmpty() == true
        ) {
            val textView3: TextView = findViewById(R.id.text_custom_text)
            textView3.text =
                (
                Passing.selectedEmergencyMessageSetup?.customTextMessage?.customTextMessage
                    ?: String
                ) as CharSequence?
        }
    }

    private fun openPopUp(buttonType: String) {
        val editEmergencyMessagePopUps = EditEmergencyMessagePopUps(buttonType)
        editEmergencyMessagePopUps.show(supportFragmentManager, "example dialog")
    }

    override fun editEmergencyMessageSetupTitle(customTextString: String) {
        if (customTextString.isEmpty()) {
            Toast.makeText(
                this@EditEmergencyMessageActivity,
                "Key phrase can't be empty",
                Toast.LENGTH_SHORT
            ).show()
            openPopUp("title")
        } else if (customTextString == textViewSelected) {
            Toast.makeText(
                this@EditEmergencyMessageActivity, "Make a change or click cancel",
                Toast.LENGTH_SHORT
            ).show()
            openPopUp("title")
        } else if (customTextString.isNotEmpty() &&
            Passing.emergencyMessageSetupList.editEmergencyMessageSetupTitle(
                    textViewSelected,
                    customTextString
                )
        ) {
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

    override fun editEmergencyMessageSetupKeyPhrase(customTextString: String) {
        if (customTextString.isEmpty()) {
            Toast.makeText(
                this@EditEmergencyMessageActivity,
                "Key phrase can't be empty",
                Toast.LENGTH_SHORT
            ).show()
            openPopUp("keyphrase")
        } else if (customTextString == textViewSelected) {
            Toast.makeText(
                this@EditEmergencyMessageActivity, "Make a change or click cancel",
                Toast.LENGTH_SHORT
            ).show()
            openPopUp("keyphrase")
        } else if (customTextString.isNotEmpty() &&
            Passing.emergencyMessageSetupList.editEmergencyMessageSetupKeyPhrase(
                    textViewSelected,
                    customTextString
                )
        ) {
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

    override fun editEmergencyMessageSetupCustomTextMessage(customTextString: String) {
        if (customTextString.isEmpty()) {
            Toast.makeText(
                this@EditEmergencyMessageActivity,
                "Custom text message can't be empty",
                Toast.LENGTH_SHORT
            ).show()
            openPopUp("customTextMessage")
        } else if (customTextString == textViewSelected) {
            Toast.makeText(
                this@EditEmergencyMessageActivity, "Make a change or click cancel",
                Toast.LENGTH_SHORT
            ).show()
            openPopUp("customTextMessage")
        } else if (customTextString.isNotEmpty() &&
            Passing.emergencyMessageSetupList.editEmergencyMessageSetupCustomTextMessage(
                    textViewSelected,
                    customTextString
                )
        ) {
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
}