package com.do_not_douch.antitheifproject

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.antitheft.alarm.donottouch.findmyphone.protector.smartapp.privacydefender.myphone.databinding.MainActivityApplicationBinding
import com.do_not_douch.antitheifproject.utilities.WifiStateReceiver
import com.do_not_douch.antitheifproject.utilities.setStatusBar

class MainActivity : AppCompatActivity() {

    private var binding: MainActivityApplicationBinding? = null
    private val wifiStateReceiver = WifiStateReceiver()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityApplicationBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            setStatusBar()
            window.decorView.apply {
                systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
            }
        }
        // Register the receiver
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(wifiStateReceiver, filter)
    }
    override fun onResume() {
        super.onResume()
    }

    override fun onDestroy() {
        super.onDestroy()
        // Unregister the receiver
        unregisterReceiver(wifiStateReceiver)
    }


}
