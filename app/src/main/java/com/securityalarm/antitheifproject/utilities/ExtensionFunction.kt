package com.securityalarm.antitheifproject.utilities

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.provider.Settings
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowInsetsController
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.constraintlayout.utils.widget.ImageFilterView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.airbnb.lottie.LottieAnimationView
import com.antitheftalarm.dont.touch.phone.finder.phonesecurity.R
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.securityalarm.antitheifproject.ads_manager.AdsBanners.isDebug
import com.securityalarm.antitheifproject.helper_class.Constants.Battery_Detection
import com.securityalarm.antitheifproject.helper_class.Constants.Battery_Detection_Check
import com.securityalarm.antitheifproject.helper_class.Constants.Battery_Detection_Flash
import com.securityalarm.antitheifproject.helper_class.Constants.Battery_Detection_Vibration
import com.securityalarm.antitheifproject.helper_class.Constants.Clap_Detection
import com.securityalarm.antitheifproject.helper_class.Constants.Clap_Detection_Check
import com.securityalarm.antitheifproject.helper_class.Constants.Clap_Detection_Flash
import com.securityalarm.antitheifproject.helper_class.Constants.Clap_Detection_Vibration
import com.securityalarm.antitheifproject.helper_class.Constants.HandFree_Detection
import com.securityalarm.antitheifproject.helper_class.Constants.HandFree_Detection_Check
import com.securityalarm.antitheifproject.helper_class.Constants.HandFree_Detection_Flash
import com.securityalarm.antitheifproject.helper_class.Constants.HandFree_Detection_Vibration
import com.securityalarm.antitheifproject.helper_class.Constants.Intruder_Alarm
import com.securityalarm.antitheifproject.helper_class.Constants.Intruder_Alarm_Flash
import com.securityalarm.antitheifproject.helper_class.Constants.Intruder_Alarm_Vibration
import com.securityalarm.antitheifproject.helper_class.Constants.Intruder_Selfie
import com.securityalarm.antitheifproject.helper_class.Constants.Motion_Detection
import com.securityalarm.antitheifproject.helper_class.Constants.Motion_Detection_Check
import com.securityalarm.antitheifproject.helper_class.Constants.Motion_Detection_Flash
import com.securityalarm.antitheifproject.helper_class.Constants.Motion_Detection_Vibration
import com.securityalarm.antitheifproject.helper_class.Constants.Pocket_Detection
import com.securityalarm.antitheifproject.helper_class.Constants.Pocket_Detection_Check
import com.securityalarm.antitheifproject.helper_class.Constants.Pocket_Detection_Flash
import com.securityalarm.antitheifproject.helper_class.Constants.Pocket_Detection_Vibration
import com.securityalarm.antitheifproject.helper_class.Constants.Remove_Charger
import com.securityalarm.antitheifproject.helper_class.Constants.Remove_Charger_Check
import com.securityalarm.antitheifproject.helper_class.Constants.Remove_Charger_Flash
import com.securityalarm.antitheifproject.helper_class.Constants.Remove_Charger_Vibration
import com.securityalarm.antitheifproject.helper_class.Constants.Whistle_Detection
import com.securityalarm.antitheifproject.helper_class.Constants.Whistle_Detection_Check
import com.securityalarm.antitheifproject.helper_class.Constants.Whistle_Detection_Flash
import com.securityalarm.antitheifproject.helper_class.Constants.Whistle_Detection_Vibration
import com.securityalarm.antitheifproject.helper_class.Constants.Wrong_Password_Detection
import com.securityalarm.antitheifproject.helper_class.DbHelper
import com.securityalarm.antitheifproject.model.LanguageAppModel
import com.securityalarm.antitheifproject.model.MainMenuModel
import com.securityalarm.antitheifproject.model.SoundModel
import com.securityalarm.antitheifproject.service.SystemEventsService
import com.securityalarm.antitheifproject.ui.FragmentDetectionSameFunction
import com.securityalarm.antitheifproject.ui.MainMenuFragment
import java.util.Locale


var isSplash = true
var isIntroLanguageShow = true
var isSplashDialog = true

var val_banner_splash_screen = true
var val_banner_main_menu_screen = true
var val_banner_language_screen = true
var val_exit_screen_native = true

var val_native_Full_screen = true
var val_native_intro_screen = true
var val_native_intro_screen1 = true
var val_native_intro_screen2 = true
var val_inter_exit_screen = true

var val_banner_1 = false
var val_inter_main_normal = true
var val_ad_native_loading_screen = true
var val_ad_native_intro_screen = true
var val_ad_native_language_screen = true
var val_ad_native_sound_screen = true
var val_ad_native_intruder_list_screen = true
var val_ad_native_show_image_screen = true
var val_ad_native_intruder_detection_screen = true
var val_ad_native_password_screen = true
var val_ad_native_main_menu_screen = true
var val_exit_dialog_native = true
var val_ad_native_Motion_Detection_screen = true
var val_ad_native_Whistle_Detection_screen = true
var val_ad_native_hand_free_screen = true
var val_ad_native_clap_detection_screen = true
var val_ad_native_Remove_Charger_screen = true
var val_ad_native_Battery_Detection_screen = true
var val_ad_native_Pocket_Detection_screen = true
var isInternetAvailable = true

