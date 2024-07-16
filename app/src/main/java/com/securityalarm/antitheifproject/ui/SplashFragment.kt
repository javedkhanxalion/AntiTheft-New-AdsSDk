package com.securityalarm.antitheifproject.ui

import android.os.Bundle
import android.os.Debug
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.antitheftalarm.dont.touch.phone.finder.phonesecurity.R
import com.antitheftalarm.dont.touch.phone.finder.phonesecurity.databinding.FragmentSplashBinding
import com.google.android.gms.ads.nativead.NativeAd
import com.google.firebase.FirebaseApp
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.firebase.remoteconfig.ktx.get
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.securityalarm.antitheifproject.ads_manager.AdsBanners
import com.securityalarm.antitheifproject.helper_class.DbHelper
import com.securityalarm.antitheifproject.model.AdSettings
import com.securityalarm.antitheifproject.model.NativeDesignType
import com.securityalarm.antitheifproject.utilities.BaseFragment
import com.securityalarm.antitheifproject.utilities.IS_FIRST
import com.securityalarm.antitheifproject.utilities.IS_INTRO
import com.securityalarm.antitheifproject.utilities.LANG_CODE
import com.securityalarm.antitheifproject.utilities.LANG_SCREEN
import com.securityalarm.antitheifproject.utilities.Onboarding_Full_Native
import com.securityalarm.antitheifproject.utilities.battery_native
import com.securityalarm.antitheifproject.utilities.battery_selectsound_bottom
import com.securityalarm.antitheifproject.utilities.clap_native
import com.securityalarm.antitheifproject.utilities.clap_selectsound_bottom
import com.securityalarm.antitheifproject.utilities.exitdialog_bottom
import com.securityalarm.antitheifproject.utilities.firebaseAnalytics
import com.securityalarm.antitheifproject.utilities.fisrt_ad_line_threshold
import com.securityalarm.antitheifproject.utilities.handfree_native
import com.securityalarm.antitheifproject.utilities.handfree_selectsound_bottom
import com.securityalarm.antitheifproject.utilities.home_native
import com.securityalarm.antitheifproject.utilities.id_banner_1
import com.securityalarm.antitheifproject.utilities.id_exit_dialog_native
import com.securityalarm.antitheifproject.utilities.id_frequency_counter
import com.securityalarm.antitheifproject.utilities.id_inter_counter
import com.securityalarm.antitheifproject.utilities.id_inter_main_medium
import com.securityalarm.antitheifproject.utilities.id_inter_main_normal
import com.securityalarm.antitheifproject.utilities.id_native_Battery_Detection_screen
import com.securityalarm.antitheifproject.utilities.id_native_Motion_Detection_screen
import com.securityalarm.antitheifproject.utilities.id_native_Pocket_Detection_screen
import com.securityalarm.antitheifproject.utilities.id_native_Remove_Charger_screen
import com.securityalarm.antitheifproject.utilities.id_native_Whistle_Detection_screen
import com.securityalarm.antitheifproject.utilities.id_native_app_open_screen
import com.securityalarm.antitheifproject.utilities.id_native_clap_detection_screen
import com.securityalarm.antitheifproject.utilities.id_native_hand_free_screen
import com.securityalarm.antitheifproject.utilities.id_native_intro_screen
import com.securityalarm.antitheifproject.utilities.id_native_intruder_detection_screen
import com.securityalarm.antitheifproject.utilities.id_native_intruder_list_screen
import com.securityalarm.antitheifproject.utilities.id_native_language_screen
import com.securityalarm.antitheifproject.utilities.id_native_loading_screen
import com.securityalarm.antitheifproject.utilities.id_native_main_menu_screen
import com.securityalarm.antitheifproject.utilities.id_native_password_screen
import com.securityalarm.antitheifproject.utilities.id_native_sound_screen
import com.securityalarm.antitheifproject.utilities.intruder_native
import com.securityalarm.antitheifproject.utilities.intruderimage_bottom
import com.securityalarm.antitheifproject.utilities.isInternetAvailable
import com.securityalarm.antitheifproject.utilities.isSplashDialog
import com.securityalarm.antitheifproject.utilities.language_bottom
import com.securityalarm.antitheifproject.utilities.language_first_r_scroll
import com.securityalarm.antitheifproject.utilities.language_reload
import com.securityalarm.antitheifproject.utilities.languageinapp_scroll
import com.securityalarm.antitheifproject.utilities.line_count
import com.securityalarm.antitheifproject.utilities.motion_native
import com.securityalarm.antitheifproject.utilities.motion_selectsound_bottom
import com.securityalarm.antitheifproject.utilities.onboarding1_bottom
import com.securityalarm.antitheifproject.utilities.onboarding2_bottom
import com.securityalarm.antitheifproject.utilities.onboarding3_bottom
import com.securityalarm.antitheifproject.utilities.openMobileDataSettings
import com.securityalarm.antitheifproject.utilities.openWifiSettings
import com.securityalarm.antitheifproject.utilities.password_native
import com.securityalarm.antitheifproject.utilities.password_selectsound_bottom
import com.securityalarm.antitheifproject.utilities.pocket_native
import com.securityalarm.antitheifproject.utilities.pocket_selectsound_bottom
import com.securityalarm.antitheifproject.utilities.remove_native
import com.securityalarm.antitheifproject.utilities.remove_selectsound_bottom
import com.securityalarm.antitheifproject.utilities.sessionOnboarding
import com.securityalarm.antitheifproject.utilities.sessionOpenlanguage
import com.securityalarm.antitheifproject.utilities.setLocaleMain
import com.securityalarm.antitheifproject.utilities.setupBackPressedCallback
import com.securityalarm.antitheifproject.utilities.showInternetDialog
import com.securityalarm.antitheifproject.utilities.test_ui_native
import com.securityalarm.antitheifproject.utilities.thankyou_bottom
import com.securityalarm.antitheifproject.utilities.val_ad_native_Battery_Detection_screen
import com.securityalarm.antitheifproject.utilities.val_ad_native_Motion_Detection_screen
import com.securityalarm.antitheifproject.utilities.val_ad_native_Pocket_Detection_screen
import com.securityalarm.antitheifproject.utilities.val_ad_native_Remove_Charger_screen
import com.securityalarm.antitheifproject.utilities.val_ad_native_Whistle_Detection_screen
import com.securityalarm.antitheifproject.utilities.val_ad_native_clap_detection_screen
import com.securityalarm.antitheifproject.utilities.val_ad_native_hand_free_screen
import com.securityalarm.antitheifproject.utilities.val_ad_native_intro_screen
import com.securityalarm.antitheifproject.utilities.val_ad_native_intruder_detection_screen
import com.securityalarm.antitheifproject.utilities.val_ad_native_intruder_list_screen
import com.securityalarm.antitheifproject.utilities.val_ad_native_language_screen
import com.securityalarm.antitheifproject.utilities.val_ad_native_loading_screen
import com.securityalarm.antitheifproject.utilities.val_ad_native_main_menu_screen
import com.securityalarm.antitheifproject.utilities.val_ad_native_password_screen
import com.securityalarm.antitheifproject.utilities.val_ad_native_show_image_screen
import com.securityalarm.antitheifproject.utilities.val_ad_native_sound_screen
import com.securityalarm.antitheifproject.utilities.val_banner_1
import com.securityalarm.antitheifproject.utilities.val_exit_dialog_native
import com.securityalarm.antitheifproject.utilities.val_inter_main_normal
import com.securityalarm.antitheifproject.utilities.whistle_native
import com.securityalarm.antitheifproject.utilities.whistle_selectsound_bottom
import com.securityalarm.antitheifproject.ads_manager.AdsManager
import com.securityalarm.antitheifproject.ads_manager.AdsManager.isNetworkAvailable
import com.securityalarm.antitheifproject.ads_manager.interfaces.NativeListener
import com.securityalarm.antitheifproject.ads_manager.loadOpenAdSplash
import com.securityalarm.antitheifproject.ads_manager.loadTwoInterAdsSplash
import com.securityalarm.antitheifproject.ads_manager.showNormalInterAdSingle
import com.securityalarm.antitheifproject.ads_manager.showOpenAd
import com.securityalarm.antitheifproject.utilities.counter
import com.securityalarm.antitheifproject.utilities.fisrt_ad_line_threshold_main
import com.securityalarm.antitheifproject.utilities.id_app_open_screen
import com.securityalarm.antitheifproject.utilities.id_banner_language_screen
import com.securityalarm.antitheifproject.utilities.id_banner_main_screen
import com.securityalarm.antitheifproject.utilities.id_banner_splash_screen
import com.securityalarm.antitheifproject.utilities.id_exit_screen_native
import com.securityalarm.antitheifproject.utilities.id_language_scroll_screen_native
import com.securityalarm.antitheifproject.utilities.id_native_Full_screen
import com.securityalarm.antitheifproject.utilities.id_native_intro_screen1
import com.securityalarm.antitheifproject.utilities.id_native_intro_screen2
import com.securityalarm.antitheifproject.utilities.id_native_intro_screen_full
import com.securityalarm.antitheifproject.utilities.inter_frequency_count
import com.securityalarm.antitheifproject.utilities.isSplash
import com.securityalarm.antitheifproject.utilities.line_count_main
import com.securityalarm.antitheifproject.utilities.main_native_scroll
import com.securityalarm.antitheifproject.utilities.val_app_open_main
import com.securityalarm.antitheifproject.utilities.val_banner_language_screen
import com.securityalarm.antitheifproject.utilities.val_banner_main_menu_screen
import com.securityalarm.antitheifproject.utilities.val_banner_splash_screen
import com.securityalarm.antitheifproject.utilities.val_exit_screen_native
import com.securityalarm.antitheifproject.utilities.val_inter_exit_screen
import com.securityalarm.antitheifproject.utilities.val_native_Full_screen
import com.securityalarm.antitheifproject.utilities.val_native_intro_screen
import com.securityalarm.antitheifproject.utilities.val_native_intro_screen1
import com.securityalarm.antitheifproject.utilities.val_native_intro_screen2
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
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dbHelper = DbHelper(context ?: return)
        adsManager = AdsManager.appAdsInit(activity ?: return)
        dbHelper?.getStringData(requireContext(), LANG_CODE, "en")?.let {
            setLocaleMain(it)
        }
        if (!isInternetAvailable(context ?: return)) {
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
            return
        } else {
            isSplashDialog = false
        }
        CoroutineScope(Dispatchers.Main).launch {
            isSplash = false
            counter = 0
            inter_frequency_count = 0
            adsManager = AdsManager.appAdsInit(activity?:return@launch)
            dbHelper = DbHelper(activity?.applicationContext?:return@launch)
            if (isNetworkAvailable(context)) {
                loadTwoInterAdsSplash(
                    adsManager ?: return@launch,
                    activity?:return@launch,
                    remoteConfigNormal = true,
                    adIdNormal = getString(R.string.id_fullscreen_splash),
                    "splash"
                )
                loadBanner()
                delay(5000)
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
                    loadOpenAdSplash(context?:return@launchWhenStarted)
                    delay(5000)
                    showOpenAd(activity ?: return@launchWhenStarted)
                    getIntentMove()
                } else {
                    delay(5000)
                    isSplash = true
                    adsManager?.let {
                        showNormalInterAdSingle(
                            it,
                            activity ?: return@let,
                            remoteConfigNormal = true,
                            adIdNormal = getString(R.string.id_fullscreen_splash),
                            layout = _binding?.adsLayDialog!!,
                            tagClass = "splash"
                        ) {
                        }
                    }
                    getIntentMove()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun getIntentMove() {
        when (sessionOpenlanguage) {
            0 -> {
                when (sessionOnboarding) {
                    0 -> {
                        firebaseAnalytics(
                            "loading_fragment_load_next_btn_main",
                            "loading_fragment_load_next_btn_main -->  Click"
                        )
                        return findNavController().navigate(
                            R.id.myMainMenuFragment
                        )
                    }

                    1 -> {
                        if (dbHelper?.getBooleanData(
                                requireContext(),
                                IS_INTRO,
                                false
                            ) == false
                        ) {
                            firebaseAnalytics(
                                "loading_fragment_load_next_btn_intro",
                                "loading_fragment_load_next_btn_intro -->  Click"
                            )
                            return findNavController().navigate(R.id.OnBordScreenNewScreen)
                        } else {
                            firebaseAnalytics(
                                "loading_fragment_load_next_btn_main",
                                "loading_fragment_load_next_btn_main -->  Click"
                            )
                            return findNavController().navigate(
                                R.id.myMainMenuFragment
                            )
                        }
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
                if ((dbHelper?.getBooleanData(
                        requireContext(),
                        IS_FIRST,
                        false
                    ) == false)
                ) {
                    firebaseAnalytics(
                        "loading_fragment_load_next_btn_language",
                        "loading_fragment_load_next_btn_language -->  Click"
                    )
                    return findNavController().navigate(
                        R.id.LanguageFragment,
                        bundleOf(LANG_SCREEN to true)
                    )
                } else {
                    firebaseAnalytics(
                        "loading_fragment_load_next_btn_main",
                        "loading_fragment_load_next_btn_main -->  Click"
                    )
                    return findNavController().navigate(
                        R.id.myMainMenuFragment
                    )

                }
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
        }
    }

    fun loadBanner() {
        _binding?.adsView?.visibility = View.VISIBLE
        AdsBanners.loadBanner(
            activity = activity?:return,
            view = _binding?.adsView!!,
            viewS = _binding?.shimmerLayout!!,
            addConfig = val_banner_splash_screen,
            bannerId = id_banner_splash_screen,
            bannerListener = {
                _binding?.shimmerLayout!!.visibility=View.GONE
            }
        )
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
                if (task.isSuccessful ) {
                    // Apply the fetched values to your app
                    applyAdIdsFromRemoteConfig(remoteConfig)
                } else {
                    // Handle the error
                    // For example, use default values or log an error message
                }
            }
    }

    private fun applyAdIdsFromRemoteConfig(remoteConfig: FirebaseRemoteConfig) {

        if (!AdsBanners.isDebug()){
            id_banner_1 = remoteConfig.getString("id_banner_1")
            id_banner_main_screen = remoteConfig.getString("id_banner_main_screen")
            id_banner_splash_screen = remoteConfig.getString("id_banner_splash_screen")
        }
            id_inter_counter = remoteConfig.getLong("id_inter_counter").toInt()
            id_frequency_counter = remoteConfig.getLong("id_frequency_counter").toInt()
            id_native_main_menu_screen = remoteConfig.getString("id_native_main_menu_screen")
            id_inter_main_normal = remoteConfig.getString("id_inter_main_normal")
            id_native_loading_screen = remoteConfig.getString("id_native_loading_screen")
            id_native_intro_screen = remoteConfig.getString("id_native_intro_screen")
            id_native_language_screen = remoteConfig.getString("id_native_language_screen")
            id_native_sound_screen = remoteConfig.getString("id_native_sound_screen")
            id_native_intruder_list_screen =remoteConfig.getString("id_native_intruder_list_screen")
            id_native_intruder_detection_screen =remoteConfig.getString("id_native_intruder_detection_screen")
            id_native_password_screen = remoteConfig.getString("id_native_password_screen")
            id_native_Pocket_Detection_screen =remoteConfig.getString("id_native_Pocket_Detection_screen")
            id_native_Motion_Detection_screen =remoteConfig.getString("id_native_Motion_Detection_screen")
            id_native_Whistle_Detection_screen =remoteConfig.getString("id_native_Whistle_Detection_screen")
            id_native_hand_free_screen = remoteConfig.getString("id_native_hand_free_screen")
            id_native_clap_detection_screen =remoteConfig.getString("id_native_clap_detection_screen")
            id_native_Remove_Charger_screen =remoteConfig.getString("id_native_Remove_Charger_screen")
            id_native_Battery_Detection_screen =remoteConfig.getString("id_native_Battery_Detection_screen")
            id_native_app_open_screen = remoteConfig.getString("id_native_app_open_screen")
            id_exit_dialog_native = remoteConfig.getString("id_exit_dialog_native")
            id_exit_screen_native = remoteConfig.getString("id_exit_screen_native")

            id_native_intro_screen1 = remoteConfig.getString("id_native_intro_screen1")
            id_native_intro_screen2 = remoteConfig.getString("id_native_intro_screen2")
            id_app_open_screen = remoteConfig.getString("id_app_open_screen")
            id_native_intro_screen_full = remoteConfig.getString("id_native_intro_screen_full")
            id_language_scroll_screen_native =remoteConfig.getString("id_language_scroll_screen_native")
            id_native_Full_screen = remoteConfig.getString("id_native_Full_screen")

            test_ui_native = remoteConfig.getString("test_ui_native")
            language_first_r_scroll = remoteConfig.getString("language_first_r_scroll")
            language_reload = remoteConfig.getString("language_reload").toInt()
            Onboarding_Full_Native = remoteConfig.getString("Onboarding_Full_Native").toInt()
            sessionOpenlanguage = remoteConfig.getString("sessionOpenlanguage").toInt()
            sessionOnboarding = remoteConfig.getString("sessionOnboarding").toInt()
            parseJsonWithGson(test_ui_native)
            parseJsonWithGsonLanguage(language_first_r_scroll)
            parseJsonWithGsonMain(remoteConfig.getString("main_first_scroll"))

            Log.d("check_language", "onSuccess: $language_reload")
            Log.d("check_language", "onSuccess: $sessionOpenlanguage")
            Log.d("check_language", "onSuccess: $sessionOnboarding")
            Log.d("check_language", "onSuccess: $test_ui_native")
            Log.d("check_language", "onSuccess: $language_first_r_scroll")
            Log.d("check_language", "onSuccess: $Onboarding_Full_Native")
            Log.d("remote_ids", "$id_inter_counter")
            Log.d("remote_ids", "$id_frequency_counter")
            Log.d("remote_ids", id_inter_main_medium)
            Log.d("remote_ids", id_native_main_menu_screen)
            Log.d("remote_ids", id_inter_main_normal)
            Log.d("remote_ids", id_native_loading_screen)
            Log.d("remote_ids", id_native_loading_screen)
            Log.d("remote_ids", id_native_loading_screen)
            Log.d("remote_ids", id_native_intro_screen)
            Log.d("remote_ids", id_native_language_screen)
            Log.d("remote_ids", id_native_sound_screen)
            Log.d("remote_ids", id_native_intruder_list_screen)
            Log.d("remote_ids", id_native_intruder_detection_screen)
            Log.d("remote_ids", id_native_password_screen)
            Log.d("remote_ids", id_native_Pocket_Detection_screen)
            Log.d("remote_ids", id_native_Motion_Detection_screen)
            Log.d("remote_ids", id_native_Whistle_Detection_screen)
            Log.d("remote_ids", id_native_hand_free_screen)
            Log.d("remote_ids", id_native_clap_detection_screen)
            Log.d("remote_ids", id_native_Remove_Charger_screen)
            Log.d("remote_ids", id_native_Battery_Detection_screen)
            Log.d("remote_ids", id_exit_dialog_native)
            Log.d("remote_ids", id_banner_1)
        initRemoteConfig()
    }

    private fun parseJsonWithGsonMain(jsonString1: String) {
        Log.d("remote_ids", "$jsonString1")
        if (jsonString1 != null && jsonString1.isNotEmpty()) {
            val gson = Gson()
            val type = object : TypeToken<Map<String, List<AdSettings>>>() {}.type
            val dataMap: Map<String, List<AdSettings>> = gson.fromJson(jsonString1, type) ?: return
            val mainScrollList = dataMap["main_scroll"]
            mainScrollList?.forEach {
                main_native_scroll = it.status.toInt()
                fisrt_ad_line_threshold_main = it.fisrt_ad_line_threshold.toInt()
                line_count_main = it.line_count.toInt()
            }
        }
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
                    val_ad_native_main_menu_screen =remoteConfig!!["val_ad_native_main_menu_screen"].asBoolean()
                    val_ad_native_loading_screen =remoteConfig!!["val_native_loading_screen"].asBoolean()
                    val_ad_native_intro_screen =remoteConfig!!["val_ad_native_intro_screen"].asBoolean()
                    val_ad_native_language_screen =remoteConfig!!["val_ad_native_language_screen"].asBoolean()
                    val_ad_native_sound_screen =remoteConfig!!["val_ad_native_sound_screen"].asBoolean()
                    val_ad_native_intruder_list_screen =remoteConfig!!["val_ad_native_intruder_list_screen"].asBoolean()
                    val_ad_native_show_image_screen =remoteConfig!!["val_ad_native_show_image_screen"].asBoolean()
                    val_ad_native_intruder_detection_screen =remoteConfig!!["val_ad_native_intruder_detection_screen"].asBoolean()
                    val_ad_native_password_screen =remoteConfig!!["val_ad_native_password_screen"].asBoolean()
                    val_ad_native_Motion_Detection_screen =remoteConfig!!["val_ad_native_Motion_Detection_screen"].asBoolean()
                    val_ad_native_Whistle_Detection_screen =remoteConfig!!["val_ad_native_Whistle_Detection_screen"].asBoolean()
                    val_ad_native_hand_free_screen =remoteConfig!!["val_ad_native_hand_free_screen"].asBoolean()
                    val_ad_native_clap_detection_screen =remoteConfig!!["val_ad_native_clap_detection_screen"].asBoolean()
                    val_ad_native_Remove_Charger_screen =remoteConfig!!["val_ad_native_Remove_Charger_screen"].asBoolean()
                    val_ad_native_Battery_Detection_screen =remoteConfig!!["val_ad_native_Battery_Detection_screen"].asBoolean()
                    val_ad_native_Pocket_Detection_screen =remoteConfig!!["val_ad_native_Pocket_Detection_screen"].asBoolean()

                    val_inter_exit_screen = remoteConfig!!["val_inter_exit_screen"].asBoolean()
                    val_exit_dialog_native = remoteConfig!!["val_exit_dialog_native"].asBoolean()
                    val_exit_screen_native = remoteConfig!!["val_exit_screen_native"].asBoolean()

                    val_banner_splash_screen = remoteConfig!!["val_banner_splash_screen"].asBoolean()
                    val_banner_language_screen = remoteConfig!!["val_banner_language_screen"].asBoolean()
                    val_banner_main_menu_screen = remoteConfig!!["val_banner_main_menu_screen"].asBoolean()

                    val_native_Full_screen = remoteConfig!!["val_native_Full_screen"].asBoolean()
                    val_native_intro_screen = remoteConfig!!["val_native_intro_screen"].asBoolean()
                    val_native_intro_screen1 = remoteConfig!!["val_native_intro_screen1"].asBoolean()
                    val_native_intro_screen2 = remoteConfig!!["val_native_intro_screen2"].asBoolean()
                    val_app_open_main = remoteConfig!!["val_app_open_main"].asBoolean()


                } else {
                    Log.d("RemoteConfig", "Fetch failed")
                }
                adsManager?.nativeAdsMain()?.loadNativeAd(
                    activity ?: return@addOnCompleteListener,
                    true,
                    id_native_main_menu_screen,
                    object : NativeListener {

                    }
                )
                observeSplashLiveData()
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun parseJsonWithGson(jsonString: String) {
        if (jsonString != null && jsonString.isNotEmpty()) {
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
        }
    }


    private fun parseJsonWithGsonLanguage(jsonString1: String) {
        if (jsonString1 != null && jsonString1.isNotEmpty()) {
            val gson = Gson()
            val type = object : TypeToken<Map<String, List<AdSettings>>>() {}.type
            val dataMap: Map<String, List<AdSettings>> = gson.fromJson(jsonString1, type) ?: return
            val languageInAppScrollList = dataMap["languageinapp_scroll"]
            languageInAppScrollList?.forEach {
                fisrt_ad_line_threshold = it.fisrt_ad_line_threshold.toInt()
                line_count = it.line_count.toInt()
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

