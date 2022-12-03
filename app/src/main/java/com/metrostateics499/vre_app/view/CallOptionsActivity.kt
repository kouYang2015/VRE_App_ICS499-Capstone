package com.metrostateics499.vre_app.view

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.metrostateics499.vre_app.R
import com.metrostateics499.vre_app.model.Passing
import com.metrostateics499.vre_app.model.data.CallMessage
import com.metrostateics499.vre_app.utility.CallMessagePopUps
import com.metrostateics499.vre_app.view.adapters.CallMessageAdapter
import java.util.*
import kotlinx.android.synthetic.main.activity_call_message_menu.*
import kotlinx.android.synthetic.main.activity_contacts.*
import kotlinx.android.synthetic.main.activity_custom_text_message_menu.*

class CallOptionsActivity :
    AppCompatActivity(),
    TextToSpeech.OnInitListener,
    CallMessagePopUps.Listener {

    private var buttonAdd: Button? = null
    private var buttonEdit: Button? = null
    private var buttonDelete: Button? = null
    private var buttonListenToMessage: Button? = null
    private var buttonViewEntireText: Button? = null
    private var textViewSelected: String = ""
    private var textViewSelectedBoolean: Boolean = false
    private var viewSelected: View? = null
    private var textToSpeech: TextToSpeech? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_call_message_menu)

        buttonAdd = findViewById<View>(R.id.buttonAdd) as Button
        buttonEdit = findViewById<View>(R.id.buttonEdit) as Button
        buttonDelete = findViewById<View>(R.id.buttonDelete) as Button

        // view binding button and edit text
        buttonListenToMessage = findViewById<View>(R.id.buttonListenToMessage) as Button
//        etSpeak = findViewById(R.id.et_input)

        buttonListenToMessage!!.isEnabled = false

        // TextToSpeech(Context: this, OnInitListener: this)
        textToSpeech = TextToSpeech(this, this)

        refreshList()
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = textToSpeech!!.setLanguage(Locale.US)

            if (result == TextToSpeech.LANG_MISSING_DATA ||
                result == TextToSpeech.LANG_NOT_SUPPORTED
            ) {
                Log.e("TTS", "The Language not supported!")
            } else {
                buttonListenToMessage!!.isEnabled = true
            }
        }
    }
    private fun speakOut() {
        val text = Passing.selectedEmergencyMessageSetup.getCallMessageListString()
        textToSpeech!!.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }

    public override fun onDestroy() {
        // Shutdown TTS when
        // activity is destroyed
        if (textToSpeech != null) {
            textToSpeech!!.stop()
            textToSpeech!!.shutdown()
        }
        super.onDestroy()
    }

    private fun refreshList() {
        val callMessageAdapter = CallMessageAdapter(
            Passing.callMessageList, this
        )

        recyclerViewCallMessage.layoutManager = LinearLayoutManager(this)
        recyclerViewCallMessage.adapter = callMessageAdapter

        buttonAdd!!.setOnClickListener {
            openPopUp(textViewSelected, "add")
        }
        buttonEdit!!.setOnClickListener {
            textViewSelected = callMessageAdapter.titleSelectedString
            textViewSelectedBoolean = callMessageAdapter.viewSelectedBoolean
            viewSelected = callMessageAdapter.viewSelected
            checkSelectForEdit(textViewSelectedBoolean, textViewSelected)
        }
        buttonDelete!!.setOnClickListener {
            textViewSelected = callMessageAdapter.titleSelectedString
            textViewSelectedBoolean = callMessageAdapter.viewSelectedBoolean
            viewSelected = callMessageAdapter.viewSelected
            checkSelectForDelete(textViewSelectedBoolean, textViewSelected)
        }
        buttonListenToMessage!!.setOnClickListener {
            speakOut()
            openPopUp("null", "listenEntireCallMessage")
        }
    }

    private fun checkSelectForDelete(textViewSelectedBoolean: Boolean, textViewSelected: String) {
        if (textViewSelectedBoolean) {
            openPopUp(textViewSelected, "delete")
        } else {
            Toast.makeText(
                this@CallOptionsActivity, "You must select a call message first",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun checkSelectForEdit(textViewSelectedBoolean: Boolean, textViewSelected: String) {
        if (textViewSelectedBoolean) {
            openPopUp(textViewSelected, "edit")
        } else {
            Toast.makeText(
                this@CallOptionsActivity, "You must select a call message first",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun openPopUp(textViewSelected: String, buttonType: String) {
        val callMessagePopUp = CallMessagePopUps(textViewSelected, buttonType)
        callMessagePopUp.show(supportFragmentManager, "example dialog")
    }

    override fun addCallMessage(callMessageTitle: String, callMessageString: String) {
        if (callMessageString.isEmpty() || callMessageTitle.isEmpty()) {
            Toast.makeText(
                this@CallOptionsActivity,
                "Please enter both field(s)",
                Toast.LENGTH_SHORT
            ).show()
            openPopUp(textViewSelected, "add")
        } else {
            Passing.callMessageList.add(
                CallMessage(
                    callMessageTitle,
                    callMessageString
                )
            )
            Toast.makeText(
                this@CallOptionsActivity,
                "New Custom Text Message Successfully " +
                    "Added",
                Toast.LENGTH_SHORT
            ).show()
            refreshList()
        }
    }

    override fun editCallMessage(callMessageTitle: String, callMessageString: String) {
        if (callMessageString.trim().isEmpty() || callMessageTitle.trim().isEmpty()) {
            Toast.makeText(
                this@CallOptionsActivity,
                "field(s) can't be empty",
                Toast.LENGTH_SHORT
            ).show()
            openPopUp(textViewSelected, "edit")
        } else if (callMessageString == textViewSelected &&
            callMessageTitle == Passing.selectedCallMessageObject.title
        ) {
            Toast.makeText(
                this@CallOptionsActivity, "Make a change or click cancel",
                Toast.LENGTH_SHORT
            ).show()
            openPopUp(textViewSelected, "edit")
        } else if (callMessageString.trim().isNotEmpty() && callMessageTitle.isNotEmpty()) {
            Passing.selectedCallMessageObject.title = callMessageTitle
            Passing.selectedCallMessageObject.callMessage = callMessageString
            Toast.makeText(
                this@CallOptionsActivity, "Successfully Edited",
                Toast.LENGTH_SHORT
            ).show()
            refreshList()
        } else {
            Toast.makeText(
                this@CallOptionsActivity,
                "That call message already exists. " +
                    "Try something else or click cancel.",
                Toast.LENGTH_SHORT
            ).show()
            openPopUp(textViewSelected, "edit")
        }
    }

    override fun deleteCallMessage(callMessageString: String) {
        if (textViewSelected.isNotEmpty()) {
            Passing.callMessageList.remove(Passing.selectedCallMessageObject)
            Toast.makeText(
                this@CallOptionsActivity,
                "You have deleted call message: " +
                    textViewSelected,
                Toast.LENGTH_SHORT
            ).show()
            for (item in Passing.emergencyMessageSetupList) {
                Passing.selectedCallMessageObject.let {
                    item.selectedCallMessages.remove(it)
                }
            }
            refreshList()
        }
    }
}