var counter = 0
var inter_frequency_count = 0
var id_frequency_counter = 3
var id_inter_counter = 3
var id_inter_main_medium = "ca-app-pub-5267896740455550/8084474775"
var id_inter_main_normal = "ca-app-pub-5267896740455550/8084474775"
var id_native_loading_screen = ""
var id_native_intro_screen = ""
var id_native_language_screen = ""
var id_native_sound_screen = ""
var id_native_intruder_list_screen = ""
var id_native_show_image_screen = ""
var id_native_intruder_detection_screen = ""
var id_native_password_screen = ""
var id_native_Pocket_Detection_screen = ""
var id_native_Motion_Detection_screen = ""
var id_native_Whistle_Detection_screen = ""
var id_native_hand_free_screen = ""
var id_native_clap_detection_screen = ""
var id_native_Remove_Charger_screen = ""
var id_native_Battery_Detection_screen = ""
var id_native_main_menu_screen = ""
var id_native_app_open_screen = ""
var id_exit_dialog_native = ""
var id_exit_screen_native: String = if (isDebug()) "ca-app-pub-3940256099942544/2247696110" else ""
var id_banner_language_screen: String = if (isDebug()) "ca-app-pub-3940256099942544/9214589741" else ""
var id_banner_1: String = if (isDebug()) "ca-app-pub-3940256099942544/9214589741" else ""
var id_banner_main_screen: String = if (isDebug()) "ca-app-pub-3940256099942544/6300978111" else ""

var id_native_intro_screen1: String = if (isDebug()) "ca-app-pub-3940256099942544/2247696110" else ""
var id_native_intro_screen2: String = if (isDebug()) "ca-app-pub-3940256099942544/2247696110" else ""
var id_app_open_screen: String = ""
var id_native_intro_screen_full: String = if (isDebug()) "ca-app-pub-3940256099942544/2247696110" else ""
var id_language_scroll_screen_native: String = if (isDebug()) "ca-app-pub-3940256099942544/2247696110" else ""
var id_native_Full_screen: String = if (isDebug()) "ca-app-pub-3940256099942544/2247696110" else ""
var id_banner_splash_screen: String = if (isDebug()) "ca-app-pub-3940256099942544/6300978111" else ""

var language_reload = 0
var Onboarding_Full_Native = 0
var sessionOpenlanguage = 1
var sessionOnboarding = 1
var fisrt_ad_line_threshold = 2
var line_count = 2
var language_native_scroll = 1
var main_native_scroll = 1
var fisrt_ad_line_threshold_main = 2
var line_count_main = 2


var language_bottom = 1
var languageinapp_scroll = 1
var onboarding1_bottom = 1
var onboarding2_bottom = 1
var onboarding3_bottom = 1
var exitdialog_bottom = 1
var thankyou_bottom = 1
var home_native = 1
var intruder_native = 1
var intruderimage_bottom = 1
var pocket_native = 1
var pocket_selectsound_bottom = 1
var password_native = 1
var password_selectsound_bottom = 1
var motion_native = 1
var motion_selectsound_bottom = 1
var whistle_native = 1
var whistle_selectsound_bottom = 1
var handfree_native = 1
var handfree_selectsound_bottom = 1
var clap_native = 1
var clap_selectsound_bottom = 1
var remove_native = 1
var remove_selectsound_bottom = 1
var battery_native = 1
var battery_selectsound_bottom = 1
var test_ui_native = ""
var language_first_r_scroll = ""

const val NOTIFY_CHANNEL_ID = "AppNameBackgroundService"

const val IS_NOTIFICATION = "IS_NOTIFICATION"
const val IS_GRID = "IS_GRID"
const val IS_FIRST = "is_First"
const val IS_INTRO = "is_Intro"
const val LANG_CODE = "language_code"
const val LANG_SCREEN = "LANG_SCREEN"
const val ANTI_TITLE = "ANTI_TITLE"

const val AUDIO_PERMISSION = "android.permission.RECORD_AUDIO"
const val PHONE_PERMISSION = "android.permission.READ_PHONE_STATE"
const val CAMERA_PERMISSION = "android.permission.CAMERA"
const val NOTIFICATION_PERMISSION = "android.permission.POST_NOTIFICATIONS"

var slideImages = arrayOf(
    R.drawable.intro_1,
    R.drawable.intro_2,
    R.drawable.intro_3
)

var introHeading = arrayOf(
    "Pocket Detection",
    "Clap Detection",
    "Motion Detection"
)

var introDetailText = arrayOf(
    "Detect Phone thieves Effortlessly",
    "Find your phone by Clap using AI tech & sounds effects",
    "Anti Pickpocket catch a mobile phone thief"
)

