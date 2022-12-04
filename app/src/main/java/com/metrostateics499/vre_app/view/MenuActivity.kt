package com.metrostateics499.vre_app.view

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.metrostateics499.vre_app.R
import com.metrostateics499.vre_app.model.Passing
import com.metrostateics499.vre_app.model.Passing.locationManager
import com.metrostateics499.vre_app.model.Passing.locationTrackingRequested
import com.metrostateics499.vre_app.utility.LocationGPS
import kotlinx.android.synthetic.main.activity_edit_emergency_message.*
import kotlinx.android.synthetic.main.activity_menu.*

class MenuActivity : AppCompatActivity() {

    private lateinit var speechButton: Button
    private lateinit var latitudeTextView: TextView
    private lateinit var longitudeTextView: TextView
    private var locationPermissionCode = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // This is to hide the action bar
        supportActionBar?.hide()
        setContentView(R.layout.activity_menu)
        Passing.locationManager = LocationGPS(this as Context)

        latitudeTextView = findViewById(R.id.latitudeTextView)
        longitudeTextView = findViewById(R.id.longitudeTextView)
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
    }
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
//            locationResult
            for (location in locationResult.locations) {
                // Update UI
                latitudeTextView.text = location.latitude.toString()
                longitudeTextView.text = location.longitude.toString()
                Passing.latitude = location.latitude.toString()
                Passing.longitude = location.longitude.toString()
            }
        }
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