package com.metrostateics499.vre_app.view

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.metrostateics499.vre_app.R
import com.metrostateics499.vre_app.model.Passing
import com.metrostateics499.vre_app.utility.LocationGPS
import github.com.vikramezhil.dks.speech.Dks
import github.com.vikramezhil.dks.speech.DksListener
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.android.synthetic.main.activity_menu.*

class MenuActivity : AppCompatActivity() {

    private lateinit var speechButton: Button
    private lateinit var latitudeValueTextView: TextView
    private lateinit var longitudeValueTextView: TextView
    private lateinit var coordinatesDateTimeTextView: TextView
    private var locationPermissionCode = 2
    private lateinit var locationManager: LocationGPS

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        setListeners()
    }

    override fun onPostResume() {
        super.onPostResume()
        checkIfPingingLocation()
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
                    Toast.LENGTH_SHORT
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

    }

    var dks = Dks(application, supportFragmentManager, object: DksListener {
        override fun onDksLiveSpeechResult(liveSpeechResult: String) {
            Log.d("DKS", "Speech result - $liveSpeechResult")
        }

        override fun onDksFinalSpeechResult(speechResult: String) {
            Log.d("DKS", "Final speech result - $speechResult")
        }

        override fun onDksLiveSpeechFrequency(frequency: Float) {
            Log.d("DKS", "frequency - $frequency")
        }

        override fun onDksLanguagesAvailable(defaultLanguage: String?, supportedLanguages: ArrayList<String>?) {
            Log.d("DKS", "defaultLanguage - $defaultLanguage")
            Log.d("DKS", "supportedLanguages - $supportedLanguages")
        }

        override fun onDksSpeechError(errMsg: String) {
            Log.d("DKS", "errMsg - $errMsg")
        }
    })

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
    }
}