fun Fragment.languageData(): ArrayList<LanguageAppModel> {
    val list = arrayListOf<LanguageAppModel>()

    list.add(LanguageAppModel(getString(R.string.english), "en", R.drawable.usa, false))
    list.add(LanguageAppModel(getString(R.string.spanish), "es", R.drawable.spain, false))
    list.add(LanguageAppModel(getString(R.string.hindi), "hi", R.drawable.india, false))
    list.add(LanguageAppModel(getString(R.string.arabic), "ar", R.drawable.sudi, false))
    list.add(LanguageAppModel(getString(R.string.french), "fr", R.drawable.france, false))
    list.add(LanguageAppModel(getString(R.string.german), "de", R.drawable.germany, false))
    list.add(LanguageAppModel(getString(R.string.japanese), "ja", R.drawable.japan, false))
    list.add(LanguageAppModel(getString(R.string.dutch), "nl", R.drawable.dutch, false))
    list.add(LanguageAppModel(getString(R.string.korean), "ko", R.drawable.japanese, false))
    list.add(LanguageAppModel(getString(R.string.portuguese), "pt", R.drawable.portuguese, false))
    list.add(LanguageAppModel(getString(R.string.chinese), "zh", R.drawable.chinese, false))
    list.add(LanguageAppModel(getString(R.string.italian), "it", R.drawable.italian, false))
    list.add(LanguageAppModel(getString(R.string.russian), "ru", R.drawable.russian, false))
    list.add(LanguageAppModel(getString(R.string.turkey), "tr", R.drawable.turkey, false))
    list.add(LanguageAppModel(getString(R.string.vietnamese), "vi", R.drawable.vietnamese, false))
    list.add(LanguageAppModel(getString(R.string.thai), "th", R.drawable.ukrainian, false))
    list.add(LanguageAppModel(getString(R.string.indonesian), "id", R.drawable.indonesian, false))

    return list
}

fun Fragment.soundData(): ArrayList<SoundModel> {
    val list = arrayListOf<SoundModel>()
    list.add(SoundModel(getString(R.string.tone_1), R.drawable.battery_icon, false))
    list.add(SoundModel(getString(R.string.tone_2), R.drawable.battery_icon, false))
    list.add(SoundModel(getString(R.string.tone_3), R.drawable.battery_icon, false))
    list.add(SoundModel(getString(R.string.tone_4), R.drawable.battery_icon, false))
    list.add(SoundModel(getString(R.string.tone_5), R.drawable.battery_icon, false))
    list.add(SoundModel(getString(R.string.tone_6), R.drawable.battery_icon, false))
    return list
}


