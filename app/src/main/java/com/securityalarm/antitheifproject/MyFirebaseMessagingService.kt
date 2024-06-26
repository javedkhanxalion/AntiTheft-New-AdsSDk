package com.securityalarm.antitheifproject

import android.content.Intent
import com.bmik.android.sdk.core.fcm.BaseIkFirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

open class MyFirebaseMessagingService : BaseIkFirebaseMessagingService() {
    
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
      super.onMessageReceived(remoteMessage)

    }

    override fun handleIntentSdk(intent: Intent) {
        super.handleIntentSdk(intent)
    }
    
    override fun splashActivityClass(): Class<*>? {
        return MainActivity::class.java
    }
}
