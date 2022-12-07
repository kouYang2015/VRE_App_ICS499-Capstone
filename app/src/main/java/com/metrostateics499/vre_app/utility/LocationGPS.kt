package com.metrostateics499.vre_app.utility

import android.annotation.SuppressLint
import android.content.Context
import android.os.Looper
import com.google.android.gms.location.*

class LocationGPS(context: Context) {
    private val context: Context = context
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest
    private var startedLocationTracking = false
    init {
        configureLocationRequest()
        setupLocationProviderClient()
    }
    private fun setupLocationProviderClient() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    }
    private fun configureLocationRequest() {
        val locBuilder = LocationRequest.Builder(0)
        locBuilder.setIntervalMillis(UPDATE_INTERVAL_MILLISECONDS)
        locBuilder.setMinUpdateIntervalMillis(FASTEST_UPDATE_INTERVAL_MILLISECONDS)
        locBuilder.setPriority(Priority.PRIORITY_HIGH_ACCURACY)
        locationRequest = locBuilder.build()
    }
    @SuppressLint("MissingPermission")
    fun startLocationTracking(locationCallback: LocationCallback) {
        if (!startedLocationTracking) {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
            this.locationCallback = locationCallback
            startedLocationTracking = true
        }
    }
    fun stopLocationTracking() {
        if (startedLocationTracking) {
            fusedLocationClient.removeLocationUpdates(locationCallback)
            startedLocationTracking = false
        }
    }
    companion object {
        const val UPDATE_INTERVAL_MILLISECONDS: Long = 0
        const val FASTEST_UPDATE_INTERVAL_MILLISECONDS = UPDATE_INTERVAL_MILLISECONDS / 2
    }
}