@SuppressLint("SuspiciousIndentation")
fun Fragment.getMenuListGrid(dbHelper: DbHelper): ArrayList<MainMenuModel> {
    val list = arrayListOf<MainMenuModel>()
    list.add(
        MainMenuModel(
            getString(R.string.intruder),
            R.drawable.intruder_detector_icon,
            getString(R.string.intruder),
            getString(R.string.detection),
            dbHelper.chkBroadCast(Intruder_Selfie),
            R.drawable.intruder_detector_icon,
            Intruder_Selfie,
            Intruder_Alarm_Flash,
            Intruder_Alarm_Vibration,
            true,
            "home_intruder",
            true,
            getString(R.string.title_intruder),
            "intruder_native",
            "",
            intruder_native,
            intruderimage_bottom
        )
    )
    list.add(
        MainMenuModel(
            Pocket_Detection,
            R.drawable.pocket_detection,
            getString(R.string.pocket),
            getString(R.string.detection),
            dbHelper.chkBroadCast(Pocket_Detection_Check),
            R.drawable.pocket_icon_inter,
            Pocket_Detection_Check,
            Pocket_Detection_Flash,
            Pocket_Detection_Vibration,
            val_ad_native_Pocket_Detection_screen,
            "home_pocket",
            true,
            getString(R.string.title_pocket),
            "pocket_native",
            "pocket_selectsound_bottom",
            pocket_native,
            pocket_selectsound_bottom
        )
    )
    list.add(
        MainMenuModel(
            Wrong_Password_Detection,
            R.drawable.password_detection_icon,
            getString(R.string.password),
            getString(R.string.detection),
            dbHelper.chkBroadCast(Intruder_Alarm),
            R.drawable.wrong_pass_icon_inter,
            Intruder_Alarm,
            Intruder_Alarm_Flash,
            Intruder_Alarm_Vibration,
            true,
            "home_password",
            true,
            getString(R.string.title_password),
            "password_native",
            "password_selectsound_bottom",
            password_native,
            password_selectsound_bottom
        )
    )
    list.add(
        MainMenuModel(

            Motion_Detection,
            R.drawable.motion_detection_icon,
            getString(R.string.motion),
            getString(R.string.detection),
            dbHelper.chkBroadCast(Motion_Detection_Check),
            R.drawable.motion_icon_inter,
            Motion_Detection_Check,
            Motion_Detection_Flash,
            Motion_Detection_Vibration,
            val_ad_native_Motion_Detection_screen,
            "home_motion",
            true,
            getString(R.string.title_motion),
            "motion_native",
            "motion_selectsound_bottom",
            motion_native,
            motion_selectsound_bottom
        )
    )
    list.add(
        MainMenuModel(
            Motion_Detection,
            R.drawable.motion_detection_icon,
            getString(R.string.motion),
            getString(R.string.detection),
            dbHelper.chkBroadCast(Motion_Detection_Check),
            R.drawable.motion_icon_inter,
            Motion_Detection_Check,
            Motion_Detection_Flash,
            Motion_Detection_Vibration,
            val_ad_native_Motion_Detection_screen,
            "home_motion",
            true,
            getString(R.string.title_motion),
            "motion_native",
            "motion_selectsound_bottom",
            whistle_native,
            whistle_selectsound_bottom
        )
    )
    list.add(
        MainMenuModel(
            Whistle_Detection,
            R.drawable.wsitle_detection_icon,
            getString(R.string.wistle),
            getString(R.string.detection),
            dbHelper.chkBroadCast(Whistle_Detection_Check),
            R.drawable.whistleicon_inter,
            Whistle_Detection_Check,
            Whistle_Detection_Flash,
            Whistle_Detection_Vibration,
            val_ad_native_Whistle_Detection_screen,
            "home_whistle",
            true,
            getString(R.string.title_whistle),
            "whistle_native",
            "whistle_selectsound_bottom",
            whistle_native,
            whistle_selectsound_bottom
        )
    )
    list.add(
        MainMenuModel(
            HandFree_Detection,
            R.drawable.hand_free_icon,
            getString(R.string.handfree),
            getString(R.string.detection),
            dbHelper.chkBroadCast(HandFree_Detection_Check),
            R.drawable.handfree_icon_inter,
            HandFree_Detection_Check,
            HandFree_Detection_Flash,
            HandFree_Detection_Vibration,
            val_ad_native_hand_free_screen,
            "home_handfree",
            true,
            getString(R.string.title_handfree),
            "handfree_native",
            "handfree_selectsound_bottom",
            handfree_native,
            handfree_selectsound_bottom
        )
    )
    list.add(
        MainMenuModel(
            Clap_Detection,
            R.drawable.clap_detection__icon,
            getString(R.string.clap),
            getString(R.string.detection),
            dbHelper.chkBroadCast(Clap_Detection_Check),
            R.drawable.clap_icon_inter,
            Clap_Detection_Check,
            Clap_Detection_Flash,
            Clap_Detection_Vibration,
            val_ad_native_clap_detection_screen,
            "home_clap",
            true,
            getString(R.string.title_clap),
            "clap_native",
            "clap_selectsound_bottom",
            clap_native,
            clap_selectsound_bottom
        )
    )
    list.add(
        MainMenuModel(
            Remove_Charger,
            R.drawable.remove_charger,
            getString(R.string.remove),
            getString(R.string.detection),
            dbHelper.chkBroadCast(Remove_Charger_Check),
            R.drawable.remove_charger_icon_inter,
            Remove_Charger_Check,
            Remove_Charger_Flash,
            Remove_Charger_Vibration,
            val_ad_native_Remove_Charger_screen,
            "home_remove",
            true,
            getString(R.string.title_remove_charger),
            "remove_native",
            "remove_selectsound_bottom",
            remove_native,
            remove_selectsound_bottom
        )
    )
    list.add(
        MainMenuModel(
            Battery_Detection,
            R.drawable.battery_icon,
            getString(R.string.battery),
            getString(R.string.detection),
            dbHelper.chkBroadCast(Battery_Detection_Check),
            R.drawable.batter_icon_inter,
            Battery_Detection_Check,
            Battery_Detection_Flash,
            Battery_Detection_Vibration,
            val_ad_native_Battery_Detection_screen,
            "home_battery",
            true, getString(R.string.title_battery),
            "battery_native",
            "battery_selectsound_bottom",
            battery_native,
            battery_selectsound_bottom
        )
    )
//    list.add(
//        MainMenuModel(
//    Wifi_Alarm
//    R.drawable.wifi_icon,
//    getString(R.string.wifi_alarm),
//    getString(R.string.detection),
//     dbHelper.chkBroadCast(Wifi_Alarm),
//    R.drawable.wifi_icon_inter,
//    Wifi_Alarm

//        )
//
//    )
    return list
}


@SuppressLint("InflateParams")
fun Fragment.showExitDialog() {
    val bottomSheetDialog = BottomSheetDialog(requireContext())
    val view = layoutInflater.inflate(R.layout.exit_bottom_dialog, null)
    bottomSheetDialog.setContentView(view)

    bottomSheetDialog.window?.setBackgroundDrawableResource(R.color.transparent)
//  view.setBackgroundResource(R.drawable.rect_white_exit_bottom)
    view.findViewById<ConstraintLayout>(R.id.main_lay)
        .setBackgroundResource(R.drawable.rect_white_exit_bottom)


    val btnExit = view.findViewById<TextView>(R.id.yes)
    val btnCancel = view.findViewById<TextView>(R.id.no)

    btnExit.setOnClickListener {
        // Perform exit action
        // For securityalarm, calling finish() for the activity
        requireActivity().finish()
    }

    btnCancel.setOnClickListener {
        bottomSheetDialog.dismiss()
    }

    bottomSheetDialog.show()
}

fun Fragment.setupBackPressedCallback(
    onBackPressedAction: () -> Unit,
) {
    requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                onBackPressedAction.invoke()
            }
        }
    )
}


