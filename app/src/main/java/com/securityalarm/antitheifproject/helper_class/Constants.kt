package com.securityalarm.antitheifproject.helper_class

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Handler
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
//import com.securityalarm.antitheifproject.Admin.Companion.isPassword
import com.securityalarm.antitheifproject.service.SystemEventsService
import com.securityalarm.antitheifproject.service.SystemEventsService.Companion.isAvailable
import com.securityalarm.antitheifproject.utilities.CAMERA_PERMISSION
import com.securityalarm.antitheifproject.utilities.FlashlightManager
import java.io.File

object Constants {
    private var handler: Handler? = null
    const val Anti_Touch = "Anti_Touch"
    const val Apppurchase = "apppurchase"
    const val Full_Battery = "Full_Battery"
    const val Intruder_Alarm = "Intruder_Alarm"
    const val No_of_attempts = "No_of_attempts"
    const val PRODUCT_ID = "remove_ads"
    const val SHARED_PREFS = "pref"
    const val Tone_Selected = "Tone_Selected"
    const val User_Value = "userpass"


//    const val Intruder_Selfie = "home_intruder"
//    const val Pocket_Detection = "home_pocket"
//    const val Wrong_Password_Detection = "home_password"
//    const val Motion_Detection = "home_motion"
//    const val Whistle_Detection = "home_whistle"
//    const val HandFree_Detection = "home_handfree"
//    const val Clap_Detection = "home_clap"
//    const val Remove_Charger = "home_remove"
//    const val Battery_Detection = "home_battery"
//    const val Wifi_Alarm = "Wifi Alarm"
//    const val All_Alarm_Sound = "All_Alarm_Sound"

    const val Intruder_Selfie = "Intruder"
    const val Pocket_Detection = "Pocket"
    const val Wrong_Password_Detection = "Password"
    const val Motion_Detection = "Motion"
    const val Whistle_Detection = "Whistle"
    const val HandFree_Detection = "HandFree"
    const val Clap_Detection = "Clap"
    const val Remove_Charger = "Remove Charger"
    const val Battery_Detection = "Battery"
    const val Wifi_Alarm = "Wifi Alarm"
    const val All_Alarm_Sound = "All_Alarm_Sound"


    const val Id_Intruder_Selfie = "home_intruder"
    const val Id_Pocket_Detection = "home_pocket"
    const val Id_Wrong_Password_Detection = "home_password"
    const val Id_Motion_Detection = "home_motion"
    const val Id_Whistle_Detection = "home_whistle"
    const val Id_HandFree_Detection = "home_handfree"
    const val Id_Clap_Detection = "home_clap"
    const val Id_Remove_Charger = "home_remove"
    const val Id_Battery_Detection = "home_battery"
    const val Id_Wifi_Alarm = "Wifi Alarm"
    const val Id_All_Alarm_Sound = "All_Alarm_Sound"

    const val Pocket_Detection_Flash = "Pocket Detection Flash"
    const val Intruder_Alarm_Flash = "Intruder Alarm Flash"
    const val Motion_Detection_Flash = "Motion Detection Flash"
    const val Whistle_Detection_Flash = "Whistle Detection Flash"
    const val HandFree_Detection_Flash = "HandFree Detection Flash"
    const val Clap_Detection_Flash = "Clap Detection Flash"
    const val Remove_Charger_Flash = "Remove Charger Flash"
    const val Battery_Detection_Flash = "Battery Detection Flash"
    const val Wifi_Alarm_Flash = "Wifi Alarm Flash"


    const val Intruder_Alarm_Vibration = "Intruder Alarm Vibration"
    const val Pocket_Detection_Vibration = "Pocket Detection Vibration"
    const val Motion_Detection_Vibration = "Motion Detection Vibration"
    const val Whistle_Detection_Vibration = "Whistle Detection Vibration"
    const val HandFree_Detection_Vibration = "HandFree Detection Vibration"
    const val Clap_Detection_Vibration = "Clap Detection Vibration"
    const val Remove_Charger_Vibration = "Remove Charger Vibration"
    const val Battery_Detection_Vibration = "Battery Detection Vibration"
    const val Wifi_Alarm_Vibration = "Wifi Alarm Vibration"

