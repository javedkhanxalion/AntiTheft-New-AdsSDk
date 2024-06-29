package com.securityalarm.antitheifproject

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.antitheftalarm.dont.touch.phone.finder.phonesecurity.databinding.MainActivityApplicationBinding
import com.bmik.android.sdk.IkmSdkController
import com.securityalarm.antitheifproject.utilities.isInternetAvailable
import com.securityalarm.antitheifproject.utilities.openMobileDataSettings
import com.securityalarm.antitheifproject.utilities.openWifiSettings
import com.securityalarm.antitheifproject.utilities.setStatusBar

class MainActivity : AppCompatActivity() {

    private var binding: MainActivityApplicationBinding? = null

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
    }
    override fun onResume() {
        super.onResume()
        if (!isInternetAvailable(this)) {
            IkmSdkController.setEnableShowResumeAds(false)
            return
        } else {
            IkmSdkController.setEnableShowResumeAds(true)
        }
    }

}
