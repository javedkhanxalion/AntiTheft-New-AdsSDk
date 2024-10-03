package com.do_not_douch.antitheifproject.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.antitheft.alarm.donottouch.findmyphone.protector.smartapp.privacydefender.myphone.R
import com.antitheft.alarm.donottouch.findmyphone.protector.smartapp.privacydefender.myphone.databinding.FragmentSplashBinding
import com.do_not_douch.antitheifproject.ads_manager.AdOpenApp
import com.do_not_douch.antitheifproject.ads_manager.AdsManager
import com.do_not_douch.antitheifproject.ads_manager.AdsManager.isNetworkAvailable
import com.do_not_douch.antitheifproject.ads_manager.AdsManager.showOpenAd
import com.do_not_douch.antitheifproject.ads_manager.CmpClass
import com.do_not_douch.antitheifproject.ads_manager.PurchasePrefs
import com.do_not_douch.antitheifproject.ads_manager.interfaces.NativeListener
import com.do_not_douch.antitheifproject.ads_manager.loadTwoInterAds
import com.do_not_douch.antitheifproject.ads_manager.loadTwoInterAdsSplash
import com.do_not_douch.antitheifproject.ads_manager.showNormalInterAdSingle
import com.do_not_douch.antitheifproject.helper_class.DbHelper
import com.do_not_douch.antitheifproject.model.NativeDesignType
import com.do_not_douch.antitheifproject.utilities.BaseFragment
import com.do_not_douch.antitheifproject.utilities.IS_FIRST
import com.do_not_douch.antitheifproject.utilities.IS_INTRO
import com.do_not_douch.antitheifproject.utilities.LANG_CODE
import com.do_not_douch.antitheifproject.utilities.LANG_SCREEN
import com.do_not_douch.antitheifproject.utilities.Onboarding_Full_Native
import com.do_not_douch.antitheifproject.utilities.appUpdateType
import com.do_not_douch.antitheifproject.utilities.banner_height
import com.do_not_douch.antitheifproject.utilities.banner_type
import com.do_not_douch.antitheifproject.utilities.battery_native
import com.do_not_douch.antitheifproject.utilities.battery_selectsound_bottom
import com.do_not_douch.antitheifproject.utilities.clap_native
import com.do_not_douch.antitheifproject.utilities.clap_selectsound_bottom
import com.do_not_douch.antitheifproject.utilities.counter
import com.do_not_douch.antitheifproject.utilities.exitdialog_bottom
import com.do_not_douch.antitheifproject.utilities.firebaseAnalytics
import com.do_not_douch.antitheifproject.utilities.getNativeLayout
import com.do_not_douch.antitheifproject.utilities.handfree_native
import com.do_not_douch.antitheifproject.utilities.handfree_selectsound_bottom
import com.do_not_douch.antitheifproject.utilities.home_native
import com.do_not_douch.antitheifproject.utilities.id_adaptive_banner
import com.do_not_douch.antitheifproject.utilities.id_app_open_screen
import com.do_not_douch.antitheifproject.utilities.id_collapsable_banner
import com.do_not_douch.antitheifproject.utilities.id_frequency_counter
import com.do_not_douch.antitheifproject.utilities.id_inter_counter
import com.do_not_douch.antitheifproject.utilities.id_inter_main_medium
import com.do_not_douch.antitheifproject.utilities.id_inter_splash_Screen
import com.do_not_douch.antitheifproject.utilities.id_native_screen
import com.do_not_douch.antitheifproject.utilities.id_splash_native
import com.do_not_douch.antitheifproject.utilities.inter_frequency_count
import com.do_not_douch.antitheifproject.utilities.intruder_native
import com.do_not_douch.antitheifproject.utilities.intruderimage_bottom
import com.do_not_douch.antitheifproject.utilities.isInternetAvailable
import com.do_not_douch.antitheifproject.utilities.isSplash
import com.do_not_douch.antitheifproject.utilities.isSplashDialog
import com.do_not_douch.antitheifproject.utilities.language_bottom
import com.do_not_douch.antitheifproject.utilities.languageinapp_scroll
import com.do_not_douch.antitheifproject.utilities.motion_native
import com.do_not_douch.antitheifproject.utilities.motion_selectsound_bottom
import com.do_not_douch.antitheifproject.utilities.onboarding1_bottom
import com.do_not_douch.antitheifproject.utilities.onboarding2_bottom
import com.do_not_douch.antitheifproject.utilities.onboarding3_bottom
import com.do_not_douch.antitheifproject.utilities.openMobileDataSettings
import com.do_not_douch.antitheifproject.utilities.openWifiSettings
import com.do_not_douch.antitheifproject.utilities.password_native
import com.do_not_douch.antitheifproject.utilities.password_selectsound_bottom
import com.do_not_douch.antitheifproject.utilities.pocket_native
import com.do_not_douch.antitheifproject.utilities.pocket_selectsound_bottom
import com.do_not_douch.antitheifproject.utilities.remove_native
import com.do_not_douch.antitheifproject.utilities.remove_selectsound_bottom
import com.do_not_douch.antitheifproject.utilities.sessionOnboarding
import com.do_not_douch.antitheifproject.utilities.sessionOpenlanguage
import com.do_not_douch.antitheifproject.utilities.setLocaleMain
import com.do_not_douch.antitheifproject.utilities.setupBackPressedCallback
import com.do_not_douch.antitheifproject.utilities.showInternetDialog
import com.do_not_douch.antitheifproject.utilities.splash_bottom
import com.do_not_douch.antitheifproject.utilities.test_ui_native
import com.do_not_douch.antitheifproject.utilities.thankyou_bottom
import com.do_not_douch.antitheifproject.utilities.val_ad_native_Battery_Detection_screen
import com.do_not_douch.antitheifproject.utilities.val_ad_native_Motion_Detection_screen
import com.do_not_douch.antitheifproject.utilities.val_ad_native_Pocket_Detection_screen
import com.do_not_douch.antitheifproject.utilities.val_ad_native_Remove_Charger_screen
import com.do_not_douch.antitheifproject.utilities.val_ad_native_Whistle_Detection_screen
import com.do_not_douch.antitheifproject.utilities.val_ad_native_clap_detection_screen
import com.do_not_douch.antitheifproject.utilities.val_ad_native_hand_free_screen
import com.do_not_douch.antitheifproject.utilities.val_ad_native_intro_screen
import com.do_not_douch.antitheifproject.utilities.val_ad_native_intruder_detection_screen
import com.do_not_douch.antitheifproject.utilities.val_ad_native_intruder_list_screen
import com.do_not_douch.antitheifproject.utilities.val_ad_native_language_screen
import com.do_not_douch.antitheifproject.utilities.val_ad_native_loading_screen
import com.do_not_douch.antitheifproject.utilities.val_ad_native_main_menu_screen
import com.do_not_douch.antitheifproject.utilities.val_ad_native_password_screen
import com.do_not_douch.antitheifproject.utilities.val_ad_native_show_image_screen
import com.do_not_douch.antitheifproject.utilities.val_ad_native_sound_screen
import com.do_not_douch.antitheifproject.utilities.val_app_open_main
import com.do_not_douch.antitheifproject.utilities.val_banner_1
import com.do_not_douch.antitheifproject.utilities.val_banner_language_screen
import com.do_not_douch.antitheifproject.utilities.val_banner_main_menu_screen
import com.do_not_douch.antitheifproject.utilities.val_banner_setting_screen
import com.do_not_douch.antitheifproject.utilities.val_banner_splash_screen
import com.do_not_douch.antitheifproject.utilities.val_exit_dialog_native
import com.do_not_douch.antitheifproject.utilities.val_exit_screen_native
import com.do_not_douch.antitheifproject.utilities.val_inapp_frequency
import com.do_not_douch.antitheifproject.utilities.val_inter_exit_screen
import com.do_not_douch.antitheifproject.utilities.val_inter_language_screen
import com.do_not_douch.antitheifproject.utilities.val_inter_main_normal
import com.do_not_douch.antitheifproject.utilities.val_inter_on_bord_screen
import com.do_not_douch.antitheifproject.utilities.val_is_inapp
import com.do_not_douch.antitheifproject.utilities.val_is_inapp_splash
import com.do_not_douch.antitheifproject.utilities.val_native_Full_screen
import com.do_not_douch.antitheifproject.utilities.val_native_intro_screen
import com.do_not_douch.antitheifproject.utilities.val_native_intro_screen1
import com.do_not_douch.antitheifproject.utilities.val_native_intro_screen2
import com.do_not_douch.antitheifproject.utilities.whistle_native
import com.do_not_douch.antitheifproject.utilities.whistle_selectsound_bottom
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.google.firebase.FirebaseApp
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.firebase.remoteconfig.ktx.get
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashFragment :
    BaseFragment<FragmentSplashBinding>(FragmentSplashBinding::inflate) {


    private var isInternetDialog: Boolean = false
    private var dbHelper: DbHelper? = null
    private var remoteConfig: FirebaseRemoteConfig? = null
    private var adsManager: AdsManager? = null

    companion object {
        var isUserConsent = false
        var consentListener: ((consent: Boolean) -> Unit?)? = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        CoroutineScope(Dispatchers.Main).launch {
            dbHelper = DbHelper(context ?: return@launch)
            isSplash = false
            val cmpClass = CmpClass(activity ?: return@launch)
            cmpClass.initilaizeCMP()

            dbHelper?.getStringData(requireContext(), LANG_CODE, "en")?.let {
                setLocaleMain(it)
            }
            if (!isInternetAvailable(context ?: return@launch)) {
                showInternetDialog(
                    onPositiveButtonClick = {
                        isInternetDialog = true
                        isSplashDialog = false
                        openMobileDataSettings(context ?: requireContext())
                    },
                    onNegitiveButtonClick = {
                        isInternetDialog = true
                        isSplashDialog = false
                        openWifiSettings(context ?: requireContext())
                    },
                    onCloseButtonClick = {
                        isSplashDialog = false
                        getIntentMove()
                    }
                )
                return@launch
            } else {
                isSplashDialog = false
            }
            isSplash = false
            counter = 0
            inter_frequency_count = 0
            dbHelper = DbHelper(activity?.applicationContext ?: return@launch)

            if (isNetworkAvailable(context)) {
                adsManager = AdsManager.appAdsInit(requireActivity())
                initRemoteIds()
            } else {
                getIntentMove()
            }

        }
        setupBackPressedCallback {
            //Do Nothing
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.clear()
    }

    private fun observeSplashLiveData() {
        try {
            lifecycleScope.launchWhenStarted {
                if (val_app_open_main) {
                    isSplash = true
                    adsManager?.let {
                        loadTwoInterAds(
                            ads = it,
                            activity = activity ?: return@launchWhenStarted,
                            remoteConfigNormal = true,
                            adIdNormal = id_inter_main_medium,
                            tagClass = "main_app_fragment_pre_load"
                        )
                    }
                    delay(7000)
                    showOpenAd(activity ?: return@launchWhenStarted) {
                    }
                    getIntentMove()
                } else {
                    delay(7000)
                    isSplash = true
                    adsManager?.let {
                        showNormalInterAdSingle(
                            it,
                            activity ?: return@let,
                            remoteConfigNormal = true,
                            adIdNormal = id_inter_main_medium,
                            layout = _binding?.adsLayDialog!!,
                            tagClass = "splash"
                        ) {
                            getIntentMove()
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getIntentMove() {
        /*    when (sessionOpenlanguage) {
                0 -> {
                    when (sessionOnboarding) {
                        0 -> {
                            firebaseAnalytics(
                                "loading_fragment_load_next_btn_main",
                                "loading_fragment_load_next_btn_main -->  Click"
                            )
                            return if (PurchasePrefs(context).getBoolean("inApp") || !val_is_inapp_splash) {
                                findNavController().navigate(
                                    R.id.myMainMenuFragment,
                                    bundleOf("is_splash" to true)
                                )
                            } else {
                                findNavController().navigate(
                                    R.id.FragmentBuyScreen,
                                    bundleOf("isSplash" to true)
                                )
                            }
                        }

                        1 -> {
                            firebaseAnalytics(
                                "loading_fragment_load_next_btn_intro",
                                "loading_fragment_load_next_btn_intro -->  Click"
                            )
                            return findNavController().navigate(R.id.OnBordScreenNewScreen)
                        }

                        2 -> {
                            firebaseAnalytics(
                                "loading_fragment_load_next_btn_intro",
                                "loading_fragment_load_next_btn_intro -->  Click"
                            )
                            return findNavController().navigate(R.id.OnBordScreenNewScreen)
                        }
                    }
                }

                1 -> {
                    firebaseAnalytics(
                        "loading_fragment_load_next_btn_language",
                        "loading_fragment_load_next_btn_language -->  Click"
                    )
                    return findNavController().navigate(
                        R.id.LanguageFragment,
                        bundleOf(LANG_SCREEN to true)
                    )
                }

                2 -> {
                    firebaseAnalytics(
                        "loading_fragment_load_next_btn_language",
                        "loading_fragment_load_next_btn_language -->  Click"
                    )
                    return findNavController().navigate(
                        R.id.LanguageFragment,
                        bundleOf(LANG_SCREEN to true)
                    )
                }
            }*/
        if (dbHelper?.getBooleanData(
                context ?: return, IS_FIRST, false
            ) == false && sessionOpenlanguage == 0
        ) {
            firebaseAnalytics(
                "loading_fragment_load_next_btn_language",
                "loading_fragment_load_next_btn_language -->  Click"
            )
            findNavController().navigate(
                R.id.LanguageFragment, bundleOf(LANG_SCREEN to true)
            )
        } else
            if (dbHelper?.getBooleanData(
                    context ?: return, IS_INTRO, false
                ) == false && sessionOnboarding == 0
            ) {
                firebaseAnalytics(
                    "loading_fragment_load_next_btn_intro",
                    "loading_fragment_load_next_btn_intro -->  Click"
                )
                findNavController().navigate(R.id.OnBordScreenNewScreen)
            } else {
                firebaseAnalytics(
                    "loading_fragment_load_next_btn_main",
                    "loading_fragment_load_next_btn_main -->  Click"
                )
                if (PurchasePrefs(context).getBoolean("inApp") || !val_is_inapp_splash) {
                    findNavController().navigate(
                        R.id.myMainMenuFragment,
                        bundleOf("is_splash" to true)
                    )
                } else {
                    findNavController().navigate(
                        R.id.FragmentBuyScreen,
                        bundleOf("isSplash" to true)
                    )
                }

            }
    }

    private fun loadBanner() {
        _binding?.adsView?.visibility = View.VISIBLE
        val adView = LayoutInflater.from(context).inflate(
            getNativeLayout(splash_bottom, _binding?.adsView!!, context ?: return),
            null, false
        ) as NativeAdView
        adsManager?.nativeAds()?.loadNativeAd(
            activity ?: return,
            val_banner_splash_screen,
            "",
            object : NativeListener {
                override fun nativeAdLoaded(currentNativeAd: NativeAd?) {
                    if (!isAdded && !isVisible && isDetached) {
                        return
                    }
                    _binding?.adsView?.visibility = View.VISIBLE
                    _binding?.shimmerLayout?.visibility = View.GONE
                    if (isAdded && isVisible && !isDetached) {
                        adsManager?.nativeAds()
                            ?.nativeViewMediaSplashSplash(
                                context ?: return,
                                currentNativeAd ?: return,
                                adView
                            )
                        _binding?.adsView?.removeAllViews()
                        _binding?.adsView?.addView(adView)
                    }

                    super.nativeAdLoaded(currentNativeAd)
                }

                override fun nativeAdFailed(loadAdError: LoadAdError) {
                    _binding?.adsView?.visibility = View.GONE
                    _binding?.shimmerLayout?.visibility = View.GONE
                    super.nativeAdFailed(loadAdError)
                }

                override fun nativeAdValidate(string: String) {
                    _binding?.adsView?.visibility = View.GONE
                    _binding?.shimmerLayout?.visibility = View.GONE
                    super.nativeAdValidate(string)
                }


            })
    }

    private fun initRemoteIds() {
        val remoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(3600) // Set the minimum interval for fetching, in seconds
            .build()
        remoteConfig.setConfigSettingsAsync(configSettings)
// Fetch the remote config values
        remoteConfig.fetchAndActivate()
            .addOnCompleteListener(activity ?: return) { task ->
                if (task.isSuccessful) {
                    // Apply the fetched values to your app
                    applyAdIdsFromRemoteConfig(remoteConfig)
                } else {
                    // Handle the error
                    // For example, use default values or log an error message
                }
            }
    }

    private fun applyAdIdsFromRemoteConfig(remoteConfig: FirebaseRemoteConfig) {

        banner_type = remoteConfig.getLong("banner_type").toInt()
        appUpdateType = remoteConfig.getLong("appUpdateType").toInt()
        id_inter_counter = remoteConfig.getLong("id_inter_counter").toInt()
        id_frequency_counter = remoteConfig.getLong("id_frequency_counter").toInt()

        val_inapp_frequency = remoteConfig.getLong("val_inapp_frequency").toInt()
        banner_height = remoteConfig.getLong("banner_height").toInt()

        id_inter_main_medium = remoteConfig.getString("id_inter_main_medium")
        id_native_screen = remoteConfig.getString("id_native_screen")
        id_adaptive_banner = remoteConfig.getString("id_adaptive_banner")
        id_app_open_screen = remoteConfig.getString("id_app_open_screen")
        id_inter_splash_Screen = remoteConfig.getString("id_inter_splash_Screen")
        id_collapsable_banner = remoteConfig.getString("id_collapsable_banner")
        id_splash_native = remoteConfig.getString("id_splash_native")

        Onboarding_Full_Native = remoteConfig.getString("Onboarding_Full_Native").toInt()
        sessionOpenlanguage = remoteConfig.getString("sessionOpenlanguage").toInt()
        sessionOnboarding = remoteConfig.getString("sessionOnboarding").toInt()
        test_ui_native = remoteConfig.getString("test_ui_native")
        parseJsonWithGson(test_ui_native)

        Log.d("check_language", "onSuccess: $sessionOpenlanguage")
        Log.d("check_language", "onSuccess: $sessionOnboarding")
        Log.d("check_language", "onSuccess: $test_ui_native")
        Log.d("check_language", "onSuccess: $Onboarding_Full_Native")
        Log.d("remote_ids", "$id_inter_counter")
        Log.d("remote_ids", "$id_frequency_counter")
        initRemoteConfig()
    }

    private fun initRemoteConfig() {
        try {
            FirebaseApp.initializeApp(context ?: return)
            remoteConfig = Firebase.remoteConfig
            val configSettings = remoteConfigSettings {
                minimumFetchIntervalInSeconds = 6
            }
            remoteConfig?.setConfigSettingsAsync(configSettings)
            remoteConfig?.setDefaultsAsync(R.xml.remote_config_defaults)
            remoteConfig?.fetchAndActivate()?.addOnCompleteListener(activity ?: return) { task ->
                if (task.isSuccessful) {
                    val updated = task.result
                    Log.d("RemoteConfig", "Config params updated: $updated")
                    val_banner_1 = remoteConfig!!["val_banner_1"].asBoolean()
                    val_inter_main_normal = remoteConfig!!["val_inter_main_normal"].asBoolean()
                    val_ad_native_main_menu_screen =
                        remoteConfig!!["val_ad_native_main_menu_screen"].asBoolean()
                    val_ad_native_loading_screen =
                        remoteConfig!!["val_native_loading_screen"].asBoolean()
                    val_ad_native_intro_screen =
                        remoteConfig!!["val_ad_native_intro_screen"].asBoolean()
                    val_ad_native_language_screen =
                        remoteConfig!!["val_ad_native_language_screen"].asBoolean()
                    val_ad_native_sound_screen =
                        remoteConfig!!["val_ad_native_sound_screen"].asBoolean()
                    val_ad_native_intruder_list_screen =
                        remoteConfig!!["val_ad_native_intruder_list_screen"].asBoolean()
                    val_ad_native_show_image_screen =
                        remoteConfig!!["val_ad_native_show_image_screen"].asBoolean()
                    val_ad_native_intruder_detection_screen =
                        remoteConfig!!["val_ad_native_intruder_detection_screen"].asBoolean()
                    val_ad_native_password_screen =
                        remoteConfig!!["val_ad_native_password_screen"].asBoolean()
                    val_ad_native_Motion_Detection_screen =
                        remoteConfig!!["val_ad_native_Motion_Detection_screen"].asBoolean()
                    val_ad_native_Whistle_Detection_screen =
                        remoteConfig!!["val_ad_native_Whistle_Detection_screen"].asBoolean()
                    val_ad_native_hand_free_screen =
                        remoteConfig!!["val_ad_native_hand_free_screen"].asBoolean()
                    val_ad_native_clap_detection_screen =
                        remoteConfig!!["val_ad_native_clap_detection_screen"].asBoolean()
                    val_ad_native_Remove_Charger_screen =
                        remoteConfig!!["val_ad_native_Remove_Charger_screen"].asBoolean()
                    val_ad_native_Battery_Detection_screen =
                        remoteConfig!!["val_ad_native_Battery_Detection_screen"].asBoolean()
                    val_ad_native_Pocket_Detection_screen =
                        remoteConfig!!["val_ad_native_Pocket_Detection_screen"].asBoolean()

                    val_banner_setting_screen =
                        remoteConfig!!["val_banner_setting_screen"].asBoolean()
                    val_inter_exit_screen = remoteConfig!!["val_inter_exit_screen"].asBoolean()
                    val_exit_dialog_native = remoteConfig!!["val_exit_dialog_native"].asBoolean()
                    val_exit_screen_native = remoteConfig!!["val_exit_screen_native"].asBoolean()

                    val_banner_splash_screen =
                        remoteConfig!!["val_banner_splash_screen"].asBoolean()
                    val_banner_language_screen =
                        remoteConfig!!["val_banner_language_screen"].asBoolean()
                    val_banner_main_menu_screen =
                        remoteConfig!!["val_banner_main_menu_screen"].asBoolean()

                    val_native_Full_screen = remoteConfig!!["val_native_Full_screen"].asBoolean()
                    val_native_intro_screen = remoteConfig!!["val_native_intro_screen"].asBoolean()
                    val_native_intro_screen1 =
                        remoteConfig!!["val_native_intro_screen1"].asBoolean()
                    val_native_intro_screen2 =
                        remoteConfig!!["val_native_intro_screen2"].asBoolean()
                    val_app_open_main = remoteConfig!!["val_app_open_main"].asBoolean()
                    val_inter_language_screen =
                        remoteConfig!!["val_inter_language_screen"].asBoolean()
                    val_inter_on_bord_screen =
                        remoteConfig!!["val_inter_on_bord_screen"].asBoolean()
                    val_is_inapp_splash = remoteConfig!!["val_is_inapp_splash"].asBoolean()
                    val_is_inapp = remoteConfig!!["val_is_inapp"].asBoolean()


                } else {
                    Log.d("RemoteConfig", "Fetch failed")
                }
                adsManager?.nativeAdsMain()?.loadNativeAd(
                    activity ?: return@addOnCompleteListener,
                    val_ad_native_loading_screen,
                    id_splash_native,
                    object : NativeListener {
                    }
                )
                AdOpenApp(activity?.application ?: return@addOnCompleteListener, id_app_open_screen)
                if (!val_app_open_main) {
                    loadTwoInterAdsSplash(
                        adsManager ?: return@addOnCompleteListener,
                        activity ?: return@addOnCompleteListener,
                        remoteConfigNormal = true,
                        adIdNormal = id_inter_splash_Screen,
                        "splash"
                    )
                }
                loadBanner()
                observeSplashLiveData()
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun parseJsonWithGson(jsonString: String) {
        if (jsonString.isNotEmpty()) {
            val gson = Gson()
            val type = object : TypeToken<Map<String, List<NativeDesignType>>>() {}.type
            val dataMap: Map<String, List<NativeDesignType>> = gson.fromJson(jsonString, type)
            val onboarding1_bottom1 = dataMap["onboarding1_bottom"]?.map { it.native_design_type }
            onboarding1_bottom1?.forEach {
                onboarding1_bottom = it.toInt()
            }
            val onboarding2_bottom1 = dataMap["onboarding2_bottom"]?.map { it.native_design_type }
            onboarding2_bottom1?.forEach {
                onboarding2_bottom = it.toInt()
            }
            val onboarding3_bottom1 = dataMap["onboarding3_bottom"]?.map { it.native_design_type }
            onboarding3_bottom1?.forEach {
                onboarding3_bottom = it.toInt()
            }
            val exitdialog_bottom1 = dataMap["exitdialog_bottom"]?.map { it.native_design_type }
            exitdialog_bottom1?.forEach {
                exitdialog_bottom = it.toInt()
            }
            val thankyou_bottom1 = dataMap["thankyou_bottom"]?.map { it.native_design_type }
            thankyou_bottom1?.forEach {
                thankyou_bottom = it.toInt()
            }
            val home_native1 = dataMap["home_native"]?.map { it.native_design_type }
            home_native1?.forEach {
                home_native = it.toInt()
            }
            val intruder_native1 = dataMap["intruder_native"]?.map { it.native_design_type }
            intruder_native1?.forEach {
                intruder_native = it.toInt()
            }
            val intruderimage_bottom1 =
                dataMap["intruderimage_bottom"]?.map { it.native_design_type }
            intruderimage_bottom1?.forEach {
                intruderimage_bottom = it.toInt()
            }
            val pocket_native1 = dataMap["pocket_native"]?.map { it.native_design_type }
            pocket_native1?.forEach {
                pocket_native = it.toInt()
            }
            val pocket_selectsound_bottom1 =
                dataMap["pocket_selectsound_bottom"]?.map { it.native_design_type }
            pocket_selectsound_bottom1?.forEach {
                pocket_selectsound_bottom = it.toInt()
            }
            val password_native1 = dataMap["password_native"]?.map { it.native_design_type }
            password_native1?.forEach {
                password_native = it.toInt()
            }
            val password_selectsound_bottom1 =
                dataMap["password_selectsound_bottom"]?.map { it.native_design_type }
            password_selectsound_bottom1?.forEach {
                password_selectsound_bottom = it.toInt()
            }
            val motion_native1 = dataMap["motion_native"]?.map { it.native_design_type }
            motion_native1?.forEach {
                motion_native = it.toInt()
            }
            val motion_selectsound_bottom1 =
                dataMap["motion_selectsound_bottom"]?.map { it.native_design_type }
            motion_selectsound_bottom1?.forEach {
                motion_selectsound_bottom = it.toInt()
            }
            val whistle_native1 = dataMap["whistle_native"]?.map { it.native_design_type }
            whistle_native1?.forEach {
                whistle_native = it.toInt()
            }
            val whistle_selectsound_bottom1 =
                dataMap["whistle_selectsound_bottom"]?.map { it.native_design_type }
            whistle_selectsound_bottom1?.forEach {
                whistle_selectsound_bottom = it.toInt()
            }
            val handfree_native1 = dataMap["handfree_native"]?.map { it.native_design_type }
            handfree_native1?.forEach {
                handfree_native = it.toInt()
            }
            val handfree_selectsound_bottom1 =
                dataMap["handfree_selectsound_bottom"]?.map { it.native_design_type }
            handfree_selectsound_bottom1?.forEach {
                handfree_selectsound_bottom = it.toInt()
            }
            val clap_native1 = dataMap["clap_native"]?.map { it.native_design_type }
            clap_native1?.forEach {
                clap_native = it.toInt()
            }
            val clap_selectsound_bottom1 =
                dataMap["clap_selectsound_bottom"]?.map { it.native_design_type }
            clap_selectsound_bottom1?.forEach {
                clap_selectsound_bottom = it.toInt()
            }
            val remove_native1 = dataMap["remove_native"]?.map { it.native_design_type }
            remove_native1?.forEach {
                remove_native = it.toInt()
            }
            val remove_selectsound_bottom1 =
                dataMap["remove_selectsound_bottom"]?.map { it.native_design_type }
            remove_selectsound_bottom1?.forEach {
                remove_selectsound_bottom = it.toInt()
            }
            val battery_native1 = dataMap["battery_native"]?.map { it.native_design_type }
            battery_native1?.forEach {
                battery_native = it.toInt()
            }
            val battery_selectsound_bottom1 =
                dataMap["battery_selectsound_bottom"]?.map { it.native_design_type }
            battery_selectsound_bottom1?.forEach {
                battery_selectsound_bottom = it.toInt()
            }
            val language_bottom1 = dataMap["language_bottom"]?.map { it.native_design_type }
            language_bottom1?.forEach {
                language_bottom = it.toInt()
            }
            val languageinapp_scroll1 =
                dataMap["languageinapp_scroll"]?.map { it.native_design_type }
            languageinapp_scroll1?.forEach {
                languageinapp_scroll = it.toInt()
            }
            val splash_bottom1 =
                dataMap["splash_bottom"]?.map { it.native_design_type }
            splash_bottom1?.forEach {
                splash_bottom = it.toInt()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (isInternetDialog) {
            if (!isInternetAvailable(context ?: return)) {
                getIntentMove()
            } else {
                activity?.recreate()
            }
        }
    }
}

