package com.securityalarm.antitheifproject.ui

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.antitheftalarm.dont.touch.phone.finder.phonesecurity.R
import com.antitheftalarm.dont.touch.phone.finder.phonesecurity.databinding.FragmentSplashBinding
import com.bmik.android.sdk.SDKBaseController
import com.bmik.android.sdk.listener.CommonAdsListenerAdapter
import com.bmik.android.sdk.listener.CustomSDKAdsListenerAdapter
import com.bmik.android.sdk.listener.IKRemoteConfigCallback
import com.bmik.android.sdk.model.dto.CommonAdsAction
import com.bmik.android.sdk.model.dto.SdkRemoteConfigDto
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
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
import com.securityalarm.antitheifproject.utilities.inter_frequency_count
import com.securityalarm.antitheifproject.utilities.intruder_native
import com.securityalarm.antitheifproject.utilities.intruderimage_bottom
import com.securityalarm.antitheifproject.utilities.isInternetAvailable
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
import com.securityalarm.antitheifproject.utilities.whistle_native
import com.securityalarm.antitheifproject.utilities.whistle_selectsound_bottom

class SplashFragment :
    BaseFragment<FragmentSplashBinding>(FragmentSplashBinding::inflate) {
    private var isInternetDialog: Boolean = false
    private var dbHelper: DbHelper? = null
    private var mOpenMainAction: (() -> Unit)? = null
    private var timerWaitAds: CountDownTimer? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dbHelper = DbHelper(context ?: return)
        dbHelper?.getStringData(requireContext(), LANG_CODE, "en")?.let {
            setLocaleMain(it)
        }


        if (!isInternetAvailable(context ?: return)) {
            showInternetDialog(
                onPositiveButtonClick = {
                    isInternetDialog = true
                    openMobileDataSettings(context ?: requireContext())
                },
                onNegitiveButtonClick = {
                    isInternetDialog = true
                    openWifiSettings(context ?: requireContext())
                },
                onCloseButtonClick = {
                    getIntentMove()
                }
            )
            return
        }

        inter_frequency_count = 0
        loadRemote()
        loadBanner()
        if (dbHelper?.getBooleanData(
                requireContext(),
                IS_FIRST,
                false
            ) == false
        ) {
            SDKBaseController.getInstance().preloadNativeAd(
                activity ?: return, "language_bottom",
                "language_bottom", object : CustomSDKAdsListenerAdapter() {
                    override fun onAdsLoaded() {
                        super.onAdsLoaded()
                        Log.d("check_ads", "onAdsLoaded: Load Ad")
                    }
                    override fun onAdsLoadFail() {
                        super.onAdsLoadFail()
                        SDKBaseController.getInstance().preloadNativeAd(
                            activity ?: return, "language_bottom",
                            "language_bottom")
                        Log.d("check_ads", "onAdsLoadFail: Load No")
                    }
                }
            )
        } else if (dbHelper?.getBooleanData(
                requireContext(),
                IS_INTRO,
                false
            ) == false
        ) {
            SDKBaseController.getInstance().preloadNativeAd(
                activity ?: return, "onboarding1_bottom",
                "onboarding1_bottom"
            )
            SDKBaseController.getInstance().preloadNativeAd(
                activity ?: return, "onboarding_fullnative",
                "onboarding_fullnative"
            )
            SDKBaseController.getInstance().preloadNativeAd(
                activity ?: return, "onboarding2_bottom",
                "onboarding2_bottom"
            )
            SDKBaseController.getInstance().preloadNativeAd(
                activity ?: return, "onboarding3_bottom",
                "onboarding3_bottom"
            )
        } else {
            SDKBaseController.getInstance().preloadNativeAd(
                activity ?: return, "home_native",
                "home_native"
            )
        }
        setupBackPressedCallback {
            //Do Nothing
        }
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.clear()
    }
    private fun initView() {
        SDKBaseController.getInstance().onDataInitSuccessListener = CommonAdsAction {
            //do something
        }
        SDKBaseController.getInstance().onDataGetSuccessListener = {
            //do something
        }
        mOpenMainAction = {
            getIntentMove()
        }
        timerWaitAds =
            SDKBaseController.getInstance().showFirstOpenAppAds(activity,
                object : CommonAdsListenerAdapter() {
                    override fun onAdsShowed(priority: Int) {
                        super.onAdsShowed(priority)
                        getIntentMove()
                    }

                    override fun onAdsDismiss() {
                    }

                    override fun onAdsShowFail(errorCode: Int) {
                        getIntentMove()
                    }

                    override fun onAdsShowTimeout() {
                        super.onAdsShowTimeout()
                        Toast.makeText(context, "Ads show timeout", Toast.LENGTH_SHORT)
                            .show()
                    }

                })
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
//                        if (IkmSdkUtils.isUserIAPAvailable()) {
                        return findNavController().navigate(
                            R.id.myMainMenuFragment
                        )
//                        } else {
//                            findNavController().navigate(
//                                R.id.FragmentInAppScreen,
//                                bundleOf("Is_From_Splash" to true)
//                            )
//                        }
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
//                            return if (IkmSdkUtils.isUserIAPAvailable()) {
                            return findNavController().navigate(
                                R.id.myMainMenuFragment
                            )
//                            } else {
//                                findNavController().navigate(
//                                    R.id.FragmentInAppScreen,
//                                    bundleOf("Is_From_Splash" to true)
//                                )
//                            }
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
//                    return if (IkmSdkUtils.isUserIAPAvailable()) {
                    return findNavController().navigate(
                        R.id.myMainMenuFragment
                    )
//                    } else {
//                        findNavController().navigate(
//                            R.id.FragmentInAppScreen,
//                            bundleOf("Is_From_Splash" to true)
//                        )
//                    }

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
        _binding?.adsView?.loadAd(
            activity, "bn_s_adw",
            "bn_s_adw", object : CustomSDKAdsListenerAdapter() {
                override fun onAdsLoadFail() {
                    super.onAdsLoadFail()
                    _binding?.adsView?.visibility = View.GONE
                }
            }
        )
    }
    fun loadRemote() {
        SDKBaseController.getInstance().fetchNewRemoteConfigData(object : IKRemoteConfigCallback {
            override fun onSuccess(data: HashMap<String, SdkRemoteConfigDto>) {
                val xx = data
                test_ui_native = xx["test_ui_native"]?.getString().toString()
                language_first_r_scroll = xx["languageinapp_scrollnative"]?.getString().toString()
                language_reload = xx["language_reload"]?.getLong()?.toInt() ?: 0
                Onboarding_Full_Native = xx["Onboarding_Full_Native"]?.getLong()?.toInt() ?: 0
                sessionOpenlanguage = xx["sessionOpenlanguage"]?.getLong()?.toInt() ?: 1
                sessionOnboarding = xx["sessionOnboarding"]?.getLong()?.toInt() ?: 1
                parseJsonWithGson(test_ui_native)
                parseJsonWithGsonLanguage(language_first_r_scroll)
                Log.d("check_language", "onSuccess: $language_reload")
                Log.d("check_language", "onSuccess: $sessionOpenlanguage")
                Log.d("check_language", "onSuccess: $sessionOnboarding")
                Log.d("check_language", "onSuccess: $test_ui_native")
                Log.d("check_language", "onSuccess: $language_first_r_scroll")
                Log.d("check_language", "onSuccess: $Onboarding_Full_Native")
            }
            override fun onFail() {
            }
        })
        initView()
    }
    private fun parseJsonWithGson(jsonString: String) {
        if (jsonString != null && jsonString.isNotEmpty()) {
            val gson = Gson()
            val type = object : TypeToken<Map<String, List<NativeDesignType>>>() {}.type
            val dataMap: Map<String, List<NativeDesignType>> = gson.fromJson(jsonString, type)
            val onboarding1_bottom1 = dataMap["onboarding1_bottom"]?.map { it.native_design_type }
            onboarding1_bottom1?.forEach {
                onboarding1_bottom=it.toInt()
            }
            val onboarding2_bottom1 = dataMap["onboarding2_bottom"]?.map { it.native_design_type }
            onboarding2_bottom1?.forEach {
                onboarding2_bottom=it.toInt()
            }
            val onboarding3_bottom1 = dataMap["onboarding3_bottom"]?.map { it.native_design_type }
            onboarding3_bottom1?.forEach {
                onboarding3_bottom=it.toInt()
            }
            val exitdialog_bottom1 = dataMap["exitdialog_bottom"]?.map { it.native_design_type }
            exitdialog_bottom1?.forEach {
                exitdialog_bottom=it.toInt()
            }
            val thankyou_bottom1 = dataMap["thankyou_bottom"]?.map { it.native_design_type }
            thankyou_bottom1?.forEach {
                thankyou_bottom=it.toInt()
            }
            val home_native1 = dataMap["home_native"]?.map { it.native_design_type }
            home_native1?.forEach {
                home_native=it.toInt()
            }
            val intruder_native1 = dataMap["intruder_native"]?.map { it.native_design_type }
            intruder_native1?.forEach {
                intruder_native=it.toInt()
            }
            val intruderimage_bottom1 = dataMap["intruderimage_bottom"]?.map { it.native_design_type }
            intruderimage_bottom1?.forEach {
                intruderimage_bottom=it.toInt()
            }
            val pocket_native1 = dataMap["pocket_native"]?.map { it.native_design_type }
            pocket_native1?.forEach {
                pocket_native=it.toInt()
            }
            val pocket_selectsound_bottom1 = dataMap["pocket_selectsound_bottom"]?.map { it.native_design_type }
            pocket_selectsound_bottom1?.forEach {
                pocket_selectsound_bottom=it.toInt()
            }
            val password_native1 = dataMap["password_native"]?.map { it.native_design_type }
            password_native1?.forEach {
                password_native=it.toInt()
            }
            val password_selectsound_bottom1 = dataMap["password_selectsound_bottom"]?.map { it.native_design_type }
            password_selectsound_bottom1?.forEach {
                password_selectsound_bottom=it.toInt()
            }
            val motion_native1 = dataMap["motion_native"]?.map { it.native_design_type }
            motion_native1?.forEach {
                motion_native=it.toInt()
            }
            val motion_selectsound_bottom1 = dataMap["motion_selectsound_bottom"]?.map { it.native_design_type }
            motion_selectsound_bottom1?.forEach {
                motion_selectsound_bottom=it.toInt()
            }
            val whistle_native1 = dataMap["whistle_native"]?.map { it.native_design_type }
            whistle_native1?.forEach {
                whistle_native=it.toInt()
            }
            val whistle_selectsound_bottom1 = dataMap["whistle_selectsound_bottom"]?.map { it.native_design_type }
            whistle_selectsound_bottom1?.forEach {
                whistle_selectsound_bottom=it.toInt()
            }
            val handfree_native1 = dataMap["handfree_native"]?.map { it.native_design_type }
            handfree_native1?.forEach {
                handfree_native=it.toInt()
            }
            val handfree_selectsound_bottom1 = dataMap["handfree_selectsound_bottom"]?.map { it.native_design_type }
            handfree_selectsound_bottom1?.forEach {
                handfree_selectsound_bottom=it.toInt()
            }
            val clap_native1 = dataMap["clap_native"]?.map { it.native_design_type }
            clap_native1?.forEach {
                clap_native=it.toInt()
            }
            val clap_selectsound_bottom1 = dataMap["clap_selectsound_bottom"]?.map { it.native_design_type }
            clap_selectsound_bottom1?.forEach {
                clap_selectsound_bottom=it.toInt()
            }
            val remove_native1 = dataMap["remove_native"]?.map { it.native_design_type }
            remove_native1?.forEach {
                remove_native=it.toInt()
            }
            val remove_selectsound_bottom1 = dataMap["remove_selectsound_bottom"]?.map { it.native_design_type }
            remove_selectsound_bottom1?.forEach {
                remove_selectsound_bottom=it.toInt()
            }
            val battery_native1 = dataMap["battery_native"]?.map { it.native_design_type }
            battery_native1?.forEach {
                battery_native=it.toInt()
            }
            val battery_selectsound_bottom1 = dataMap["battery_selectsound_bottom"]?.map { it.native_design_type }
            battery_selectsound_bottom1?.forEach {
                battery_selectsound_bottom=it.toInt()
            }
            val language_bottom1 = dataMap["language_bottom"]?.map { it.native_design_type }
            language_bottom1?.forEach {
                language_bottom=it.toInt()
            }
            val languageinapp_scroll1 = dataMap["languageinapp_scroll"]?.map { it.native_design_type }
            languageinapp_scroll1?.forEach {
                languageinapp_scroll=it.toInt()
            }
        }
    }
    private fun parseJsonWithGsonLanguage(jsonString1: String) {
        if (jsonString1 != null && jsonString1.isNotEmpty()) {
            val gson = Gson()
            val type = object : TypeToken<Map<String, List<AdSettings>>>() {}.type
            val dataMap: Map<String, List<AdSettings>> = gson.fromJson(jsonString1, type)?:return
            val languageInAppScrollList = dataMap["languageinapp_scroll"]
            languageInAppScrollList?.forEach {
                fisrt_ad_line_threshold=it.fisrt_ad_line_threshold.toInt()
                line_count=it.line_count.toInt()
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

