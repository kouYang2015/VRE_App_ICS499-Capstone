package com.metrostateics499.vre_app.view

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.metrostateics499.vre_app.R
import com.metrostateics499.vre_app.model.Passing
import com.metrostateics499.vre_app.model.data.KeyPhrase
import com.metrostateics499.vre_app.utility.KeyPhrasePopUps
import com.metrostateics499.vre_app.utility.TestListenKeyPhrasePopUp
import com.metrostateics499.vre_app.view.adapters.KeyPhraseAdapter
import java.util.*
import kotlinx.android.synthetic.main.activity_key_phrases_menu.*

/**
 * Key words activity
 * This class is the Activity for the user to interact with the key phrases menu
 * It also initiates functions to add and remove key phrases
 *
 * @constructor Create empty Key words activity
 */
class KeyPhraseActivity : AppCompatActivity(), KeyPhrasePopUps.Listener {

    private var buttonAdd: Button? = null
    private var buttonEdit: Button? = null
    private var buttonDelete: Button? = null
    private var buttonTest: Button? = null

    private var textViewSelected: String = ""
    private var viewSelectedBoolean: Boolean = false
    private var viewSelected: View? = null

    private lateinit var speechRecognizer: SpeechRecognizer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_key_phrases_menu)

        initializeComponents()
        refreshList()
        setListeners()
    }

    private fun initializeComponents() {
        buttonAdd = findViewById<View>(R.id.buttonAddKeyPhrase) as Button
        buttonEdit = findViewById<View>(R.id.buttonEditKeyPhrase) as Button
        buttonDelete = findViewById<View>(R.id.buttonDeleteKeyPhrase) as Button
        buttonTest = findViewById<View>(R.id.buttonTest) as Button
//        recordAudioPermissionRequest =
//            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
//            }
//        speechOnButton = findViewById(R.id.activateSpeech)
//        speechOffButton = findViewById(R.id.disableSpeech)
//        txtResult = findViewById(R.id.speechToTextBox)
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
//        requestSmsPermission()
        requestRecordAudioPermission()
    }

//    private fun refreshList() {
//        textViewSelectedBoolean = false
//        textViewSelected = ""
//
//        val listview: ListView = findViewById(R.id.listViewPhrases)
//        val arrayAdapter = ArrayAdapter(
//            this, android.R.layout.simple_list_item_1, Passing.keyPhraseList
//        )
//        listview.adapter = arrayAdapter
//        listview.setOnItemClickListener { parent, view, position, _ ->
//            viewSelected?.setBackgroundResource(
//                androidx.appcompat.R.drawable
//                    .abc_item_background_holo_light
//            )
//            Toast.makeText(
//                this@KeyPhraseActivity,
//                "You have selected " +
//                    parent.getItemAtPosition(position),
//                Toast.LENGTH_SHORT
//            ).show()
//            textViewSelected = parent.getItemAtPosition(position).toString()
//            objectSelected = parent.getItemAtPosition(position) as KeyPhrase
//            viewSelected = view
//            textViewSelectedBoolean = true
//            view.setBackgroundResource(androidx.appcompat.R.drawable.abc_list_pressed_holo_dark)
//        }
//    }

