package com.do_not_douch.antitheifproject.utilities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager

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
