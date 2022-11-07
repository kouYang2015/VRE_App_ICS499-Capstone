package com.metrostateics499.vre_app

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_edit_emergency_message.*
import kotlinx.android.synthetic.main.activity_emergency_message_setup_menu.*
import kotlinx.android.synthetic.main.activity_main.*

class EditEmergencyMessageActivity() : AppCompatActivity(), EditEmergencyMessagePopUps.Listener {
    private var buttonAdd: Button? = null
    private var buttonEdit: Button? = null
    private var buttonDelete: Button? = null

    private var textViewSelected: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_emergency_message)

        buttonAdd = findViewById<View>(R.id.buttonAdd) as Button
        buttonEdit = findViewById<View>(R.id.buttonEdit) as Button
        buttonDelete = findViewById<View>(R.id.buttonDelete) as Button

//        val view = inflater.inflate(R.layout.activity_edit_emergency_message, null)
        val textView: TextView = findViewById(R.id.text_view_em)
        textView.text = (Passing.selectedEmergencyMessageSetup?.title ?: String) as CharSequence?

        val textView2: TextView = findViewById(R.id.text_view_keyphrase)
        textView2.text = (
            (
                Passing.selectedEmergencyMessageSetup?.keyPhrase?.keyPhrase
                    ?: String
                ) as CharSequence?
            )

        val textView3: TextView = findViewById(R.id.text_custom_text)
        textView3.text =
            (
            Passing.selectedEmergencyMessageSetup?.customTextMessage?.customTextMessage
                ?: String
            ) as CharSequence?

        val relativeLayout: RelativeLayout = findViewById(R.id.relativeLayout)
        val relativeLayout2: RelativeLayout = findViewById(R.id.relativeLayout2)
        val relativeLayout3: RelativeLayout = findViewById(R.id.relativeLayout3)

        relativeLayout.setOnClickListener {
            relativeLayout.setBackgroundResource(
                androidx.appcompat.R.drawable
                    .abc_item_background_holo_light
            )
            textViewSelected = (Passing.selectedEmergencyMessageSetup?.title ?: String) as String?
            openPopUp("title")
        }

        relativeLayout2.setOnClickListener {
            relativeLayout2.setBackgroundResource(
                androidx.appcompat.R.drawable
                    .abc_item_background_holo_light
            )
            openPopUp("keyphrase")
        }
        relativeLayout3.setOnClickListener {
            relativeLayout3.setBackgroundResource(
                androidx.appcompat.R.drawable
                    .abc_item_background_holo_light
            )
        }

//        buttonEdit!!.setOnClickListener {
//            when (){
//
//            }
//        }
    }

    fun refreshRelativeLayout() {
        val textView: TextView = findViewById(R.id.text_view_em)
        textView.text = (Passing.selectedEmergencyMessageSetup?.title ?: String) as CharSequence?
    }

    private fun openPopUp(buttonType: String) {
        val editEmergencyMessagePopUps = EditEmergencyMessagePopUps(buttonType)
        editEmergencyMessagePopUps.show(supportFragmentManager, "example dialog")
    }

    override fun editEmergencyMessageSetup(customTextString: String) {
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
            openPopUp("edit")
        } else if (customTextString.isNotEmpty() &&
            Passing.emergencyMessageSetupList.editEmergencyMessageSetupTitle(
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
            openPopUp("edit")
        }
    }

    override fun deleteEmergencyMessageSetup(customTextString: String) {
        TODO("Not yet implemented")
    }

    override fun addEmergencyMessageSetup(customTextString: String) {
        TODO("Not yet implemented")
    }

    override fun goToEditPage() {
        TODO("Not yet implemented")
    }
}