//    private fun checkSelectForDeleteKeyPhrasePopUp() {
//        if (textViewSelectedBoolean) {
//            openPopUp(textViewSelected, "delete")
//        } else {
//            Toast.makeText(
//                this@KeyPhraseActivity, "You must select a key phrase first",
//                Toast.LENGTH_SHORT
//            ).show()
//        }
//    }

    private fun openPopUp(textViewSelected: String, buttonType: String) {
        val keyPhrasePopUp = KeyPhrasePopUps(textViewSelected, buttonType)
        keyPhrasePopUp.show(supportFragmentManager, "example dialog")
    }

    private fun checkSelectForDelete(viewSelectedBoolean: Boolean, titleSelectedString: String) {
        if (viewSelectedBoolean) {
            openPopUp(titleSelectedString, "delete")
        } else {
            Toast.makeText(
                this@KeyPhraseActivity, "You must select something first",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun checkSelectForEdit(viewSelectedBoolean: Boolean, titleSelectedString: String) {
        if (viewSelectedBoolean) {
            openPopUp(titleSelectedString, "edit")
        } else {
            Toast.makeText(
                this@KeyPhraseActivity, "You must select something first",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun refreshList() {
        val keyPhraseAdapter = KeyPhraseAdapter(
            Passing.keyPhraseList, this
        )

        recyclerViewPhrases.layoutManager = LinearLayoutManager(this)
        recyclerViewPhrases.adapter = keyPhraseAdapter

        buttonAdd!!.setOnClickListener {
            openPopUp(textViewSelected, "add")
        }
        buttonEdit!!.setOnClickListener {
            textViewSelected = keyPhraseAdapter.titleSelectedString
            viewSelectedBoolean = keyPhraseAdapter.viewSelectedBoolean
            viewSelected = keyPhraseAdapter.viewSelected
            checkSelectForEdit(viewSelectedBoolean, textViewSelected)
        }
        buttonDelete!!.setOnClickListener {
            textViewSelected = keyPhraseAdapter.titleSelectedString
            viewSelectedBoolean = keyPhraseAdapter.viewSelectedBoolean
            viewSelected = keyPhraseAdapter.viewSelected
            checkSelectForDelete(viewSelectedBoolean, textViewSelected)
        }
//        buttonTest!!.setOnClickListener {
//            textViewSelected = keyPhraseAdapter.titleSelectedString
//        }
    }
//
//    private fun checkSelectForEditKeyPhrasePopUp() {
//        if (textViewSelectedBoolean) {
//            openPopUp(textViewSelected, "edit")
//        } else {
//            Toast.makeText(
//                this@KeyPhraseActivity, "You must select a key phrase first",
//                Toast.LENGTH_SHORT
//            ).show()
//        }
//    }

    override fun addKeyPhrase(keyphraseString: String) {
        if (keyphraseString.isEmpty()) {
            Toast.makeText(
                this@KeyPhraseActivity,
                "Please enter a key phrase",
                Toast.LENGTH_SHORT
            ).show()
            openPopUp(textViewSelected, "add")
        } else if (keyphraseString.trim().isNotEmpty() &&
            checkUniqueness(keyphraseString.trim())
        ) {
            Passing.keyPhraseList.add(KeyPhrase(keyphraseString.trim()))
            Toast.makeText(
                this@KeyPhraseActivity,
                "New Key Phrase Successfully " +
                    "Added",
                Toast.LENGTH_SHORT
            ).show()
            refreshList()
        } else {
            Toast.makeText(
                this@KeyPhraseActivity,
                "That Key Phrase already exists. Try something else or click cancel",
                Toast.LENGTH_SHORT
            ).show()
            openPopUp(textViewSelected, "add")
        }
    }

    override fun editKeyPhrase(keyphraseString: String) {
        if (keyphraseString.isEmpty()) {
            Toast.makeText(
                this@KeyPhraseActivity,
                "Key phrase can't be empty",
                Toast.LENGTH_SHORT
            ).show()
            openPopUp(textViewSelected, "edit")
//        } else if (keyphraseString == textViewSelected) {
//            Toast.makeText(
//                this@KeyPhraseActivity, "Make a change or click cancel",
//                Toast.LENGTH_SHORT
//            ).show()
//            openPopUp(textViewSelected, "edit")
        } else if (keyphraseString.trim().isNotEmpty() &&
            !Passing.selectedKeyPhraseObject.phrase.equals(keyphraseString.trim(), true) &&
            checkUniqueness(keyphraseString.trim())
        ) {
            Passing.selectedKeyPhraseObject.phrase = keyphraseString
            Toast.makeText(
                this@KeyPhraseActivity, "Successfully Edited",
                Toast.LENGTH_SHORT
            ).show()
            refreshList()
        } else {
            Toast.makeText(
                this@KeyPhraseActivity,
                "That Key Phrase already exists. " +
                    "Try something else or click cancel.",
                Toast.LENGTH_SHORT
            ).show()
            openPopUp(textViewSelected, "edit")
        }
    }

    override fun deleteKeyPhrase() {
        if (textViewSelected.isNotEmpty()) {
            Passing.keyPhraseList.remove(Passing.selectedKeyPhraseObject)
            Toast.makeText(
                this@KeyPhraseActivity,
                "You have deleted phrase: " +
                    textViewSelected,
                Toast.LENGTH_SHORT
            ).show()
            refreshList()
        }
    }

    private fun checkUniqueness(keyphraseString: String): Boolean {
        for (item in Passing.keyPhraseList) {
            if (item.phrase.equals(keyphraseString, true)) {
                return false
            }
        }
        return true
    }

    private fun requestRecordAudioPermission() {
        val permission = android.Manifest.permission.RECORD_AUDIO
        val grant = ContextCompat.checkSelfPermission(this, permission)
        if (grant != PackageManager.PERMISSION_GRANTED) {
            val permissionList = arrayOfNulls<String>(1)
            permissionList[0] = permission
            ActivityCompat.requestPermissions(this, permissionList, 1)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        speechRecognizer.destroy()
    }

    /**
     * Sets listener for button components and SpeechRecognizer
     */
    private fun setListeners() {
        val speechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        speechRecognizerIntent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH
        )
        speechRecognizerIntent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE,
            Locale.getDefault()
        )
        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(p0: Bundle?) {
                testKeyPhraseTextView.text = "Waiting for speech"
            }

            override fun onBeginningOfSpeech() {
                testKeyPhraseTextView.text = "Listening to speech"
            }

            override fun onRmsChanged(p0: Float) {}

            override fun onBufferReceived(p0: ByteArray?) {}

            override fun onEndOfSpeech() {
                testKeyPhraseTextView.text = ""
            }

            override fun onError(p0: Int) {}

            override fun onResults(bundle: Bundle?) {
                val data = bundle?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                recognizeKeyPhrase(data!![0])
            }

            override fun onPartialResults(bundle: Bundle?) {}

            override fun onEvent(p0: Int, p1: Bundle?) {}
        })
        buttonTest?.setOnClickListener {
            if (Passing.checkInitializationSelectedKeyPhrase()) {
                textViewSelected = Passing.selectedKeyPhraseObject.phrase
                requestRecordAudioPermission()
                speechRecognizer.startListening(speechRecognizerIntent)
                //            txtResult.text = null
            } else {
                Toast.makeText(
                    this@KeyPhraseActivity,
                    "You must select or create a key phrase first",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
//        speechOffButton.setOnClickListener {
//            speechRecognizer.stopListening()
//        }
    }

    private fun recognizeKeyPhrase(incomingSpeech: String?) {
        if (textViewSelected.isEmpty()) {
            Toast.makeText(
                this@KeyPhraseActivity,
                "You must select a Key Phrase first",
                Toast.LENGTH_SHORT
            ).show()
        } else if (findKeyPhraseMatch(incomingSpeech) != null) {
            findKeyPhraseMatch(incomingSpeech)?.phrase
                ?.let { TestListenKeyPhrasePopUp(it, "success") }
                ?.show(supportFragmentManager, "example dialog")
        } else {
            val testListenKeyPhrasePopUp = TestListenKeyPhrasePopUp(
                textViewSelected, "unrecognized")
            testListenKeyPhrasePopUp.show(supportFragmentManager, "example dialog")
        }
    }

    private fun findKeyPhraseMatch(incomingSpeech: String?): KeyPhrase? {
        if (incomingSpeech?.contains(textViewSelected, true) == true) {
            return Passing.selectedKeyPhraseObject
        }
        return null
    }
}
