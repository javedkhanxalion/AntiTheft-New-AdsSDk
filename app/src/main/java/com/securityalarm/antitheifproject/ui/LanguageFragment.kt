package com.securityalarm.antitheifproject.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.core.app.ActivityCompat.recreate
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.antitheftalarm.dont.touch.phone.finder.phonesecurity.R
import com.antitheftalarm.dont.touch.phone.finder.phonesecurity.databinding.FragmentLanguageBinding
import com.bmik.android.sdk.IkmSdkController
import com.bmik.android.sdk.SDKBaseController
import com.bmik.android.sdk.listener.CustomSDKAdsListenerAdapter
import com.bmik.android.sdk.widgets.IkmWidgetAdLayout
import com.securityalarm.antitheifproject.adapter.LanguageGridAdapter
import com.securityalarm.antitheifproject.adapter.LanguageGridAdapter.Companion.AD_TYPE
import com.securityalarm.antitheifproject.helper_class.DbHelper
import com.securityalarm.antitheifproject.model.LanguageAppModel
import com.securityalarm.antitheifproject.utilities.BaseFragment
import com.securityalarm.antitheifproject.utilities.IS_FIRST
import com.securityalarm.antitheifproject.utilities.IS_INTRO
import com.securityalarm.antitheifproject.utilities.LANG_CODE
import com.securityalarm.antitheifproject.utilities.LANG_SCREEN
import com.securityalarm.antitheifproject.utilities.clickWithThrottle
import com.securityalarm.antitheifproject.utilities.firebaseAnalytics
import com.securityalarm.antitheifproject.utilities.fisrt_ad_line_threshold
import com.securityalarm.antitheifproject.utilities.getNativeLayout
import com.securityalarm.antitheifproject.utilities.getNativeLayoutShimmer
import com.securityalarm.antitheifproject.utilities.isInternetAvailable
import com.securityalarm.antitheifproject.utilities.language_bottom
import com.securityalarm.antitheifproject.utilities.language_reload
import com.securityalarm.antitheifproject.utilities.line_count
import com.securityalarm.antitheifproject.utilities.openMobileDataSettings
import com.securityalarm.antitheifproject.utilities.openWifiSettings
import com.securityalarm.antitheifproject.utilities.restartApp
import com.securityalarm.antitheifproject.utilities.sessionOnboarding
import com.securityalarm.antitheifproject.utilities.setLocaleMain
import com.securityalarm.antitheifproject.utilities.setupBackPressedCallback


