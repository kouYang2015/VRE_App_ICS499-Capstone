package com.metrostateics499.vre_app.view

import android.Manifest
import android.Manifest.permission.READ_PHONE_STATE
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioManager
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.telephony.PhoneStateListener
import android.telephony.SmsManager
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.metrostateics499.vre_app.R
import com.metrostateics499.vre_app.model.Passing
import com.metrostateics499.vre_app.model.data.EmergencyMessageSetup
import com.metrostateics499.vre_app.model.data.KeyPhrase
import java.util.*

/**
 * Listen speech activity. Activity used to show view of and handle user interaction when they
 * want to test speech recognition of saved KeyPhrases.
 *
 * @constructor Create empty Listen speech activity
 */
class ListenSpeechActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var speechOnButton: Button
    private lateinit var speechOffButton: Button
    private lateinit var txtResult: TextView
    private lateinit var speechRecognizer: SpeechRecognizer
    private val REQRECORDAUDIOCODE = 10001
    private lateinit var recordAudioPermissionRequest: ActivityResultLauncher<Array<String>>
    private val requestCall = 1
    private var textToSpeech: TextToSpeech? = null
    private lateinit var audioManager: AudioManager
    private var myHashAlarm: HashMap<String, String> = HashMap()
    private var warningMessage: String = "Voice Recognition Emergency Services " +
        "have been activated. Your emergency message and your location has " +
        "been sent to all your emergency contacts."

    private var callMessageTTS: String? = null
    private lateinit var telephonyManager: TelephonyManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_listen_speech)
        initializeComponents()
        setListeners()
    }

    override fun onDestroy() {
        super.onDestroy()
        speechRecognizer.destroy()
        if (textToSpeech != null) {
            textToSpeech!!.stop()
            textToSpeech!!.shutdown()
        }
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
                txtResult.hint = "Waiting for speech"
            }

            override fun onBeginningOfSpeech() {
                txtResult.hint = "Listening to speech"
            }

            override fun onRmsChanged(p0: Float) {}

            override fun onBufferReceived(p0: ByteArray?) {}

            override fun onEndOfSpeech() {}

            override fun onError(p0: Int) {}

            override fun onResults(bundle: Bundle?) {
                val data = bundle?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                recognizeKeyPhrase(data!![0])
            }

            override fun onPartialResults(bundle: Bundle?) {}

            override fun onEvent(p0: Int, p1: Bundle?) {}
        })
        speechOnButton.setOnClickListener {
            checkAndRequestPermissions()
            speechRecognizer.startListening(speechRecognizerIntent)
            txtResult.text = null
        }
        speechOffButton.setOnClickListener {
            speechRecognizer.stopListening()
        }
    }

    /**
     * Checks if there are any keyPhrases set. If there is, check if User has spoken any of them
     * and displays to screen the recognized key phrase.
     *
     * @param incomingSpeech
     */
    private fun recognizeKeyPhrase(incomingSpeech: String?) {
        if (Passing.keyPhraseList.isEmpty() && Passing.emergencyMessageSetupList.isEmpty()
        ) {
            txtResult.text = buildString { append("No KeyPhrase(s) set") }
        } else {
            if (findKeyPhraseMatch(incomingSpeech) != null) {
                val emergencySetup =
                    (findEmergencyMessageSetupMatch(findKeyPhraseMatch(incomingSpeech)?.phrase))
                var coordinatesLinks: String
                var coordinatesDate: String

                if (emergencySetup != null) {
                    if (emergencySetup.activeSendText) {
                        if (emergencySetup.activeGPS) {
                            coordinatesLinks =
                                "My last known location: www.google.com/maps/place/" +
                                Passing.latitude + "," + Passing.longitude +
                                " or http://maps.apple.com/?daddr=" +
                                Passing.latitude + "," + Passing.longitude
                            coordinatesDate =
                                "Last known coordinates were taken on date: \n" +
                                Passing.dateTimeGPS +
                                "\nLatitude: " + Passing.latitude +
                                "\nLongitude: " + Passing.longitude
                        } else {
                            coordinatesLinks = "Last Known Location: Unavailable or Deactivated "
                            coordinatesDate = ""
                        }
                        for (contact in emergencySetup.selectedContactList) {
                            try {
                                val smsManager: SmsManager =
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                        applicationContext.getSystemService(
                                            SmsManager::class.java
                                        )
                                    } else {
                                        SmsManager.getDefault()
                                    }
                                val emergencyTextMessage =
                                    "VOICE RECOGNITION EMERGENCY: " +
                                        emergencySetup.getCustomTextListString()
                                var textMessages: List<String>

                                if (emergencyTextMessage.length > 160 && emergencySetup.activeGPS) {
                                    textMessages = splitEmergencyTextMessage(emergencyTextMessage)
                                    textMessages = (
                                        textMessages +
                                            coordinatesLinks +
                                            coordinatesDate
                                        )
                                } else if (emergencyTextMessage.length > 160 &&
                                    !emergencySetup.activeGPS
                                ) {
                                    textMessages = splitEmergencyTextMessage(emergencyTextMessage)
                                    textMessages = (textMessages + coordinatesLinks)
                                } else if (emergencyTextMessage.length <= 160 &&
                                    !emergencySetup.activeGPS
                                ) {
                                    textMessages =
                                        listOf(emergencyTextMessage, coordinatesLinks)
                                } else {
                                    textMessages =
                                        listOf(
                                            emergencyTextMessage,
                                            coordinatesLinks,
                                            coordinatesDate
                                        )
                                }
                                for (textItem in textMessages) {
                                    smsManager.sendTextMessage(
                                        contact.phoneNumber, null,
                                        textItem, null, null
                                    )
                                    Thread.sleep(1_500)
                                }
                                Toast.makeText(
                                    applicationContext, "Message Sent",
                                    Toast.LENGTH_LONG
                                ).show()
                            } catch (e: Exception) {
                                Toast.makeText(
                                    applicationContext,
                                    "Missing Contact Data" +
                                        e.message.toString(),
                                    Toast.LENGTH_LONG
                                )
                                    .show()
                            }
                        }
                    }
                    if (emergencySetup.activeGPS && emergencySetup.activeSendText) {
                        emergencySetup.activePingLocation = true

                        AsyncTask.execute {
                            while (emergencySetup.activePingLocation) {
                                Thread.sleep(120_000)
                                if (emergencySetup.activePingLocation) {
                                    val smsManager: SmsManager =
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                            applicationContext.getSystemService(
                                                SmsManager::class.java
                                            )
                                        } else {
                                            SmsManager.getDefault()
                                        }
                                    coordinatesLinks =
                                        "New Location Ping: www.google.com/maps/place/" +
                                        Passing.latitude + "," + Passing.longitude +
                                        " or http://maps.apple.com/?daddr=" +
                                        Passing.latitude + "," + Passing.longitude
                                    coordinatesDate =
                                        "Coordinates Timestamp: \n" +
                                        Passing.dateTimeGPS +
                                        "\nLatitude: " + Passing.latitude +
                                        "\nLongitude: " + Passing.longitude
                                    for (contact in emergencySetup.selectedContactList) {
                                        smsManager.sendTextMessage(
                                            contact.phoneNumber, null,
                                            coordinatesLinks, null, null
                                        )
                                        Thread.sleep(1_500)
                                        smsManager.sendTextMessage(
                                            contact.phoneNumber, null,
                                            coordinatesDate, null, null
                                        )
                                    }
                                }
                            }
                        }
                    }
                    if (emergencySetup.activeAudioWarningMessage) {
                        playActivationWarningMessage()
                    }
                    if (emergencySetup.activeCall) {
//                        callMessageTTS?.let { saveToAudioFile(it) }
                        Thread.sleep(2_000)
                        for (contact in emergencySetup.selectedContactList) {
                            makePhoneCall(
                                contact.phoneNumber
                            )
                        }
                    }
                }
                txtResult.text = buildString {
                    findKeyPhraseMatch(incomingSpeech)?.let {
                        append(
                            "KeyPhrase Recognized!\n",
                            it.phrase
                        )
                    }
                }
            } else {
                txtResult.text = buildString {
                    append("No Keyphrase matched")
                }
            }
        }
    }

    private fun splitEmergencyTextMessage(textMessage: String): List<String> {
        val size = 160
        return textMessage.split("(?<=\\G.{$size})".toRegex())
    }

    private fun findEmergencyMessageSetupMatch(keyPhraseMatch: String?): EmergencyMessageSetup? {
        for (emergencySetup in Passing.emergencyMessageSetupList) {
            for (phrase in emergencySetup.selectedKeyPhraseList) {
                if (keyPhraseMatch?.contains(phrase.toString(), true) == true) {
                    return emergencySetup
                }
            }
        }
        return null
    }

    /**
     * Find keyPhrase object in the list of set keyPhrase that matches User's speech.
     *
     * @param incomingSpeech
     * @return KeyPhrase if there is a KeyPhrase object that matches User's speech.
     */
    private fun findKeyPhraseMatch(incomingSpeech: String?): KeyPhrase? {
        for (emergencySetup in Passing.emergencyMessageSetupList) {
            for (phrase in emergencySetup.selectedKeyPhraseList) {
                if (Passing.selectedEmergencyMessageSetup.activeEMS) {
                    if (incomingSpeech?.contains(phrase.toString(), true) == true) {
                        return phrase
                    }
                }
            }
        }
        return null
    }

    /**
     * Initializes variables to handle components.
     */
    private fun initializeComponents() {
        recordAudioPermissionRequest =
            registerForActivityResult(
                ActivityResultContracts
                    .RequestMultiplePermissions()
            ) {
            }
        speechOnButton = findViewById(R.id.activateSpeech)
        speechOffButton = findViewById(R.id.disableSpeech)
        txtResult = findViewById(R.id.speechToTextBox)
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
        textToSpeech = TextToSpeech(this, this)
        telephonyManager = this.getSystemService(TELEPHONY_SERVICE) as TelephonyManager
        audioManager = getSystemService(AUDIO_SERVICE) as AudioManager

        myHashAlarm[TextToSpeech.Engine.KEY_PARAM_STREAM] =
            AudioManager.STREAM_VOICE_CALL.toString()

        requestSmsPermission()
        requestCallPermission()

        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE)
    }

    /**
     * Checks if phone is compatible with SpeechRecognizer and if permission required is currently granted.
     * Requests user for permission if not.
     */
    private fun checkAndRequestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission
                (
                        this,
                        Manifest.permission.RECORD_AUDIO
                    ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.RECORD_AUDIO),
                    REQRECORDAUDIOCODE
                )
            }
            if (ActivityCompat.checkSelfPermission(
                    this, READ_PHONE_STATE
                )
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(READ_PHONE_STATE),
                    4
                )
            }
        } else {
            Toast.makeText(
                this,
                "Phone not compatible with App. Requires Android 7+",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    /**
     * Handles results of callback from requestPermission(). Shows rationale for permission request
     * if permission was not granted. If permission granted, show toast confirmation.
     */
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQRECORDAUDIOCODE && permissionGranted(grantResults)
        ) {
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
        } else {
            showMicPermissionRationale()
        }
    }

    /**
     * Checks if permission has been granted in grantResults IntArray.
     * @return true: iff all value in the grantResults IntArray are 1.
     * @return false: if any value in the grantResults IntArray are 0.
     */
    private fun permissionGranted(grantResults: IntArray): Boolean {
        for (grantResult in grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false
            }
        }
        return true
    }

    /**
     * Shows an AlertDialog window that informs users why the mic permission is required.
     * Selecting 'Ok' will ask for the permission
     * Selecting 'Cancel' will close the window
     */
    private fun showMicPermissionRationale() {
        AlertDialog.Builder(this)
            .setTitle("Rationale")
            .setMessage("Mic Permission required to use SpeechToText Recognition")
            .setNeutralButton("Ok") { _, _ ->
                recordAudioPermissionRequest.launch(
                    arrayOf(Manifest.permission.RECORD_AUDIO)
                )
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun requestSmsPermission() {
        val permission = Manifest.permission.SEND_SMS
        val grant = ContextCompat.checkSelfPermission(this, permission)
        if (grant != PackageManager.PERMISSION_GRANTED) {
            val permissionList = arrayOfNulls<String>(1)
            permissionList[0] = permission
            ActivityCompat.requestPermissions(this, permissionList, 1)
        }
    }

    private fun requestCallPermission() {
        val permission = Manifest.permission.CALL_PHONE
        val grant = ContextCompat.checkSelfPermission(this, permission)
        if (grant != PackageManager.PERMISSION_GRANTED) {
            val permissionList = arrayOfNulls<String>(1)
            permissionList[0] = permission
            ActivityCompat.requestPermissions(this, permissionList, requestCall)
        }
    }

    private fun makePhoneCall(phoneNumber: String) {

        if (phoneNumber.trim { it <= ' ' }.isNotEmpty()) {
            if (ContextCompat.checkSelfPermission(
                    this@ListenSpeechActivity,
                    Manifest.permission.CALL_PHONE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this@ListenSpeechActivity,
                    arrayOf(Manifest.permission.CALL_PHONE),
                    requestCall
                )
            } else {
                val dial = "tel:$phoneNumber"

                startActivity(Intent(Intent.ACTION_CALL, Uri.parse(dial)))
            }
        }
    }

    private fun playActivationWarningMessage() {
        val streamMaxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        audioManager.mode = AudioManager.MODE_NORMAL
        audioManager.isSpeakerphoneOn = true
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, streamMaxVolume, 0)
        textToSpeech?.speak(warningMessage, TextToSpeech.QUEUE_FLUSH, myHashAlarm)
        textToSpeech?.speak(warningMessage, TextToSpeech.QUEUE_ADD, myHashAlarm)
    }

    private var phoneStateListener = object : PhoneStateListener() {
        override fun onCallStateChanged(state: Int, incomingNumber: String) {
            // TODO Auto-generated method stub
            super.onCallStateChanged(state, incomingNumber)
            when (state) {
                TelephonyManager.CALL_STATE_RINGING -> {
                    Toast.makeText(
                        this@ListenSpeechActivity,
                        "Phone RINGING",
                        Toast.LENGTH_LONG
                    ).show()
                }
                TelephonyManager.CALL_STATE_OFFHOOK -> {
                    Toast.makeText(
                        this@ListenSpeechActivity,
                        "Phone Offhook",
                        Toast.LENGTH_LONG
                    ).show()
                    Thread.sleep(1_000)
                    val streamMaxVolume = audioManager
                        .getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL)
                    audioManager.mode = AudioManager.MODE_IN_CALL
                    audioManager.isSpeakerphoneOn = true
                    audioManager.setStreamVolume(
                        AudioManager.STREAM_VOICE_CALL,
                        streamMaxVolume, 0
                    )

                    Thread.sleep(5_000)
//
//                    textToSpeech?.speak(callMessageTTS, TextToSpeech.QUEUE_FLUSH, myHashAlarm)
//                    textToSpeech?.speak(callMessageTTS, TextToSpeech.QUEUE_ADD, myHashAlarm)
                }
                TelephonyManager.CALL_STATE_IDLE -> {
                    Toast.makeText(
                        this@ListenSpeechActivity,
                        "Phone IDLE",
                        Toast.LENGTH_LONG
                    ).show()
                    audioManager.isSpeakerphoneOn = false
                }
            }
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = textToSpeech!!.setLanguage(Locale.US)

            if (result == TextToSpeech.LANG_MISSING_DATA ||
                result == TextToSpeech.LANG_NOT_SUPPORTED
            ) {
                Log.e("TTS", "The Language not supported!")
            }
        }
    }
}
