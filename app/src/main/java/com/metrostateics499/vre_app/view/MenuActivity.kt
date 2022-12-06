package com.metrostateics499.vre_app.view

import android.Manifest
import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioManager
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.telephony.PhoneStateListener
import android.telephony.SmsManager
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.metrostateics499.vre_app.R
import com.metrostateics499.vre_app.model.Passing
import com.metrostateics499.vre_app.model.data.EmergencyMessageSetup
import com.metrostateics499.vre_app.model.data.KeyPhrase
import com.metrostateics499.vre_app.utility.LocationGPS
import github.com.vikramezhil.dks.speech.Dks
import github.com.vikramezhil.dks.speech.DksListener
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.android.synthetic.main.activity_menu.*

class MenuActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var emergencyFound: EmergencyMessageSetup
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

    private lateinit var speechButton: Button
    private lateinit var latitudeValueTextView: TextView
    private lateinit var longitudeValueTextView: TextView
    private lateinit var coordinatesDateTimeTextView: TextView
    private var locationPermissionCode = 2
    private lateinit var locationManager: LocationGPS
    private lateinit var app: Application
    private lateinit var dks: Dks

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = this.getApplication()

        // This is to hide the action bar
        supportActionBar?.hide()
        setContentView(R.layout.activity_menu)

        locationManager = LocationGPS(this as Context)
        latitudeValueTextView = findViewById(R.id.latitudeValueTextView)
        longitudeValueTextView = findViewById(R.id.longitudeValueTextView)
        coordinatesDateTimeTextView = findViewById(R.id.coordinatesDateTimeTextView)
        val switchMenuGPS: SwitchCompat = findViewById(R.id.switchMenuGPS)

        // Register button click listeners
        speechButton = findViewById(R.id.speechRecognition)
        speechButton.setOnClickListener() {
            startActivity(Intent(this, ListenSpeechActivity::class.java))
        }

        val goToEmergencyMessageSetup = findViewById<Button>(R.id.emergencyMessageSetup)
        goToEmergencyMessageSetup.setOnClickListener {
            startActivity(Intent(this, EmergencyMessageSetupActivity::class.java))
        }
        checkIfPingingLocation()
        checkIfGPSSwitchOn()
        initializeComponents()
        setListeners()
    }

    override fun onPostResume() {
        super.onPostResume()
        checkIfPingingLocation()
    }

    private fun initializeComponents() {
        recordAudioPermissionRequest =
            registerForActivityResult(
                ActivityResultContracts
                    .RequestMultiplePermissions()
            ) {
            }
        txtResult = findViewById(R.id.vreServiceActiveText)
//        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
        textToSpeech = TextToSpeech(this, this)
        telephonyManager = this.getSystemService(TELEPHONY_SERVICE) as TelephonyManager
        audioManager = getSystemService(AUDIO_SERVICE) as AudioManager

        myHashAlarm[TextToSpeech.Engine.KEY_PARAM_STREAM] =
            AudioManager.STREAM_VOICE_CALL.toString()

//        requestSmsPermission()
//        requestCallPermission()

        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE)
    }

    private fun setListeners() {
        switchMenuGPS.setOnClickListener {
            if (requestGPSPermission()) {
                if (switchMenuGPS.isChecked) {
                    switchMenuGPS.isChecked = true
                    Passing.locationTrackingRequested = true
                    locationManager.startLocationTracking(locationCallback)
                    Toast.makeText(
                        this@MenuActivity,
                        "You have activated GPS location for all activated Emergency Messages",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    switchMenuGPS.isChecked = false
                    onPause()
                    Passing.locationTrackingRequested = false
                    locationManager.stopLocationTracking()
                    Toast.makeText(
                        this@MenuActivity,
                        "You have deactivated GPS location for all Emergency Messages",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                switchMenuGPS.isChecked = false
                onPause()
            }
        }
        switchMenuEMSPingingLocation.setOnClickListener {
            if (switchMenuEMSPingingLocation.isChecked) {
                switchMenuEMSPingingLocation.isChecked = false
                Toast.makeText(
                    this@MenuActivity,
                    "EMS Pinging only activates when an EMS is activated with a keyphrase. " +
                        "You can only deactivate it here.",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                switchMenuEMSPingingLocation.isChecked = false
                for (emergencyMessage in Passing.emergencyMessageSetupList) {
                    emergencyMessage.activePingLocation = false
                }

                Toast.makeText(
                    this@MenuActivity,
                    "Stopped Pinging Location to Active Emergency Messages",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        menuVreServiceSwitch.setOnClickListener {
            if (requestRecordAudioPermission()) {
                if (menuVreServiceSwitch.isChecked) {
                    menuVreServiceSwitch.isChecked = true
                    Passing.vreServiceActive = true
                    dks.startSpeechRecognition()
                    vreServiceActiveText.text = "VRE Service is ON - Listening for keyphrases..."
                    Toast.makeText(
                        this@MenuActivity,
                        "You have activated VRE service for all activated Emergency Messages",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    menuVreServiceSwitch.isChecked = false
                    onPause()
                    Passing.vreServiceActive = false
                    dks.closeSpeechOperations()
                    vreServiceActiveText.text = "VRE Service is OFF"
                    Toast.makeText(
                        this@MenuActivity,
                        "You have deactivated VRE service",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                menuVreServiceSwitch.isChecked = false
                onPause()
            }
        }
        dks = Dks(
            application, supportFragmentManager,
            object : DksListener {
                override fun onDksLiveSpeechResult(liveSpeechResult: String) {
                    Log.d("DKS", "Speech result - $liveSpeechResult")
                    if (findKeyPhraseMatch(liveSpeechResult) != null) {
                        vreServiceActiveText.text = buildString {
                            findKeyPhraseMatch(liveSpeechResult)?.let {
                                append(
                                    "KeyPhrase Recognized! - Processing Emergency Message...\n",
                                    it.phrase
                                )
                            }
                        }
                        vreServiceActiveTextTimer.start()
                        findEmergencyMessageSetupMatch(
                            findKeyPhraseMatch(liveSpeechResult)?.phrase
                        )?.let {
                            performEmergencyMessage(
                                it
                            )
                        }
                    } else {
                        vreServiceActiveText.text = "VRE Service is ON - Not Recognized" +
                            " - Still listening..."
                    }
                }

                override fun onDksFinalSpeechResult(speechResult: String) {
                    Log.d("DKS", "Final speech result - $speechResult")
                    if (findKeyPhraseMatch(speechResult) != null) {
                        vreServiceActiveText.text = buildString {
                            findKeyPhraseMatch(speechResult)?.let {
                                append(
                                    "KeyPhrase Recognized! - Processing Emergency Message...\n",
                                    it.phrase
                                )
                            }
                        }
                        vreServiceActiveTextTimer.start()
                        findEmergencyMessageSetupMatch(
                            findKeyPhraseMatch(speechResult)?.phrase
                        )?.let {
                            performEmergencyMessage(
                                it
                            )
                        }
                    } else {
                        vreServiceActiveText.text = "VRE Service is ON - Not Recognized" +
                            " - Still listening..."
                        vreServiceActiveTextTimer.start()
                    }
                }

                override fun onDksLiveSpeechFrequency(frequency: Float) {

                    Log.d("DKS", "frequency - $frequency")
                }

                override fun onDksLanguagesAvailable(
                    defaultLanguage: String?,
                    supportedLanguages: ArrayList<String>?
                ) {
                    Log.d("DKS", "defaultLanguage - $defaultLanguage")
                    Log.d("DKS", "supportedLanguages - $supportedLanguages")
                }

                override fun onDksSpeechError(errMsg: String) {
                    Log.d("DKS", "errMsg - $errMsg")
                }
            }
        )
    }

    val vreServiceActiveTextTimer = object : CountDownTimer(5_000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
        }
        override fun onFinish() {
            vreServiceActiveText.text = "VRE Service is ON - Listening for keyphrases..."
        }
    }

    val vreServiceSendingTextTimer = object : CountDownTimer(5_000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
        }
        override fun onFinish() {
            vreServiceActiveText.text = "Performing Emergency Message..."
        }
    }

    private fun performEmergencyMessage(emergencySetup: EmergencyMessageSetup) {

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
    }

    private fun makePhoneCall(phoneNumber: String) {

        if (phoneNumber.trim { it <= ' ' }.isNotEmpty()) {
            if (ContextCompat.checkSelfPermission(
                    this@MenuActivity,
                    Manifest.permission.CALL_PHONE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this@MenuActivity,
                    arrayOf(Manifest.permission.CALL_PHONE),
                    requestCall
                )
            } else {
                val dial = "tel:$phoneNumber"

                startActivity(Intent(Intent.ACTION_CALL, Uri.parse(dial)))
            }
        }
    }

    /**
     * Shows an AlertDialog window that informs users why the mic permission is required.
     * Selecting 'Ok' will ask for the permission
     * Selecting 'Cancel' will close the window
     */

    private fun playActivationWarningMessage() {
        val streamMaxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        audioManager.mode = AudioManager.MODE_NORMAL
        audioManager.isSpeakerphoneOn = true
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, streamMaxVolume, 0)
        textToSpeech?.speak(warningMessage, TextToSpeech.QUEUE_FLUSH, myHashAlarm)
        textToSpeech?.speak(warningMessage, TextToSpeech.QUEUE_ADD, myHashAlarm)
    }

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
                    this, Manifest.permission.READ_PHONE_STATE
                )
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_PHONE_STATE),
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

    private var phoneStateListener = object : PhoneStateListener() {
        override fun onCallStateChanged(state: Int, incomingNumber: String) {
            // TODO Auto-generated method stub
            super.onCallStateChanged(state, incomingNumber)
            when (state) {
                TelephonyManager.CALL_STATE_RINGING -> {
                    Toast.makeText(
                        this@MenuActivity,
                        "Phone RINGING",
                        Toast.LENGTH_LONG
                    ).show()
                }
                TelephonyManager.CALL_STATE_OFFHOOK -> {
                    Toast.makeText(
                        this@MenuActivity,
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
                        this@MenuActivity,
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

    private fun splitEmergencyTextMessage(textMessage: String): List<String> {
        val size = 160
        return textMessage.split("(?<=\\G.{$size})".toRegex())
    }

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

    private fun checkIfPingingLocation() {
        for (emergencyMessage in Passing.emergencyMessageSetupList) {
            if (emergencyMessage.activePingLocation) {
                switchMenuEMSPingingLocation.isChecked = true
            }
        }
    }
    private fun checkIfGPSSwitchOn() {
        if (Passing.locationTrackingRequested) {
            switchMenuGPS.isChecked = true
        }
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
//            locationResult
            for (location in locationResult.locations) {
                // Update UI
                val date = getCurrentDateTime()
                val dateString = date.toString("yyyy/MM/dd HH:mm")
                latitudeValueTextView.text = location.latitude.toString()
                longitudeValueTextView.text = location.longitude.toString()
                coordinatesDateTimeTextView.text = dateString
                Passing.latitude = location.latitude.toString()
                Passing.longitude = location.longitude.toString()

                Passing.dateTimeGPS = dateString
            }
        }
    }

    fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }

    fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }

    private fun requestGPSPermission(): Boolean {
        val permission = android.Manifest.permission.ACCESS_FINE_LOCATION
        val grant = ContextCompat.checkSelfPermission(this, permission)
        if (grant != PackageManager.PERMISSION_GRANTED) {
            val permissionList = arrayOfNulls<String>(1)
            permissionList[0] = permission
            ActivityCompat.requestPermissions(this, permissionList, locationPermissionCode)
        }
        return grant == PackageManager.PERMISSION_GRANTED
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                switchMenuGPS.isChecked = true
                Passing.locationTrackingRequested = true
                Toast.makeText(
                    this@MenuActivity,
                    "Permission Granted.\nYou have activated GPS " +
                        "Tracking for all activated Emergency Messages",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
        if (requestCode == REQRECORDAUDIOCODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                showMicPermissionRationale()
            }
        }
    }

    private fun showMicPermissionRationale() {
        AlertDialog.Builder(this)
            .setTitle("Rationale")
            .setMessage("Mic Permission required to use SpeechToText Recognition")
            .setNeutralButton("Ok") { _, _ ->
                requestRecordAudioPermission()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}