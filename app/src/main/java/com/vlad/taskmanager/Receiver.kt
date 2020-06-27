package com.vlad.taskmanager

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.getSystemService

class Receiver : BroadcastReceiver() {
    lateinit var notificationManager : NotificationManager
    lateinit var notificationChannel : NotificationChannel
    lateinit var builder : Notification.Builder
    private val channelID = "com.vlad.taskmanager"
    private val description = "Task reminder"



    override fun onReceive(context: Context?, intent: Intent?) {


        //TODO: Notification
//        val notification = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            Notification.Builder(context, "notifya")
//                .setSmallIcon(R.drawable.ic_calendar)
//                .setContentTitle("Task remind")
//                .setContentText("This task needed your attention!")
//
//        } else {
//            TODO("VERSION.SDK_INT < O")
//        }
//        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (context != null) {
            notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = NotificationChannel(channelID, description, NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.GREEN
            notificationChannel.enableVibration(false)
            notificationManager.createNotificationChannel(notificationChannel)

            builder = Notification.Builder(context, channelID)
                .setSmallIcon(R.drawable.ic_calendar)
                .setContentTitle("Менеджер задач")
                .setContentText("Задача требует вашего внимания!")

        }
        else {
            builder = Notification.Builder(context)
                .setSmallIcon(R.drawable.ic_calendar)
                .setContentTitle("Task remind")
                .setContentText("This task needed your attention!")
        }
        notificationManager.notify(1234,builder.build())
    }

//    private fun createChannel() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val name = notify.id.toString() + "_action_edu"
//            val import = NotificationManager.IMPORTANCE_DEFAULT
//            val channel = NotificationChannel(name, name, import)
//
//            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//            notificationManager.createNotificationChannel(channel)
//        }
//    }
}