package com.do_not_douch.antitheifproject.model

import com.antitheft.alarm.donottouch.findmyphone.protector.smartapp.privacydefender.myphone.R

data class LanguageAppModel(
    var country_name: String,
    var country_code: String,
    var country_flag: Int = R.drawable.clap_detection__icon,
    var check: Boolean
)