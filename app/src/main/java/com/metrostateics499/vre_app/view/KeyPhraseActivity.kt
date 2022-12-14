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
import com.metrostateics499.vre_app.view.adapters.KeyPhraseAdapter
import java.util.*
import kotlinx.android.synthetic.main.activity_edit_emergency_message.*
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

    private val REQRECORDAUDIOCODE = 10001
    private lateinit var speechRecognizer: SpeechRecognizer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
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
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
    }

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
    }

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
        } else if (keyphraseString == textViewSelected) {
            Toast.makeText(
                this@KeyPhraseActivity, "Make a change or click cancel",
                Toast.LENGTH_SHORT
            ).show()
            openPopUp(textViewSelected, "edit")
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
            for (item in Passing.emergencyMessageSetupList) {
                Passing.selectedKeyPhraseObject.let {
                    item.selectedKeyPhraseList.remove(it)
                }
            }
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQRECORDAUDIOCODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(
                    this@KeyPhraseActivity,
                    "Permission Granted.",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun requestRecordAudioPermission(): Boolean {
        val permission = android.Manifest.permission.RECORD_AUDIO
        val grant = ContextCompat.checkSelfPermission(this, permission)
        if (grant != PackageManager.PERMISSION_GRANTED) {
            val permissionList = arrayOfNulls<String>(1)
            permissionList[0] = permission
            ActivityCompat.requestPermissions(this, permissionList, REQRECORDAUDIOCODE)
        }
        return grant == PackageManager.PERMISSION_GRANTED
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

            override fun onError(p0: Int) {
                testKeyPhraseTextView.text = ""
            }

            override fun onResults(bundle: Bundle?) {
                val data = bundle?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                recognizeKeyPhrase(data!![0])
            }

            override fun onPartialResults(bundle: Bundle?) {}

            override fun onEvent(p0: Int, p1: Bundle?) {}
        })
        buttonTest?.setOnClickListener {
            if (requestRecordAudioPermission()) {
                if (Passing.checkInitializationSelectedKeyPhrase()) {
                    textViewSelected = Passing.selectedKeyPhraseObject.phrase
                    requestRecordAudioPermission()
                    speechRecognizer.startListening(speechRecognizerIntent)
                } else {
                    Toast.makeText(
                        this@KeyPhraseActivity,
                        "You must select or create a key phrase first",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
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
                ?.let { KeyPhrasePopUps(it, "success") }
                ?.show(supportFragmentManager, "example dialog")
        } else {
            val testListenKeyPhrasePopUp = KeyPhrasePopUps(
                textViewSelected, "unrecognized"
            )
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
