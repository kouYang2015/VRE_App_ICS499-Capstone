package com.example.vre_app

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.*

class ListenSpeechActivity : AppCompatActivity() {

    private lateinit var speechOnButton: Button
    private lateinit var speechOffButton: Button
    private lateinit var txtResult: TextView
    private lateinit var speechRecognizer: SpeechRecognizer
    private val REQRECORDAUDIOCODE = 10001
    private lateinit var recordAudioPermissionRequest: ActivityResultLauncher<Array<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listen_speech)
        initializeComponents()
        setListeners()
    }

    override fun onDestroy() {
        super.onDestroy()
        speechRecognizer!!.destroy()
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
            override fun onReadyForSpeech(p0: Bundle?) {}

            override fun onBeginningOfSpeech() {
                txtResult!!.setHint("Listening to speech")
            }

            override fun onRmsChanged(p0: Float) {}

            override fun onBufferReceived(p0: ByteArray?) {}

            override fun onEndOfSpeech() {}

            override fun onError(p0: Int) {}

            override fun onResults(bundle: Bundle?) {
                val data = bundle?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                txtResult.text = data!![0]
            }

            override fun onPartialResults(bundle: Bundle?) {}

            override fun onEvent(p0: Int, p1: Bundle?) {}

        })
        speechOnButton?.setOnClickListener() {
            checkAndRequestPermissions()
            speechRecognizer.startListening(speechRecognizerIntent)
        }
        speechOffButton?.setOnClickListener() {
            speechRecognizer.stopListening()
        }
    }

    /**
     * Initializes variables to handle components.
     */
    private fun initializeComponents() {
        recordAudioPermissionRequest =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            }
        speechOnButton = findViewById<Button>(R.id.activateSpeech)
        speechOffButton = findViewById<Button>(R.id.disableSpeech)
        txtResult = findViewById(R.id.speechToTextBox)
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
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
}