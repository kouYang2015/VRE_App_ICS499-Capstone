package com.metrostateics499.vre_app.utility

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.util.Log
import com.metrostateics499.vre_app.R
import java.util.*

class NotificationAppClosed : Service() {
    private var timer: Timer? = null
    private var timerTask: TimerTask? = null
    private var tag = "Timers"
    private var seconds: Long = 5
    override fun onBind(arg0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        Log.e(tag, "onStartCommand")
        super.onStartCommand(intent, flags, startId)
        notification()
        startTimer()
        return START_STICKY
    }

    override fun onCreate() {
        Log.e(tag, "onCreate")
    }

    override fun onDestroy() {
        Log.e(tag, "onDestroy")
        stopTimerTask()
        super.onDestroy()
    }

    // we are going to use a handler to be able to run in our TimerTask
    val handler: Handler = Handler()
    private fun startTimer() {
        // set a new Timer
        timer = Timer()

        // initialize the TimerTask's job
        initializeTimerTask()

        // schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
        timer!!.schedule(timerTask, 5000, seconds * 1000) //
        // timer.schedule(timerTask, 5000,1000); //
    }

    private fun stopTimerTask() {
        // stop the timer, if it's not already null
        if (timer != null) {
            timer!!.cancel()
            timer = null
        }
    }

    private fun initializeTimerTask() {
        timerTask = object : TimerTask() {
            override fun run() {

                // use a handler to run a toast that shows the current timestamp
                handler.post { // TODO CALL NOTIFICATION FUNC
                    notification()
                }
            }
        }
    }
    private fun notification() {
        lateinit var notificationChannel: NotificationChannel
        lateinit var builder: Notification.Builder
        val channelId = "12345"
        val description = "Test Notification"
        val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as
            NotificationManager
        val intent = Intent(this, LauncherActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = NotificationChannel(channelId, description, NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.lightColor = Color.BLUE
//            notificationChannel.enableVibration(true)
            notificationManager.createNotificationChannel(notificationChannel)
            builder = Notification.Builder(this, channelId).setContentTitle(
                "NOTIFICATION USING " +
                    "KOTLIN"
            ).setContentText("Test Notification").setSmallIcon(androidx.core.R.drawable.notification_bg_normal).setLargeIcon(
                BitmapFactory.decodeResource(
                    this.resources,
                    R.drawable
                        .ic_launcher_background
                )
            ).setContentIntent(pendingIntent)
        }
        notificationManager.notify(12345, builder.build())
    }
}