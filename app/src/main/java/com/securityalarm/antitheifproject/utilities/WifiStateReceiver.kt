package com.securityalarm.antitheifproject.utilities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager

class WifiStateReceiver : BroadcastReceiver() {

    companion object {
        const val WIFI_STATE_CHANGE_ACTION = "com.example.app.WIFI_STATE_CHANGED"
        const val EXTRA_WIFI_STATE = "extra_wifi_state"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == WifiManager.WIFI_STATE_CHANGED_ACTION) {
            val wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN)

            if (wifiState == WifiManager.WIFI_STATE_DISABLED) {
                // WiFi is disabled, send local broadcast
                val localIntent = Intent(WIFI_STATE_CHANGE_ACTION).apply {
                    putExtra(EXTRA_WIFI_STATE, wifiState)
                }
                context?.sendBroadcast(localIntent)
            }
        }
    }
}