fun Context.shareApp() {
    val intentShare = Intent()
    intentShare.action = "android.intent.action.SEND"
    intentShare.putExtra(
        "android.intent.extra.TEXT", """
     Anti Theft app Download at: 
     https://play.google.com/store/apps/details?id=$packageName
     """.trimIndent()
    )
    intentShare.type = "text/plain"
    try {
        startActivity(Intent.createChooser(intentShare, "Share via"))
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun Context.moreApp() {

    try {
        startActivity(
            Intent(
                "android.intent.action.VIEW",
                Uri.parse("https://play.google.com/store/apps/developer?id=Master+of+Door+Lock+%26+Zipper+Lock+Screen")
            )
        )
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun Context.privacyPolicy() {
    try {
        startActivity(
            Intent(
                "android.intent.action.VIEW",
                Uri.parse("https://sites.google.com/view/masterdoorlockzipperlock/antitheftalarm")
            )
        )
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun Context.rateUs() {
    val i = Intent(Intent.ACTION_VIEW)
    i.data = Uri.parse("market://details?id=" + this.packageName)
    try {
        startActivity(i)
    } catch (e: ActivityNotFoundException) {
        e.printStackTrace()
    }
}

fun View.clickWithThrottle(throttleTime: Long = 400L, action: () -> Unit) {
    this.setOnClickListener(object : View.OnClickListener {
        private var lastClickTime: Long = 0

        override fun onClick(v: View) {
            if (SystemClock.elapsedRealtime() - lastClickTime < throttleTime) return
            else action()

            lastClickTime = SystemClock.elapsedRealtime()
        }
    })
}

val toast: Toast? = null

fun Fragment.showToast(msg: String) {
    toast?.cancel()
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
}

fun Context.setLocale(languageCode: String) {
    val locale = Locale(languageCode)
    Locale.setDefault(locale)
    val resources: Resources = resources
    val configuration: Configuration = resources.configuration
    configuration.setLocale(locale)
    resources.updateConfiguration(configuration, resources.displayMetrics)
}

fun Fragment.setLocaleMain(languageCode: String) {
    val locale = Locale(languageCode)
    Locale.setDefault(locale)
    val resources: Resources = resources
    val configuration: Configuration = resources.configuration
    configuration.setLocale(locale)
    resources.updateConfiguration(configuration, resources.displayMetrics)
}

var ratingDialog: AlertDialog? = null
fun Fragment.showRatingDialog(
    onPositiveButtonClick: (Float, AlertDialog) -> Unit,
) {
    val dialogView = layoutInflater.inflate(R.layout.rating_dialog, null)
    ratingDialog = AlertDialog.Builder(requireContext()).create()
    ratingDialog?.setView(dialogView)
    ratingDialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    val ratingBar = dialogView.findViewById<RatingBar>(R.id.ratingBar)
    val rateUs = dialogView.findViewById<TextView>(R.id.rate_us)

    rateUs.setOnClickListener {
        onPositiveButtonClick(ratingBar.rating, ratingDialog ?: return@setOnClickListener)
    }

    if (isVisible && isAdded && !isDetached) {
        ratingDialog?.show()
    }

}

fun Fragment.showInternetDialog(
    onPositiveButtonClick: () -> Unit,
    onNegitiveButtonClick: () -> Unit,
    onCloseButtonClick: () -> Unit,
) {
    val dialogView = layoutInflater.inflate(R.layout.internet_dialog, null)
    ratingDialog = AlertDialog.Builder(requireContext()).create()
    ratingDialog?.setCancelable(false)
    ratingDialog?.setCanceledOnTouchOutside(false)
    ratingDialog?.setView(dialogView)
    ratingDialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    val cancel = dialogView.findViewById<TextView>(R.id.cancl_btn)
    val yes = dialogView.findViewById<TextView>(R.id.cnfrm_del_btn)
    val close = dialogView.findViewById<ImageFilterView>(R.id.closeBtn)

    yes.setOnClickListener {
        ratingDialog?.dismiss()
        onPositiveButtonClick.invoke()
    }
    cancel.setOnClickListener {
        ratingDialog?.dismiss()
        onNegitiveButtonClick.invoke()
    }
    close.setOnClickListener {
        ratingDialog?.dismiss()
        onCloseButtonClick.invoke()
    }
    ratingDialog?.show()

}

fun showInternetDialogNew(
    context: Context,
    onPositiveButtonClick: () -> Unit,
    onNegitiveButtonClick: () -> Unit,
    onCloseButtonClick: () -> Unit,
) {
    val dialogView = LayoutInflater.from(context).inflate(R.layout.internet_dialog, null)
    ratingDialog = AlertDialog.Builder(context).create()
    ratingDialog?.setCancelable(false)
    ratingDialog?.setCanceledOnTouchOutside(false)
    ratingDialog?.setView(dialogView)
    ratingDialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    val cancel = dialogView.findViewById<TextView>(R.id.cancl_btn)
    val yes = dialogView.findViewById<TextView>(R.id.cnfrm_del_btn)
    val close = dialogView.findViewById<ImageFilterView>(R.id.closeBtn)

    yes.setOnClickListener {
        ratingDialog?.dismiss()
        onPositiveButtonClick.invoke()
    }
    cancel.setOnClickListener {
        ratingDialog?.dismiss()
        onNegitiveButtonClick.invoke()
    }
    close.setOnClickListener {
        ratingDialog?.dismiss()
        onCloseButtonClick.invoke()
    }
    ratingDialog?.show()

}

/*
fun Activity.showInternetDialog(
    onPositiveButtonClick: () -> Unit,
    onNegitiveButtonClick: () -> Unit,
    onCloseButtonClick: () -> Unit,
) {
    val dialogView = layoutInflater.inflate(R.layout.internet_dialog, null)
    ratingDialog = AlertDialog.Builder(this).create()
    ratingDialog?.setView(dialogView)
    ratingDialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    val cancel = dialogView.findViewById<TextView>(R.id.cancl_btn)
    val yes = dialogView.findViewById<TextView>(R.id.cnfrm_del_btn)
    val close = dialogView.findViewById<TextView>(R.id.closeBtn)

    yes.setOnClickListener {
        ratingDialog?.dismiss()
        onPositiveButtonClick.invoke()
    }
    cancel.setOnClickListener {
        ratingDialog?.dismiss()
        onNegitiveButtonClick.invoke()
    }
    close.setOnClickListener {
        ratingDialog?.dismiss()
        onCloseButtonClick.invoke()
    }
    ratingDialog?.show()

}
*/

//var inAppDialog: AlertDialog? = null
//var billingManager: BillingManager? = null

fun Fragment.showInAppDialog(
) {
    val dialog = Dialog(context ?: return)
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
    dialog.window?.setLayout(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT)
    dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.setContentView(R.layout.in_app_dialog_first)
//    val dialogView = layoutInflater.inflate(R.layout.in_app_dialog_first, null)
//    inAppDialog = AlertDialog.Builder(requireContext()).create()
//    inAppDialog?.setView(dialogView)
//    inAppDialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
//    inAppDialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
//    billingManager = BillingManager(context ?: return)
//    val subsProductIdList = listOf("subs_product_id_1", "subs_product_id_2", "subs_product_id_3")
//    val inAppProductIdList = when (BuildConfig.DEBUG) {
//        true -> listOf(billingManager?.getDebugProductIDList())
//        false -> listOf("inapp_product_id_1", "inapp_product_id_2")
//    }
//    billingManager?.initialize(
//        productInAppPurchases = inAppProductIdList as List<String>,
//        productSubscriptions = subsProductIdList,
//        billingListener = object : BillingListener {
//            override fun onConnectionResult(isSuccess: Boolean, message: String) {
//                Log.d(
//                    "in_app_TAG",
//                    "Billing: initBilling: onConnectionResult: isSuccess = $isSuccess - message = $message"
//                )
//                if (!isSuccess) {
////                        proceedApp()
//                }
//            }
//
//            override fun purchasesResult(purchaseDetailList: List<PurchaseDetail>) {
//                if (purchaseDetailList.isEmpty()) {
//                    // No purchase found, reset all sharedPreferences (premium properties)
//                }
//                purchaseDetailList.forEachIndexed { index, purchaseDetail ->
//                    Log.d(
//                        "in_app_TAG",
//                        "Billing: initBilling: purchasesResult: $index) $purchaseDetail "
//                    )
//                }
////                    proceedApp()
//            }
//        }
//    )
//    billingManager?.productDetailsLiveData?.observe(viewLifecycleOwner) { productDetailList ->
//        Log.d("in_app_TAG", "Billing: initObservers: $productDetailList")
//
//        productDetailList.forEach { productDetail ->
//            if (productDetail.productType == ProductType.inapp) {
//                if (productDetail.productId == "inapp_product_id_1") {
//                    // productDetail
//                } else if (productDetail.productId == "inapp_product_id_2") {
//                    // productDetail
//                }
//            } else {
//                if (productDetail.productId == "subs_product_id_1" && productDetail.planId == "subs_plan_id_1") {
//                    // productDetail (monthly)
//                } else if (productDetail.productId == "subs_product_id_2" && productDetail.planId == "subs_plan_id_2") {
//                    // productDetail (3 months)
//                } else if (productDetail.productId == "subs_product_id_3" && productDetail.planId == "subs_plan_id_3") {
//                    // productDetail (yearly)
//                }
//            }
//        }
//    }
    if (isVisible && isAdded && !isDetached) {
        dialog.show()
    }
}

private var ratingService: AlertDialog? = null
fun Fragment.showServiceDialog(
    onPositiveYesClick: () -> Unit,
    onPositiveNoClick: () -> Unit,
) {
    val dialogView = layoutInflater.inflate(R.layout.service_dialog, null)
    ratingService = AlertDialog.Builder(requireContext()).create()
    ratingDialog?.setCancelable(false)
    ratingDialog?.setCanceledOnTouchOutside(false)
    ratingService?.setView(dialogView)
    ratingService?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    val no = dialogView.findViewById<Button>(R.id.cancl_btn)
    val yes = dialogView.findViewById<Button>(R.id.cnfrm_del_btn)
    yes.setOnClickListener {
        if (isVisible && isAdded && !isDetached) {
            ratingService?.dismiss()
        }
        onPositiveYesClick()
    }

    no.setOnClickListener {
        if (isVisible && isAdded && !isDetached) {
            ratingService?.dismiss()
        }
        onPositiveNoClick()
    }

    if (isVisible && isAdded && !isDetached) {
        ratingService?.show()
    }

}

fun Fragment.requestCameraPermission(view: Switch) {
    if (ActivityCompat.shouldShowRequestPermissionRationale(
            requireActivity(),
            CAMERA_PERMISSION
        )
    ) {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(CAMERA_PERMISSION),
            2
        )
      /*  android.app.AlertDialog.Builder(context).setTitle(getString(R.string.permission_needed))
            .setMessage(getString(R.string.camera_permission)).setPositiveButton(
                getString(R.string.ok)
            ) { _, _ ->
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(CAMERA_PERMISSION),
                    2
                )
            }.setNegativeButton(
                getString(R.string.cancel)
            ) { dialogInterface, _ ->
                view.isChecked = false
                dialogInterface.dismiss()
            }.create().show()*/
    } else {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(CAMERA_PERMISSION),
            2
        )
    }
}

fun Fragment.requestCameraPermissionAudio() {
    if (ActivityCompat.shouldShowRequestPermissionRationale(
            requireActivity(),
            AUDIO_PERMISSION
        ) && ActivityCompat.checkSelfPermission(
            requireActivity(),
            PHONE_PERMISSION
        ) != 0
    ) {
        android.app.AlertDialog.Builder(context).setTitle(getString(R.string.permission_needed))
        android.app.AlertDialog.Builder(context).setCancelable(false)
            .setMessage(getString(R.string.camera_permission)).setPositiveButton(
                getString(R.string.ok)
            ) { _, _ ->
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(AUDIO_PERMISSION, PHONE_PERMISSION),
                    2
                )
            }.setNegativeButton(
                getString(R.string.cancel)
            ) { dialogInterface, _ ->
                dialogInterface.dismiss()
            }.create().show()
    } else {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(AUDIO_PERMISSION, PHONE_PERMISSION),
            2
        )
    }
}

fun Fragment.requestCameraPermissionNotification(hideAd: View?) {
    if (ActivityCompat.shouldShowRequestPermissionRationale(
            requireActivity(),
            NOTIFICATION_PERMISSION
        )
    ) {
        showNotificationPermissionDialog(hideAd)
    } else {
        showNotificationPermissionDialog(hideAd)
    }
}

fun Fragment.showNotificationPermissionDialog(hideAd: View?) {
    val dialogView = layoutInflater.inflate(R.layout.notification_dialog, null)
    ratingService = AlertDialog.Builder(requireContext()).create()
    ratingDialog?.setCancelable(false)
    ratingDialog?.setCanceledOnTouchOutside(false)
    ratingService?.setView(dialogView)
    ratingService?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    val no = dialogView.findViewById<Button>(R.id.cancl_btn)
    val yes = dialogView.findViewById<Button>(R.id.cnfrm_del_btn)
    yes.setOnClickListener {
        hideAd?.visibility = View.GONE
        if (isVisible && isAdded && !isDetached) {
            ratingService?.dismiss()
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(NOTIFICATION_PERMISSION),
                2
            )
        }
    }

    no.setOnClickListener {
        hideAd?.visibility = View.GONE
        if (isVisible && isAdded && !isDetached) {
            ratingService?.dismiss()
        }
    }

    if (isVisible && isAdded && !isDetached) {
        ratingService?.show()
    }

}

fun Activity.setStatusBar() {
    val nightModeFlags: Int = this.resources.configuration.uiMode and
            Configuration.UI_MODE_NIGHT_MASK
    when (nightModeFlags) {
        Configuration.UI_MODE_NIGHT_YES -> {
            window.decorView.windowInsetsController?.setSystemBarsAppearance(
                0,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        }

        Configuration.UI_MODE_NIGHT_NO -> window.decorView.windowInsetsController?.setSystemBarsAppearance(
            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
        )

        Configuration.UI_MODE_NIGHT_UNDEFINED ->
            window.decorView.windowInsetsController?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
    }
}

fun Activity.setDarkMode(isDarkMode: Boolean) {
    if (isDarkMode)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    else
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
}

fun ImageView.loadImage(context: Context?, resourceId: Int) {
    Glide.with(context ?: return)
        .load(resourceId)
        .into(this)
}

fun ImageView.setImage(resourceId: Int) {
    setBackgroundResource(resourceId)
}

fun MainMenuFragment.autoServiceFunction(isStart: Boolean) {
    sharedPrefUtils?.setBroadCast(IS_NOTIFICATION, isStart)
    if (isStart) {
        ContextCompat.startForegroundService(
            context ?: return,
            Intent(
                context ?: return,
                SystemEventsService::class.java
            )
        )
    } else {
        context?.stopService(
            Intent(
                context ?: return,
                SystemEventsService::class.java
            )
        )
    }

}

fun FragmentDetectionSameFunction.autoServiceFunctionInternalModule(
    isStart: Boolean,
    active: String?,
) {
    sharedPrefUtils?.setBroadCast(active, isStart)
    if (isStart) {
        sharedPrefUtils?.setBroadCast(IS_NOTIFICATION, true)
        ContextCompat.startForegroundService(
            context ?: return,
            Intent(
                context ?: return,
                SystemEventsService::class.java
            )
        )
    }

}

fun Fragment.autoServiceFunctionIntruder(
    isStart: Boolean,
    sharedPrefUtils: DbHelper?,
) {
    if (isStart) {
        sharedPrefUtils?.setBroadCast(IS_NOTIFICATION, true)
        ContextCompat.startForegroundService(
            context ?: return,
            Intent(
                context ?: return,
                SystemEventsService::class.java
            )
        )
    }

}

fun Fragment.startLottieAnimation(
    animationView: LottieAnimationView,
    animationViewText: TextView,
    isCheck: Boolean,
) {
    if (isCheck) {
        animationView.setAnimation("ic_activate.json")
        animationViewText.text = getString(R.string.active)
    } else {
        animationView.setAnimation("ic_deactive.json")
        animationViewText.text = getString(R.string.de_active)
    }
    animationView.loop(true)
    animationView.playAnimation()
}

fun firebaseAnalytics(Item_id: String, Item_name: String) {
    try {
        val firebaseAnalytics = Firebase.analytics

        val bundle = Bundle().apply {
            //        putString(FirebaseAnalytics.Param.ITEM_ID, Item_id)
            putString(FirebaseAnalytics.Param.ITEM_NAME, Item_name)
        }
        firebaseAnalytics.logEvent(Item_id, bundle)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun Fragment.checkNotificationPermission(layout1: View) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
        return
    }
    if (ContextCompat.checkSelfPermission(requireContext(), NOTIFICATION_PERMISSION) != 0) {
        layout1.visibility = View.VISIBLE
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(NOTIFICATION_PERMISSION),
            2
        )
//        requestCameraPermissionNotification(layout1)
    }

}

fun isInternetAvailable(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                isInternetAvailable = true
                true
            }

            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                isInternetAvailable = true
                true
            }

            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                isInternetAvailable = true
                true
            }

            else -> {
                isInternetAvailable = false
                false
            }
        }
    } else {
        val networkInfo = connectivityManager.activeNetworkInfo ?: return false
        isInternetAvailable = networkInfo.isConnected
        return networkInfo.isConnected
    }
}


