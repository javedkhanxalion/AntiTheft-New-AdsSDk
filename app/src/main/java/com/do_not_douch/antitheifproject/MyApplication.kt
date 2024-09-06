package com.do_not_douch.antitheifproject

import android.app.Application
import android.util.Log
import com.antitheft.alarm.donottouch.findmyphone.protector.smartapp.privacydefender.myphone.R
import com.do_not_douch.antitheifproject.ads_manager.AdOpenApp

class MyApplication : Application(){

    override fun onCreate() {
        super.onCreate()
        Log.d("application_class", "onCreate")

    }


}