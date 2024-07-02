package com.securityalarm.antitheifproject.utilities

import android.net.wifi.WifiManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

class WifiStateReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == ConnectivityManager.CONNECTIVITY_ACTION) {
            val isConnected = isNetworkAvailable(context!!)
            if (!isConnected && !isSplashDialog) {
                // Show dialog indicating no internet
                showInternetDialogNew(
                    context,
                    onPositiveButtonClick = {
                        openMobileDataSettings(context )
                    },
                    onNegitiveButtonClick = {
                        openWifiSettings(context )
                    },
                    onCloseButtonClick = {
                    }
                )
            }
        }
    }

}
