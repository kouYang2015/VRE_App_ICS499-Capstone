package com.metrostateics499.vre_app.utility

import android.app.Service
import android.content.Intent
import android.os.AsyncTask
import android.os.Build
import android.os.IBinder
import android.speech.tts.TextToSpeech
import android.telephony.SmsManager
import android.util.Log
import android.widget.Toast
import com.metrostateics499.vre_app.model.Passing
import com.metrostateics499.vre_app.model.data.EmergencyMessageSetup
import java.util.*

class ProcessEmergencyMessageService : Service(), TextToSpeech.OnInitListener {
    private val tag = "ProcessEmergencyMessage"
    private var textToSpeech: TextToSpeech? = null
    private lateinit var coordinatesLinks: String
    private lateinit var coordinatesDate: String
    private lateinit var emergencySetup: EmergencyMessageSetup

    override fun onCreate() {
        performEmergencyMessage()
        super.onCreate()
    }

    override fun onDestroy() {
        // TODO Auto-generated method stub
        if (textToSpeech != null) {
            textToSpeech!!.stop()
            textToSpeech!!.shutdown()
        }
        super.onDestroy()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.e(tag, "onStartCommand")
        super.onStartCommand(intent, flags, startId)
        return START_STICKY
    }

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    private fun performEmergencyMessage() {
        this.emergencySetup = Passing.vreActivatedEMS
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
                            applicationContext, "Emergency Message Sent",
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
                sendUpdateCoordinatesLoop(emergencySetup)
            }
        }
    }

    private fun sendUpdateCoordinatesLoop(emergencySetup: EmergencyMessageSetup) {
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
}