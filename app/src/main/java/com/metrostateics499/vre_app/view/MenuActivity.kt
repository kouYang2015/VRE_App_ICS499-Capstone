package com.metrostateics499.vre_app.view

import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioManager
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.metrostateics499.vre_app.R
import com.metrostateics499.vre_app.model.Passing
import com.metrostateics499.vre_app.model.data.KeyPhrase
import com.metrostateics499.vre_app.utility.LocationGPS
import com.metrostateics499.vre_app.utility.ProcessEmergencyMessageService
import github.com.vikramezhil.dks.speech.Dks
import github.com.vikramezhil.dks.speech.DksListener
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.android.synthetic.main.activity_menu.*

class MenuActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var txtResult: TextView
    private val REQRECORDAUDIOCODE = 10001
    private lateinit var recordAudioPermissionRequest: ActivityResultLauncher<Array<String>>
    private var textToSpeech: TextToSpeech? = null
    private lateinit var audioManager: AudioManager
    private var myHashAlarm: HashMap<String, String> = HashMap()
    private var warningMessage: String = "Voice Recognition Emergency Services " +
        "have been activated. Your emergency message and your location has " +
        "been sent to all your emergency contacts."

    private lateinit var telephonyManager: TelephonyManager

    private lateinit var speechButton: Button
    private lateinit var latitudeValueTextView: TextView
    private lateinit var longitudeValueTextView: TextView
    private lateinit var coordinatesDateTimeTextView: TextView
    private var locationPermissionCode = 2
    private lateinit var locationManager: LocationGPS
    private lateinit var app: Application
    private lateinit var dks: Dks
    private lateinit var profileButton: Button
    private lateinit var logoutButton: Button
    private var callState: String = "idle"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = this.getApplication()

        // This is to hide the action bar
        supportActionBar?.hide()
        setContentView(R.layout.activity_menu)
        logoutButton = findViewById(R.id.logout_button)

        locationManager = LocationGPS(this as Context)
        latitudeValueTextView = findViewById(R.id.latitudeValueTextView)
        longitudeValueTextView = findViewById(R.id.longitudeValueTextView)
        coordinatesDateTimeTextView = findViewById(R.id.coordinatesDateTimeTextView)

        // Profile button click listeners
        profileButton = findViewById(R.id.profile)
        profileButton.setOnClickListener {
            val intent = Intent(this, ProfileInformationActivity::class.java)
            startActivity(intent)
        }

        logoutButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finishAffinity()
        }

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
        textToSpeech = TextToSpeech(this, this)
        myHashAlarm[TextToSpeech.Engine.KEY_PARAM_STREAM] =
            AudioManager.STREAM_VOICE_CALL.toString()
        audioManager = getSystemService(AUDIO_SERVICE) as AudioManager
        telephonyManager = this.getSystemService(TELEPHONY_SERVICE) as TelephonyManager
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE)
        recordAudioPermissionRequest =
            registerForActivityResult(
                ActivityResultContracts
                    .RequestMultiplePermissions()
            ) {
            }
        txtResult = findViewById(R.id.vreServiceActiveText)
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
            if (menuVreServiceSwitch.isChecked) {
                val activeEMS = checkActiveEMS()
                if (activeEMS) {
                    if (requestRecordAudioPermission()) {
                        menuVreServiceSwitch.isChecked = true
                        Passing.vreServiceActive = true
                        dks.startSpeechRecognition()
                        vreServiceActiveText.text = "VRE Service is ON - " +
                            "Listening for keyphrases..."
                        Toast.makeText(
                            this@MenuActivity,
                            "You have activated VRE service for all " +
                                "activated Emergency Messages",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    menuVreServiceSwitch.isChecked = false
                    onPause()
                    Passing.vreServiceActive = false
                    Toast.makeText(
                        this@MenuActivity,
                        "VRE Service can only be activated when you've " +
                            "setup and activated an emergency message",
                        Toast.LENGTH_LONG
                    ).show()
                }
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
        }

        dks = Dks(
            application, supportFragmentManager,
            object : DksListener {
                override fun onDksLiveSpeechResult(liveSpeechResult: String) {
                    Log.d("DKS", "Speech result - $liveSpeechResult")
                    if (findKeyPhraseMatch(liveSpeechResult) != null) {
                        vreServiceActiveText.text = buildString {
                            append(
                                "KeyPhrase Recognized! - Processing Emergency Message...\n",
                                liveSpeechResult
                            )
                        }
                        checkIfPingingLocation()
                        vreServiceActiveTextTimer.start()
                        startService(
                            Intent(
                                this@MenuActivity,
                                ProcessEmergencyMessageService::class.java
                            )
                        )
                        Thread.sleep(2_000)
                        if (Passing.vreActivatedEMS.activeAudioWarningMessage) {
                            playActivationWarningMessage()
                        }
                        Thread.sleep(2_000)
                        if (Passing.vreActivatedEMS.activeCall) {
                            Passing.callingInProcess = true
                            phoneCallLoop()
                        }
                    }

                    if (Passing.callingInProcess) {
                        if ((liveSpeechResult.contains(Passing.deactivateCallingPhrase, true))) {
                            Passing.callingInProcess = false
                            vreServiceActiveText.text =
                                "VRE Service is ON - Recognized Stop Calls" +
                                " - Still listening..."
                            Passing.callingInProcess = false
                            vreServiceActiveTextTimer.start()
                        }
                    } else {
                        vreServiceActiveText.text = "VRE Service is ON - Not Recognized" +
                            " - Still listening..."
                        vreServiceActiveTextTimer.start()
                    }
                }

                override fun onDksFinalSpeechResult(speechResult: String) {
                    Log.d("DKS", "Final speech result - $speechResult")
//                    if (findKeyPhraseMatch(speechResult) != null) {
//                        vreServiceActiveText.text = buildString {
//                                append(
//                                    "KeyPhrase Recognized! - Processing Emergency Message...\n",
//                                    speechResult
//                                )
//                            }
//                        vreServiceActiveTextTimer.start()
//                        Thread.sleep(5_000)
//                        startService(Intent(this@MenuActivity, ProcessEmergencyMessage::class.java))
//                        } else {
//                            vreServiceActiveText.text = "VRE Service is ON - Not Recognized" +
//                                " - Still listening..."
//                            vreServiceActiveTextTimer.start()
//                    }
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
                    
    private fun phoneCallLoop() {
        Thread {
            while (Passing.vreActivatedEMS.activeCall) {
                if (!Passing.callingInProcess) {
                    textToSpeech?.speak(
                        "Calling Stopped",
                        TextToSpeech
                            .QUEUE_FLUSH,
                        myHashAlarm
                    )
                    break
                }
                try {
                    for (contact in Passing.vreActivatedEMS.selectedContactList) {
                        if (!Passing.callingInProcess) {
                            textToSpeech?.speak(
                                "Calling Stopped",
                                TextToSpeech
                                    .QUEUE_FLUSH,
                                myHashAlarm
                            )
                            break
                        }
                        if (callState == "idle") {
                            textToSpeech?.speak(
                                "Calling " + contact.name,
                                TextToSpeech
                                    .QUEUE_FLUSH,
                                myHashAlarm
                            )
                            makePhoneCall(contact.phoneNumber)
                            Thread.sleep(30_000)
                        } else {
                            Thread.sleep(30_000)
                            if (!Passing.callingInProcess) {
                                textToSpeech?.speak(
                                    "Calling Stopped",
                                    TextToSpeech
                                        .QUEUE_FLUSH,
                                    myHashAlarm
                                )
                                Thread.sleep(4_000)
                                break
                            }
                        }
                    }
                } catch (e: Exception) {
                    Toast.makeText(
                        applicationContext,
                        "Something Went Wrong" +
                            e.message.toString(),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }.start()
    }

    private fun makePhoneCall(phoneNumber: String) {

        if (phoneNumber.trim { it <= ' ' }.isNotEmpty()) {
            val dial = "tel:$phoneNumber"
            startActivity(Intent(Intent.ACTION_CALL, Uri.parse(dial)))
        }
    }

    private var phoneStateListener = object : PhoneStateListener() {
        @Deprecated("Deprecated in Java")
        override fun onCallStateChanged(state: Int, incomingNumber: String) {
            // TODO Auto-generated method stub
            super.onCallStateChanged(state, incomingNumber)
            when (state) {
                TelephonyManager.CALL_STATE_RINGING -> {
                    Toast.makeText(
                        applicationContext,
                        "Phone RINGING",
                        Toast.LENGTH_LONG
                    ).show()
                }
                TelephonyManager.CALL_STATE_OFFHOOK -> {
                    callState = "offHook"
                    Toast.makeText(
                        applicationContext,
                        "Phone Offhook",
                        Toast.LENGTH_LONG
                    ).show()
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
                    callState = "idle"
//                    Toast.makeText(
//                        applicationContext,
//                        "Phone IDLE",
//                        Toast.LENGTH_LONG
//                    ).show()
                    audioManager.isSpeakerphoneOn = false
                }
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

    private fun checkActiveEMS(): Boolean {
        for (item in Passing.emergencyMessageSetupList) {
            if (item.activeEMS) {
                return true
            }
        }
        return false
    }

    val vreServiceActiveTextTimer = object : CountDownTimer(
        7_000,
        1000
    ) {
        override fun onTick(millisUntilFinished: Long) {
        }
        override fun onFinish() {
            vreServiceActiveText.text = "VRE Service is ON - Listening for keyphrases..."
        }
    }

    val vreServiceSendingTextTimer = object : CountDownTimer(
        5_000,
        1000
    ) {
        override fun onTick(millisUntilFinished: Long) {
        }
        override fun onFinish() {
            vreServiceActiveText.text = "Performing Emergency Message..."
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

    private fun findKeyPhraseMatch(incomingSpeech: String?): KeyPhrase? {
        for (emergencySetup in Passing.emergencyMessageSetupList) {
            for (phrase in emergencySetup.selectedKeyPhraseList) {
                if (Passing.selectedEmergencyMessageSetup.activeEMS) {
                    if (incomingSpeech?.contains(phrase.toString(), true) == true) {
                        Passing.vreActivatedEMS = emergencySetup
                        return phrase
                    }
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

    /**
     * Shows an AlertDialog window that informs users why the mic permission is required.
     * Selecting 'Ok' will ask for the permission
     * Selecting 'Cancel' will close the window
     */
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