fun isNetworkAvailable(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    } else {
        val networkInfo = connectivityManager.activeNetworkInfo ?: return false
        return networkInfo.isConnected
    }
}


// Usage example

fun openWifiSettings(context: Context) {
    val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
    context.startActivity(intent)
}

fun openMobileDataSettings(context: Context) {
    val intent = Intent(Settings.ACTION_DATA_ROAMING_SETTINGS)
    context.startActivity(intent)
}

fun getNativeLayout(position: Int, layout: FrameLayout, context: Context): Int {
    Log.d("check_layout", "getNativeLayout: $position")
    when (position) {
        1 -> {
            layout.minimumHeight = convertDpToPixel(80F, context).toInt()
            return R.layout.layout_native_80
        }

        2 -> {
            layout.minimumHeight = convertDpToPixel(140F, context).toInt()
            return R.layout.layout_native_140
        }

        3 -> {
            layout.minimumHeight = convertDpToPixel(176F, context).toInt()
            return R.layout.layout_native_176
        }

        4 -> {
            layout.minimumHeight = convertDpToPixel(190F, context).toInt()
            return R.layout.native_layout_190
        }

        5 -> {
            layout.minimumHeight = convertDpToPixel(276F, context).toInt()
            return R.layout.native_layout_276
        }

        6 -> {
            layout.minimumHeight = convertDpToPixel(260F, context).toInt()
            return R.layout.layout_native_260
        }
    }
    layout.minimumHeight = convertDpToPixel(80F, context).toInt()
    return R.layout.layout_native_80
}

