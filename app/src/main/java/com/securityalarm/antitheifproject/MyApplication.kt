package com.securityalarm.antitheifproject

import android.app.Application
import android.util.Log
import com.antitheftalarm.dont.touch.phone.finder.phonesecurity.R
import com.securityalarm.antitheifproject.ads_manager.AdOpenApp

class MyApplication : Application(){

    override fun onCreate() {
        super.onCreate()
        Log.d("application_class", "onCreate")
//        if (val_ad_native_app_open_screen) {
//            AppOpenManager(this,id_native_app_open_screen)
//            AppOpenManager(this,getString(R.string.app_open_splash))
            AdOpenApp(this,getString(R.string.app_open_splash))
//        }

    }


}