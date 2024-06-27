package com.securityalarm.antitheifproject

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat
import com.antitheftalarm.dont.touch.phone.finder.phonesecurity.R
import com.bmik.android.sdk.core.fcm.BaseIkFirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.securityalarm.antitheifproject.utilities.isAppInBackground
import java.io.IOException
import java.net.URL

open class MyFirebaseMessagingService : BaseIkFirebaseMessagingService() {

    val FCM_PARAM = "picture"
    private val CHANNEL_NAME = "FCM"
    private val CHANNEL_DESC = "Firebase Cloud Messaging"
    private var numMessages = 0

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        if(isAppInBackground()){
            val notification = remoteMessage.notification
            val data = remoteMessage.data
            Log.d("FROM", remoteMessage.from!!)
            sendNotification(notification, data)
        } else {
            return
        }
    }

    private fun sendNotification(
        notification: RemoteMessage.Notification?,
        data: Map<String, String>
    ) {
        val bundle = Bundle()
        bundle.putString(FCM_PARAM, data[FCM_PARAM])
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtras(bundle)
        val pendingIntent =
            PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val notificationBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(this, "default")
                .setContentTitle(notification!!.title)
                .setContentText(notification.body)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)) //.setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.win))
                .setContentIntent(pendingIntent)
                .setContentInfo("Hello")
                .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
                .setColor(getColor(R.color.color_ads))
                .setLights(Color.RED, 1000, 300)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setNumber(++numMessages)
                .setSmallIcon(R.drawable.ic_notification)
        try {
            val picture = data[FCM_PARAM]
            if (picture != null && "" != picture) {
                val url = URL(picture)
                val bigPicture = BitmapFactory.decodeStream(url.openConnection().getInputStream())
                notificationBuilder.setStyle(
                    NotificationCompat.BigPictureStyle().bigPicture(bigPicture)
                        .setSummaryText(notification.body)
                )
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "default", CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = CHANNEL_DESC
            channel.setShowBadge(true)
            channel.canShowBadge()
            channel.enableLights(true)
            channel.lightColor = Color.RED
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500)
            assert(notificationManager != null)
            notificationManager.createNotificationChannel(channel)
        }
        assert(notificationManager != null)
        notificationManager.notify(0, notificationBuilder.build())
    }

    override fun handleIntentSdk(intent: Intent) {
        super.handleIntentSdk(intent)
    }
    
    override fun splashActivityClass(): Class<*>? {
        return MainActivity::class.java
    }
}
