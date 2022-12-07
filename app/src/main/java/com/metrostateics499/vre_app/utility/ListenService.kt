//package com.metrostateics499.vre_app.utility
//
//import android.app.Application
//import android.app.Service
//import android.content.Intent
//import android.location.Location
//import android.media.AudioManager
//import android.net.Uri
//import android.os.AsyncTask
//import android.os.Build
//import android.os.CountDownTimer
//import android.os.IBinder
//import android.speech.SpeechRecognizer
//import android.speech.tts.TextToSpeech
//import android.telephony.PhoneStateListener
//import android.telephony.SmsManager
//import android.telephony.TelephonyManager
//import android.util.Log
//import android.widget.Button
//import android.widget.TextView
//import android.widget.Toast
//import androidx.activity.result.ActivityResultLauncher
//import androidx.localbroadcastmanager.content.LocalBroadcastManager
//import com.google.android.gms.location.LocationCallback
//import com.google.android.gms.location.LocationResult
//import com.metrostateics499.vre_app.model.Passing
//import com.metrostateics499.vre_app.model.data.EmergencyMessageSetup
//import com.metrostateics499.vre_app.model.data.KeyPhrase
//import github.com.vikramezhil.dks.speech.Dks
//import github.com.vikramezhil.dks.speech.DksListener
//import kotlinx.android.synthetic.main.activity_menu.*
//import java.text.SimpleDateFormat
//import java.util.*
//
//
//class ListenService : Service(), TextToSpeech.OnInitListener {
//    private lateinit var emergencyFound: EmergencyMessageSetup
//    private lateinit var txtResult: TextView
//    private lateinit var speechRecognizer: SpeechRecognizer
//    private val REQRECORDAUDIOCODE = 10001
//    private lateinit var recordAudioPermissionRequest: ActivityResultLauncher<Array<String>>
//    private val requestCall = 1
//    private var textToSpeech: TextToSpeech? = null
//    private lateinit var audioManager: AudioManager
//    private var myHashAlarm: HashMap<String, String> = HashMap()
//    private var warningMessage: String = "Voice Recognition Emergency Services " +
//            "have been activated. Your emergency message and your location has " +
//            "been sent to all your emergency contacts."
//
//    private var callMessageTTS: String? = null
//    private lateinit var telephonyManager: TelephonyManager
//
//    private lateinit var speechButton: Button
//    private lateinit var latitudeValueTextView: TextView
//    private lateinit var longitudeValueTextView: TextView
//    private lateinit var coordinatesDateTimeTextView: TextView
//    private var locationPermissionCode = 2
//    private lateinit var locationManager: LocationGPS
//    private lateinit var app: Application
//    private lateinit var dks: Dks
//
//    val ACTION_LOCATION_BROADCAST: String =
//        ListenService::class.java.name + "LocationBroadcast"
//    val EXTRA_LATITUDE = "extra_latitude"
//    val EXTRA_LONGITUDE = "extra_longitude"
//
//
//    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
//        initializeComponents()
//        setListeners()
//        onTaskRemoved(intent)
//        Toast.makeText(
//            applicationContext, "This is a Service running in Background",
//            Toast.LENGTH_SHORT
//        ).show()
//        return START_STICKY
//
//
//    }
//    override fun onBind(intent: Intent): IBinder? {
//        // TODO: Return the communication channel to the service.
//        throw UnsupportedOperationException("Not yet implemented")
//    }
//    override fun onTaskRemoved(rootIntent: Intent) {
//        val restartServiceIntent = Intent(applicationContext, this.javaClass)
//        restartServiceIntent.setPackage(packageName)
//        startService(restartServiceIntent)
//        super.onTaskRemoved(rootIntent)
//    }
//
//    private fun initializeComponents() {
////        txtResult = findViewById(R.id.vreServiceActiveText)
////        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
//        textToSpeech = TextToSpeech(this, this)
//        telephonyManager = this.getSystemService(TELEPHONY_SERVICE) as TelephonyManager
//        audioManager = getSystemService(AUDIO_SERVICE) as AudioManager
//
//        myHashAlarm[TextToSpeech.Engine.KEY_PARAM_STREAM] =
//            AudioManager.STREAM_VOICE_CALL.toString()
//        if(Passing.locationTrackingRequested){
//            locationManager.startLocationTracking(locationCallback)
//        }
//
////        requestSmsPermission()
////        requestCallPermission()
//
//        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE)
//    }
//
//    private fun setListeners(){
//        dks = Dks(
//            application, supportFragmentManager,
//            object : DksListener {
//                override fun onDksLiveSpeechResult(liveSpeechResult: String) {
//                    Log.d("DKS", "Speech result - $liveSpeechResult")
//                    if (findKeyPhraseMatch(liveSpeechResult) != null) {
//                        vreServiceActiveText.text = buildString {
//                            findKeyPhraseMatch(liveSpeechResult)?.let {
//                                append(
//                                    "KeyPhrase Recognized! - Processing Emergency Message...\n",
//                                    it.phrase
//                                )
//                            }
//                        }
//                        vreServiceActiveTextTimer.start()
//                        findEmergencyMessageSetupMatch(
//                            findKeyPhraseMatch(liveSpeechResult)?.phrase
//                        )?.let {
//                            performEmergencyMessage(
//                                it
//                            )
//                        }
//                    } else {
//                        vreServiceActiveText.text = "VRE Service is ON - Not Recognized" +
//                                " - Still listening..."
//                    }
//                }
//
//                override fun onDksFinalSpeechResult(speechResult: String) {
//                    Log.d("DKS", "Final speech result - $speechResult")
//                    if (findKeyPhraseMatch(speechResult) != null) {
//                        vreServiceActiveText.text = buildString {
//                            findKeyPhraseMatch(speechResult)?.let {
//                                append(
//                                    "KeyPhrase Recognized! - Processing Emergency Message...\n",
//                                    it.phrase
//                                )
//                            }
//                        }
//                        vreServiceActiveTextTimer.start()
//                        findEmergencyMessageSetupMatch(
//                            findKeyPhraseMatch(speechResult)?.phrase
//                        )?.let {
//                            performEmergencyMessage(
//                                it
//                            )
//                        }
//                    } else {
//                        vreServiceActiveText.text = "VRE Service is ON - Not Recognized" +
//                                " - Still listening..."
//                        vreServiceActiveTextTimer.start()
//                    }
//                }
//
//                override fun onDksLiveSpeechFrequency(frequency: Float) {
//
//                    Log.d("DKS", "frequency - $frequency")
//                }
//
//                override fun onDksLanguagesAvailable(
//                    defaultLanguage: String?,
//                    supportedLanguages: ArrayList<String>?
//                ) {
//                    Log.d("DKS", "defaultLanguage - $defaultLanguage")
//                    Log.d("DKS", "supportedLanguages - $supportedLanguages")
//                }
//
//                override fun onDksSpeechError(errMsg: String) {
//                    Log.d("DKS", "errMsg - $errMsg")
//                }
//            }
//        )
//    }
//    val vreServiceActiveTextTimer = object : CountDownTimer(5_000, 1000) {
//        override fun onTick(millisUntilFinished: Long) {
//        }
//        override fun onFinish() {
//            vreServiceActiveText.text = "VRE Service is ON - Listening for keyphrases..."
//        }
//    }
//
//    val vreServiceSendingTextTimer = object : CountDownTimer(5_000, 1000) {
//        override fun onTick(millisUntilFinished: Long) {
//        }
//        override fun onFinish() {
//            vreServiceActiveText.text = "Performing Emergency Message..."
//        }
//    }
//
//    private fun performEmergencyMessage(emergencySetup: EmergencyMessageSetup) {
//
//        var coordinatesLinks: String
//        var coordinatesDate: String
//
//        if (emergencySetup != null) {
//            if (emergencySetup.activeSendText) {
//                if (emergencySetup.activeGPS) {
//                    coordinatesLinks =
//                        "My last known location: www.google.com/maps/place/" +
//                                Passing.latitude + "," + Passing.longitude +
//                                " or http://maps.apple.com/?daddr=" +
//                                Passing.latitude + "," + Passing.longitude
//                    coordinatesDate =
//                        "Last known coordinates were taken on date: \n" +
//                                Passing.dateTimeGPS +
//                                "\nLatitude: " + Passing.latitude +
//                                "\nLongitude: " + Passing.longitude
//                } else {
//                    coordinatesLinks = "Last Known Location: Unavailable or Deactivated "
//                    coordinatesDate = ""
//                }
//                for (contact in emergencySetup.selectedContactList) {
//                    try {
//                        val smsManager: SmsManager =
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//                                applicationContext.getSystemService(
//                                    SmsManager::class.java
//                                )
//                            } else {
//                                SmsManager.getDefault()
//                            }
//                        val emergencyTextMessage =
//                            "VOICE RECOGNITION EMERGENCY: " +
//                                    emergencySetup.getCustomTextListString()
//                        var textMessages: List<String>
//
//                        if (emergencyTextMessage.length > 160 && emergencySetup.activeGPS) {
//                            textMessages = splitEmergencyTextMessage(emergencyTextMessage)
//                            textMessages = (
//                                    textMessages +
//                                            coordinatesLinks +
//                                            coordinatesDate
//                                    )
//                        } else if (emergencyTextMessage.length > 160 &&
//                            !emergencySetup.activeGPS
//                        ) {
//                            textMessages = splitEmergencyTextMessage(emergencyTextMessage)
//                            textMessages = (textMessages + coordinatesLinks)
//                        } else if (emergencyTextMessage.length <= 160 &&
//                            !emergencySetup.activeGPS
//                        ) {
//                            textMessages =
//                                listOf(emergencyTextMessage, coordinatesLinks)
//                        } else {
//                            textMessages =
//                                listOf(
//                                    emergencyTextMessage,
//                                    coordinatesLinks,
//                                    coordinatesDate
//                                )
//                        }
//                        for (textItem in textMessages) {
//                            smsManager.sendTextMessage(
//                                contact.phoneNumber, null,
//                                textItem, null, null
//                            )
//                            Thread.sleep(1_500)
//                        }
//                        Toast.makeText(
//                            applicationContext, "Emergency Message Sent",
//                            Toast.LENGTH_LONG
//                        ).show()
//                    } catch (e: Exception) {
//                        Toast.makeText(
//                            applicationContext,
//                            "Missing Contact Data" +
//                                    e.message.toString(),
//                            Toast.LENGTH_LONG
//                        )
//                            .show()
//                    }
//                }
//            }
//            if (emergencySetup.activeGPS && emergencySetup.activeSendText) {
//                emergencySetup.activePingLocation = true
//                switchMenuEMSPingingLocation.isChecked = true
//                AsyncTask.execute {
//                    while (emergencySetup.activePingLocation) {
//                        Thread.sleep(120_000)
//                        if (emergencySetup.activePingLocation) {
//                            val smsManager: SmsManager =
//                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//                                    applicationContext.getSystemService(
//                                        SmsManager::class.java
//                                    )
//                                } else {
//                                    SmsManager.getDefault()
//                                }
//                            coordinatesLinks =
//                                "New Location Ping: www.google.com/maps/place/" +
//                                        Passing.latitude + "," + Passing.longitude +
//                                        " or http://maps.apple.com/?daddr=" +
//                                        Passing.latitude + "," + Passing.longitude
//                            coordinatesDate =
//                                "Coordinates Timestamp: \n" +
//                                        Passing.dateTimeGPS +
//                                        "\nLatitude: " + Passing.latitude +
//                                        "\nLongitude: " + Passing.longitude
//                            for (contact in emergencySetup.selectedContactList) {
//                                smsManager.sendTextMessage(
//                                    contact.phoneNumber, null,
//                                    coordinatesLinks, null, null
//                                )
//                                Thread.sleep(1_500)
//                                smsManager.sendTextMessage(
//                                    contact.phoneNumber, null,
//                                    coordinatesDate, null, null
//                                )
//                            }
//                            Toast.makeText(
//                                applicationContext,
//                                "Location Ping Sent",
//                                Toast.LENGTH_SHORT
//                            ).show()
//                        }
//                    }
//                }
//            }
//            if (emergencySetup.activeAudioWarningMessage) {
//                playActivationWarningMessage()
//            }
//            if (emergencySetup.activeCall) {
//                Toast.makeText(
//                    applicationContext,
//                    "Calling...",
//                    Toast.LENGTH_SHORT
//                ).show()
//                //                        callMessageTTS?.let { saveToAudioFile(it) }
//                Thread.sleep(2_000)
//                for (contact in emergencySetup.selectedContactList) {
//                    makePhoneCall(
//                        contact.phoneNumber
//                    )
//                }
//            }
//        }
//    }
//
//    private fun makePhoneCall(phoneNumber: String) {
//        if (phoneNumber.trim { it <= ' ' }.isNotEmpty()) {
//                val dial = "tel:$phoneNumber"
//                startActivity(Intent(Intent.ACTION_CALL, Uri.parse(dial)))
//            }
//    }
//
//    /**
//     * Shows an AlertDialog window that informs users why the mic permission is required.
//     * Selecting 'Ok' will ask for the permission
//     * Selecting 'Cancel' will close the window
//     */
//
//    private fun playActivationWarningMessage() {
//        val streamMaxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
//        audioManager.mode = AudioManager.MODE_NORMAL
//        audioManager.isSpeakerphoneOn = true
//        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, streamMaxVolume, 0)
//        textToSpeech?.speak(warningMessage, TextToSpeech.QUEUE_FLUSH, myHashAlarm)
//        textToSpeech?.speak(warningMessage, TextToSpeech.QUEUE_ADD, myHashAlarm)
//    }
//
//    private var phoneStateListener = object : PhoneStateListener() {
//        override fun onCallStateChanged(state: Int, incomingNumber: String) {
//            // TODO Auto-generated method stub
//            super.onCallStateChanged(state, incomingNumber)
//            when (state) {
//                TelephonyManager.CALL_STATE_RINGING -> {
//                    Toast.makeText(
//                        applicationContext,
//                        "Phone RINGING",
//                        Toast.LENGTH_LONG
//                    ).show()
//                }
//                TelephonyManager.CALL_STATE_OFFHOOK -> {
//                    Toast.makeText(
//                        applicationContext,
//                        "Phone Offhook",
//                        Toast.LENGTH_LONG
//                    ).show()
//                    Thread.sleep(1_000)
//                    val streamMaxVolume = audioManager
//                        .getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL)
//                    audioManager.mode = AudioManager.MODE_IN_CALL
//                    audioManager.isSpeakerphoneOn = true
//                    audioManager.setStreamVolume(
//                        AudioManager.STREAM_VOICE_CALL,
//                        streamMaxVolume, 0
//                    )
//
//                    Thread.sleep(5_000)
////
////                    textToSpeech?.speak(callMessageTTS, TextToSpeech.QUEUE_FLUSH, myHashAlarm)
////                    textToSpeech?.speak(callMessageTTS, TextToSpeech.QUEUE_ADD, myHashAlarm)
//                }
//                TelephonyManager.CALL_STATE_IDLE -> {
//                    Toast.makeText(
//                        applicationContext,
//                        "Phone IDLE",
//                        Toast.LENGTH_LONG
//                    ).show()
//                    audioManager.isSpeakerphoneOn = false
//                }
//            }
//        }
//    }
//
//    override fun onInit(status: Int) {
//        if (status == TextToSpeech.SUCCESS) {
//            val result = textToSpeech!!.setLanguage(Locale.US)
//
//            if (result == TextToSpeech.LANG_MISSING_DATA ||
//                result == TextToSpeech.LANG_NOT_SUPPORTED
//            ) {
//                Log.e("TTS", "The Language not supported!")
//            }
//        }
//    }
//    private fun splitEmergencyTextMessage(textMessage: String): List<String> {
//        val size = 160
//        return textMessage.split("(?<=\\G.{$size})".toRegex())
//    }
//
//    private fun findKeyPhraseMatch(incomingSpeech: String?): KeyPhrase? {
//        for (emergencySetup in Passing.emergencyMessageSetupList) {
//            for (phrase in emergencySetup.selectedKeyPhraseList) {
//                if (Passing.selectedEmergencyMessageSetup.activeEMS) {
//                    if (incomingSpeech?.contains(phrase.toString(), true) == true) {
//                        return phrase
//                    }
//                }
//            }
//        }
//        return null
//    }
//
//    private fun findEmergencyMessageSetupMatch(keyPhraseMatch: String?): EmergencyMessageSetup? {
//        for (emergencySetup in Passing.emergencyMessageSetupList) {
//            for (phrase in emergencySetup.selectedKeyPhraseList) {
//                if (keyPhraseMatch?.contains(phrase.toString(), true) == true) {
//                    return emergencySetup
//                }
//            }
//        }
//        return null
//    }
//
//    private fun checkIfPingingLocation() {
//        for (emergencyMessage in Passing.emergencyMessageSetupList) {
//            if (emergencyMessage.activePingLocation) {
//                switchMenuEMSPingingLocation.isChecked = true
//            }
//        }
//    }
//    private fun checkIfGPSSwitchOn() {
//        if (Passing.locationTrackingRequested) {
//            switchMenuGPS.isChecked = true
//        }
//    }
//
//    private val locationCallback = object : LocationCallback() {
//        override fun onLocationResult(locationResult: LocationResult) {
////            locationResult
//            for (location in locationResult.locations) {
//                sendBroadcastLocation(location)
//                // Update UI
//                val date = getCurrentDateTime()
//                val dateString = date.toString("yyyy/MM/dd HH:mm")
//                latitudeValueTextView.text = location.latitude.toString()
//                longitudeValueTextView.text = location.longitude.toString()
//                coordinatesDateTimeTextView.text = dateString
//                Passing.latitude = location.latitude.toString()
//                Passing.longitude = location.longitude.toString()
//
//                Passing.dateTimeGPS = dateString
//            }
//        }
//    }
//
//    private fun sendBroadcastLocation(location: Location?) {
//        if (location != null) {
//            val intent = Intent(ACTION_LOCATION_BROADCAST)
//            intent.putExtra(EXTRA_LATITUDE, location.latitude)
//            intent.putExtra(EXTRA_LONGITUDE, location.longitude)
//            LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
//        }
//    }
//
//    fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
//        val formatter = SimpleDateFormat(format, locale)
//        return formatter.format(this)
//    }
//
//    fun getCurrentDateTime(): Date {
//        return Calendar.getInstance().time
//    }
//}