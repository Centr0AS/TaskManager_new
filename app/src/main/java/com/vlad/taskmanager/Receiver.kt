package com.vlad.taskmanager

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build


class Receiver : BroadcastReceiver() {
    private lateinit var notificationManager : NotificationManager
    private lateinit var notificationChannel : NotificationChannel
    private lateinit var builder : Notification.Builder
    private val channelID = "com.vlad.taskmanager"
    private val description = "Task reminder"


    override fun onReceive(context: Context?, intent: Intent?) {

        val i = Intent(context, MainActivity::class.java)
        val resultPendingIntent = PendingIntent.getActivity(context, 0, i,
            PendingIntent.FLAG_UPDATE_CURRENT)

        if (context != null) {
            notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        }
//        var notifiedTask = intent?.getSerializableExtra("name") as Task
        val nameOfTask = intent?.getStringExtra("name")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = NotificationChannel(channelID, description, NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.GREEN
            notificationChannel.enableVibration(false)
            notificationManager.createNotificationChannel(notificationChannel)

            builder = Notification.Builder(context, channelID)
                .setSmallIcon(R.drawable.ic_calendar)
                .setContentTitle("Менеджер задач")
                .setContentText("Задача $nameOfTask требует вашего внимания!")
                .setContentIntent(resultPendingIntent)
                .setAutoCancel(true)

        }
        else {
            builder = Notification.Builder(context)
                .setSmallIcon(R.drawable.ic_calendar)
                .setContentTitle("Task remind")
                .setContentText("This task is needed your attention!")
        }
        notificationManager.notify(1234,builder.build())
    }

}