fun getNativeLayoutShimmer(position: Int): Int {
    Log.d("check_layout", "getNativeLayout: $position")
    when (position) {
        1 -> {
            return R.layout.shimmer_loadong_native_80
        }

        2 -> {
            return R.layout.shimmer_loading_native_140
        }

        3 -> {
            return R.layout.shimmer_loading_native_176
        }

        4 -> {
            return R.layout.shimmer_loading_native_190
        }

        5 -> {
            return R.layout.shimmer_loading_native_276
        }

        6 -> {
            return R.layout.shimmer_loading_native_260
        }
    }
    return R.layout.shimmer_loadong_native_80
}

private fun convertDpToPixel(valueDp: Float, context: Context): Float {
    val displayMetrics = context.resources.displayMetrics
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, valueDp,
        displayMetrics
    )
}

fun getBannerId(position: String): String {
    when (position) {
        Pocket_Detection -> {
            return "pocket_banner"
        }

        Motion_Detection -> {
            return "motion_banner"
        }

        Whistle_Detection -> {
            return "whistle_banner"
        }

        HandFree_Detection -> {
            return "handfree_banner"
        }

        Clap_Detection -> {
            return "clap_banner"
        }

        Remove_Charger -> {
            return "remove_banner"
        }

        Battery_Detection -> {
            return "battery_banner"
        }
    }
    return "pocket_banner"
}

fun Fragment.restartApp() {
    val intent = context?.packageManager?.getLaunchIntentForPackage(context?.packageName ?: return)
    intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
    context?.startActivity(intent)
}

private var isAppInBackground = true

fun Context.isAppInBackground(): Boolean {
    return isAppInBackground
}

fun Application.registerAppLifecycleCallbacks() {
    registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}

        override fun onActivityStarted(activity: Activity) {}

        override fun onActivityResumed(activity: Activity) {
            isAppInBackground = false
        }

        override fun onActivityPaused(activity: Activity) {
            isAppInBackground = true
        }

        override fun onActivityStopped(activity: Activity) {}

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

        override fun onActivityDestroyed(activity: Activity) {}
    })
}