    const val Pocket_Detection_Check = "Pocket Detection Check"
    const val Motion_Detection_Check = "Motion Detection Check"
    const val Whistle_Detection_Check = "Whistle Detection Check"
    const val HandFree_Detection_Check = "HandFree Detection Check"
    const val Clap_Detection_Check = "Clap Detection Check"
    const val Remove_Charger_Check = "Remove Charger Check"
    const val Battery_Detection_Check = "Battery Detection Check"
    const val Wifi_Alarm_Check = "Wifi Alarm Check"

    const val All_Alarm_Sound_Check = "All_Alarm_Sound"

    var count = 1

    var mp: MediaPlayer? = null

    @JvmField
    var Vibration_Status = false

    @JvmField
    var Pocket_Status = false

    @JvmField
    var Light_Status = false
    var isAppPurchased = false

    @JvmStatic
    fun startMediaPlayer() {
        try {
//            if(isPassword == true) {
            val mediaPlayer = mp
            if (mediaPlayer != null && !mediaPlayer.isPlaying) {
                mp?.start()
                mp?.setOnCompletionListener {
//                        isPassword = true
                }
//                }
            }
        } catch (unused: Exception) {
            unused.printStackTrace()
        }
    }

    fun startMediaPlayer(context: Context) {
        try {
            val mediaPlayer = mp
            if (mediaPlayer != null && !mediaPlayer.isPlaying) {
                mp?.start()
                mp?.setOnCompletionListener {
                    setFlash(context, false)
                    isAvailable = true
                }
            }
        } catch (unused: Exception) {
            unused.printStackTrace()
        }
    }

    @JvmStatic
    fun pauseMediaPlayer() {
        try {
            val mediaPlayer = mp
            if (mediaPlayer != null && mediaPlayer.isPlaying) {
                mp?.pause()
            }
        } catch (unused: Exception) {
            unused.printStackTrace()
        }
    }

    fun setFlash(context: Context, isOn: Boolean) {
        if (ContextCompat.checkSelfPermission(context, CAMERA_PERMISSION) == 1) {
            return
        }
        try {
            FlashlightManager.setFlash(context, isOn)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @JvmStatic
    fun isMyServiceRunning(context: Context, cls: Class<*>): Boolean {
        for (runningServiceInfo in (context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager).getRunningServices(
            Int.MAX_VALUE
        )) {
            if (SystemEventsService::class.java.name == runningServiceInfo.service.className) {
                return true
            }
        }
        return false
    }

    @JvmStatic
    fun isMainServiceRunning(context: Context): Boolean {
        try {
            for (runningServiceInfo in (context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager).getRunningServices(
                Int.MAX_VALUE
            )) {
                if (SystemEventsService::class.java.name == runningServiceInfo.service.className) {
                    return true
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    @JvmStatic
    fun Fragment.isServiceRunning(): Boolean {
        try {
            for (runningServiceInfo in (context?.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager).getRunningServices(
                Int.MAX_VALUE
            )) {
                if (SystemEventsService::class.java.name == runningServiceInfo.service.className) {
                    return true
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    fun shareApp(context: Context) {
        val intent = Intent()
        intent.action = "android.intent.action.SEND"
        //        intent.putExtra("android.intent.extra.TEXT", context.getString(R.string.heycheckoutthisappat) + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
        intent.type = "text/plain"
        context.startActivity(intent)
    }

    fun rateApp(context: Context) {
        val intent = Intent("android.intent.action.VIEW")
        intent.data =
            Uri.parse("https://play.google.com/store/apps/details?id=" + context.packageName)
        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        }
    }

    fun showPrivacyPolicy(context: Context) {
        context.startActivity(
            Intent(
                "android.intent.action.VIEW",
                Uri.parse("https://ww.google.com")
            )
        )
    }

    // Define the extension function
    fun Fragment?.getAntiTheftDirectory(): File? {
        return this?.context?.getExternalFilesDir(null)?.let { File(it, "Anti Theft") }
    }

}