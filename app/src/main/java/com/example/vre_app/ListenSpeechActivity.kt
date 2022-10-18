package com.example.vre_app

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
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
        recordAudioPermissionRequest =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            }
        if (ContextCompat.checkSelfPermission
                (
                this,
                android.Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            checkPermissions()
        }
        Log.d("Perm", "Passed permissions")
        speechOnButton = findViewById<Button>(R.id.activateSpeech)
        speechOffButton = findViewById<Button>(R.id.disableSpeech)
        txtResult = findViewById(R.id.speechToTextBox)
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
        val speechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        speechRecognizerIntent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH
        )
        speechRecognizerIntent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE,
            Locale.getDefault()
        )
        //speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_PREFER_OFFLINE, true)
        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(p0: Bundle?) {}

            override fun onBeginningOfSpeech() {
                txtResult!!.setText("")
                txtResult!!.setHint("Listening to speech")
            }

            override fun onRmsChanged(p0: Float) {}

            override fun onBufferReceived(p0: ByteArray?) {}

            override fun onEndOfSpeech() {}

            override fun onError(p0: Int) {}

            override fun onResults(bundle: Bundle?) {
                //Disable button?
                var data = bundle?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                Log.d("SpeechIn onResults", data!![0])
                txtResult.text = data[0]
            }

            override fun onPartialResults(bundle: Bundle?) {
                var data = bundle?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                Log.d("SpeechIn onResults", data!![0])
                txtResult.text = data[0]
            }

            override fun onEvent(p0: Int, p1: Bundle?) {}

        })
        speechOnButton?.setOnClickListener() {
            askRecordAudioPermission()
            speechRecognizer.startListening(speechRecognizerIntent)
            Log.d("SpeechButton", "Started speech reco")
        }
        speechOffButton?.setOnClickListener() {
            //speechRecognizer!!.destroy()
            speechRecognizer.stopListening()
            Log.d("SpeechButton", "Stopped speech reco")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        speechRecognizer!!.destroy() //For now, later on destroy when turn off.
    }

    private fun askRecordAudioPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            /*Permission not granted*/
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    android.Manifest.permission.RECORD_AUDIO
                )
            ) {
                showLocationPermissionRationale()
                Log.d("Perm", "Showing Rationale")
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.RECORD_AUDIO),
                    REQRECORDAUDIOCODE
                )
                Log.d("Perm", "Requesting Perm")
            }
        } else {
            // Permission is granted. Go back to main.
            Log.d("Perm", "Permission is granted")

        }
    }

    private fun showLocationPermissionRationale() {
        AlertDialog.Builder(this)
            .setTitle("Rationale")
            .setMessage("Mic Permission required to use Speech Recognition")
            .setNeutralButton("Ok") { _, _ ->
                recordAudioPermissionRequest.launch(
                    arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION)
                )
            }
            .show()
    }

    private fun checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            askRecordAudioPermission()
/*            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.RECORD_AUDIO),
                REQRECORDAUDIOCODE
            )*/
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQRECORDAUDIOCODE && grantResults.isNotEmpty()
        ) {
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
        }
    }
}