class LanguageFragment : BaseFragment<FragmentLanguageBinding>(FragmentLanguageBinding::inflate) {
    private var positionSelected: String = "en"
    private var isLangScreen: Boolean = false
    private var isValue: Int = 0
    private var recallActive: Int = 1
    private var sharedPrefUtils: DbHelper? = null
    private var adapter: LanguageGridAdapter? = null
    var list: ArrayList<LanguageAppModel> = ArrayList()
    private var isInternetDialog: Boolean = false
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            firebaseAnalytics("language_fragment_open", "language_fragment_open -->  Click")
            requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
            initializeData()
            arguments?.let {
                isLangScreen = it.getBoolean(LANG_SCREEN)
            }
            if (isInternetAvailable(context ?: return) && !isLangScreen) {
                insertAds()
            }
            if (isLangScreen) {
                loadNative()
                SDKBaseController.getInstance().preloadNativeAd(
                    activity ?: return, "onboarding1_bottom",
                    "onboarding1_bottom",
                    null
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
                _binding?.backBtn?.visibility = View.GONE
            } else {
                loadBanner()
            }
            _binding?.forwardBtn?.clickWithThrottle {
                sharedPrefUtils?.saveData(requireContext(), IS_FIRST, true)
                if (!isLangScreen) {
                    firebaseAnalytics(
                        "language_fragment_forward_btn_from",
                        "language_fragment_forward_btn_from -->  Click"
                    )
                    sharedPrefUtils?.saveData(requireContext(), LANG_CODE, positionSelected) ?: "en"
                    setLocaleMain(positionSelected)
                    restartApp()
                } else {
                    firebaseAnalytics(
                        "language_fragment_forward_btn_from",
                        "language_fragment_forward_btn_from -->  Click"
                    )
                    sharedPrefUtils?.saveData(requireContext(), LANG_CODE, positionSelected) ?: "en"
                    setLocaleMain(positionSelected)
                    sharedPrefUtils?.saveData(requireContext(), IS_FIRST, true)

                    when (sessionOnboarding) {
                        0 -> {
                            firebaseAnalytics(
                                "loading_fragment_load_next_btn_main",
                                "loading_fragment_load_next_btn_main -->  Click"
                            )
                            findNavController().navigate(
                                R.id.myMainMenuFragment
                            )
                        }

                        1 -> {
                            if (sharedPrefUtils?.getBooleanData(
                                    requireContext(),
                                    IS_INTRO,
                                    false
                                ) == false
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
//                                 if(IkmSdkUtils.isUserIAPAvailable()){
                                findNavController().navigate(
                                    R.id.myMainMenuFragment
                                )
//                                }else{
//                                    findNavController().navigate(
//                                        R.id.FragmentInAppScreen,
//                                        bundleOf("Is_From_Splash" to true)
//                                    )
//                                }
                            }
                        }

                        2 -> {
                            firebaseAnalytics(
                                "loading_fragment_load_next_btn_intro",
                                "loading_fragment_load_next_btn_intro -->  Click"
                            )
                            findNavController().navigate(R.id.OnBordScreenNewScreen)
                        }
                    }

                }
            }
            sharedPrefUtils = DbHelper(context ?: return)
            positionSelected =
                sharedPrefUtils?.getStringData(requireContext(), LANG_CODE, "en") ?: "en"
            adapter = LanguageGridAdapter(list,
                clickItem = {
                    positionSelected = it.country_code
                    adapter?.selectLanguage(positionSelected)
                    if (isLangScreen) {
                        reCall()
                    }
                }
            )
            adapter?.selectLanguage(positionSelected)
            _binding?.conversationDetail?.layoutManager = GridLayoutManager(context, 2).apply {
                spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                    override fun getSpanSize(position: Int): Int {
                        return if (adapter?.getItemViewType(position) == AD_TYPE) 2 else 1
                    }
                }
            }
            _binding?.conversationDetail?.adapter = adapter
            _binding?.backBtn?.clickWithThrottle {
                if (!isLangScreen) {
                    firebaseAnalytics(
                        "language_fragment_back_press",
                        "language_fragment_back_press -->  Click"
                    )
                    findNavController().popBackStack()
                }
            }
            setupBackPressedCallback {
                if (!isLangScreen) {
                    firebaseAnalytics(
                        "language_fragment_back_press",
                        "language_fragment_back_press -->  Click"
                    )
                    findNavController().popBackStack()
                } else {
                    firebaseAnalytics(
                        "language_fragment_forward_btn_from",
                        "language_fragment_forward_btn_from -->  Click"
                    )
                    sharedPrefUtils?.saveData(requireContext(), LANG_CODE, positionSelected) ?: "en"
                    setLocaleMain(positionSelected)
                    sharedPrefUtils?.saveData(requireContext(), IS_FIRST, true)
                    findNavController().navigate(
                        R.id.OnBordScreenNewScreen
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    private fun loadNative() {
        _binding?.mainAdsNative?.visibility = View.VISIBLE
        val adLayout = LayoutInflater.from(context).inflate(
            getNativeLayout(language_bottom,_binding?.mainAdsNative!!,context?:return),
            null, false
        ) as? IkmWidgetAdLayout
        adLayout?.titleView = adLayout?.findViewById(R.id.custom_headline)
        adLayout?.bodyView = adLayout?.findViewById(R.id.custom_body)
        adLayout?.callToActionView = adLayout?.findViewById(R.id.custom_call_to_action)
        adLayout?.iconView = adLayout?.findViewById(R.id.custom_app_icon)
        adLayout?.mediaView = adLayout?.findViewById(R.id.custom_media)
        _binding?.mainAdsNative?.loadAd(
            activity ?: return,  getNativeLayoutShimmer(language_bottom),
            adLayout!!, "language_bottom",
            "language_bottom", object : CustomSDKAdsListenerAdapter() {

                override fun onAdsLoaded() {
                    super.onAdsLoaded()
                        Log.d("check_ads", "onAdsLoadFail: Load Yes")
                    _binding?.mainAdsNative?.visibility = View.VISIBLE
                }

                override fun onAdsLoadFail() {
                    super.onAdsLoadFail()
                    Log.d("check_ads", "onAdsLoadFail: Load No")
                    _binding?.mainAdsNative?.visibility = View.GONE
                }
            }
        )
    }
    private fun reCall() {
        when (language_reload) {
            1 -> {
                _binding?.mainAdsNative?.reCallLoadAd()
            }

            2 -> {
                if (recallActive == 2) {
                    _binding?.mainAdsNative?.reCallLoadAd()
                    recallActive = 0
                } else {
                    recallActive += 1
                }
            }
        }
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.clear()
    }
    fun loadBanner() {
        _binding?.adsView?.visibility = View.VISIBLE
        _binding?.adsView?.loadAd(
            activity, "languageinapp_banner",
            "languageinapp_banner", object : CustomSDKAdsListenerAdapter() {

                override fun onAdsLoadFail() {
                    super.onAdsLoadFail()
                    _binding?.adsView?.visibility = View.GONE
                }
            }
        )

    }
    private fun initializeData() {
        list.add(LanguageAppModel(getString(R.string.english), "en", R.drawable.usa, false))
        list.add(LanguageAppModel(getString(R.string.spanish), "es", R.drawable.spain, false))
        list.add(LanguageAppModel(getString(R.string.hindi), "hi", R.drawable.india, false))
        list.add(LanguageAppModel(getString(R.string.arabic), "ar", R.drawable.sudi, false))
        list.add(LanguageAppModel(getString(R.string.french), "fr", R.drawable.france, false))
        list.add(LanguageAppModel(getString(R.string.german), "de", R.drawable.germany, false))
        list.add(LanguageAppModel(getString(R.string.japanese), "ja", R.drawable.japan, false))
        list.add(LanguageAppModel(getString(R.string.dutch), "nl", R.drawable.dutch, false))
        list.add(LanguageAppModel(getString(R.string.korean), "ko", R.drawable.japanese, false))
        list.add(
            LanguageAppModel(
                getString(R.string.portuguese),
                "pt",
                R.drawable.portuguese,
                false
            )
        )
        list.add(LanguageAppModel(getString(R.string.chinese), "zh", R.drawable.chinese, false))
        list.add(LanguageAppModel(getString(R.string.italian), "it", R.drawable.italian, false))
        list.add(LanguageAppModel(getString(R.string.russian), "ru", R.drawable.russian, false))
        list.add(LanguageAppModel(getString(R.string.turkey), "tr", R.drawable.turkey, false))
        list.add(
            LanguageAppModel(
                getString(R.string.vietnamese),
                "vi",
                R.drawable.vietnamese,
                false
            )
        )
        list.add(LanguageAppModel(getString(R.string.thai), "th", R.drawable.ukrainian, false))
        list.add(
            LanguageAppModel(
                getString(R.string.indonesian),
                "id",
                R.drawable.indonesian,
                false
            )
        )
    }
    private fun insertAds() {
        val adPosition = getFirstAdPosition(fisrt_ad_line_threshold)
        val repeatAdPosition = getRepeatAdPosition(line_count)
        var currentPos = adPosition
        while (currentPos < list.size) {
            list.add(currentPos, LanguageAppModel("Ad", "", 0, false))
            currentPos += repeatAdPosition
        }
    }
    private fun getFirstAdPosition(position: Int): Int {
        when (position) {
            1 -> {
                return 2
            }

            2 -> {
                return 4
            }

            3 -> {
                return 6
            }

            4 -> {
                return 8
            }

            5 -> {
                return 10
            }
        }
        return 2
    }
    private fun getRepeatAdPosition(position: Int): Int {
        when (position) {
            1 -> {
                return 3
            }

            2 -> {
                return 5
            }

            3 -> {
                return 7
            }

            4 -> {
                return 9
            }

            5 -> {
                return 11
            }
        }
        return 2
    }
    override fun onPause() {
        super.onPause()
        isInternetDialog=true
        if (!isInternetAvailable(context ?: return)) {
            IkmSdkController.setEnableShowResumeAds(false)
        }
    }
    override fun onResume() {
        super.onResume()
        if (isInternetDialog) {
            if (!isInternetAvailable(context ?: return)) {
                IkmSdkController.setEnableShowResumeAds(false)
              /*  showInternetDialog(
                    onPositiveButtonClick = {
                        isInternetDialog = true
                        openMobileDataSettings(context ?: requireContext())
                    },
                    onNegitiveButtonClick = {
                        isInternetDialog = true
                        openWifiSettings(context ?: requireContext())
                    },
                    onCloseButtonClick = {
                    }
                )*/
                return
            }else{
                IkmSdkController.setEnableShowResumeAds(true)
            }
        }
    }
}