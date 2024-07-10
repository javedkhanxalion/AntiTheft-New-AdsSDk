package com.securityalarm.antitheifproject.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.antitheftalarm.dont.touch.phone.finder.phonesecurity.R
import com.antitheftalarm.dont.touch.phone.finder.phonesecurity.databinding.FragmentLanguageBinding
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.securityalarm.antitheifproject.adapter.LanguageGridAdapter
import com.securityalarm.antitheifproject.adapter.LanguageGridAdapter.Companion.AD_TYPE
import com.securityalarm.antitheifproject.ads_manager.AdsBanners
import com.securityalarm.antitheifproject.ads_manager.AdsManager
import com.securityalarm.antitheifproject.ads_manager.interfaces.NativeListener
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
import com.securityalarm.antitheifproject.utilities.id_banner_language_screen
import com.securityalarm.antitheifproject.utilities.id_native_language_screen
import com.securityalarm.antitheifproject.utilities.isInternetAvailable
import com.securityalarm.antitheifproject.utilities.language_bottom
import com.securityalarm.antitheifproject.utilities.language_reload
import com.securityalarm.antitheifproject.utilities.line_count
import com.securityalarm.antitheifproject.utilities.restartApp
import com.securityalarm.antitheifproject.utilities.sessionOnboarding
import com.securityalarm.antitheifproject.utilities.setLocaleMain
import com.securityalarm.antitheifproject.utilities.setupBackPressedCallback
import com.securityalarm.antitheifproject.utilities.val_ad_native_language_screen
import com.securityalarm.antitheifproject.utilities.val_banner_language_screen


class LanguageFragment : BaseFragment<FragmentLanguageBinding>(FragmentLanguageBinding::inflate) {
    private var positionSelected: String = "en"
    private var isLangScreen: Boolean = false
    private var isValue: Int = 0
    private var recallActive: Int = 1
    private var sharedPrefUtils: DbHelper? = null
    private var adapter: LanguageGridAdapter? = null
    var list: ArrayList<LanguageAppModel> = ArrayList()
    private var isInternetDialog: Boolean = false
    private var adsManager: AdsManager? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            firebaseAnalytics("language_fragment_open", "language_fragment_open -->  Click")
            requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
            initializeData()
            adsManager = AdsManager.appAdsInit(activity ?: return)
            arguments?.let {
                isLangScreen = it.getBoolean(LANG_SCREEN)
            }
            if (isInternetAvailable(context ?: return) && !isLangScreen) {
                insertAds()
            }
            if (isLangScreen) {
                loadNative()
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
        val adView = LayoutInflater.from(context).inflate(
            getNativeLayout(language_bottom, _binding?.mainAdsNative!!, context ?: return),
            null, false
        ) as NativeAdView
        adsManager?.nativeAdsMain()?.loadNativeAd(
            activity ?: return,
            val_ad_native_language_screen,
            id_native_language_screen,
            object : NativeListener {
                override fun nativeAdLoaded(currentNativeAd: NativeAd?) {
                    _binding?.mainAdsNative?.visibility = View.VISIBLE
                    _binding?.shimmerLayout?.visibility = View.GONE
                    adsManager?.nativeAdsMain()?.nativeViewMedia(currentNativeAd ?: return, adView)
                    _binding?.mainAdsNative?.removeAllViews()
                    _binding?.mainAdsNative?.addView(adView)
                    super.nativeAdLoaded(currentNativeAd)
                }

                override fun nativeAdFailed(loadAdError: LoadAdError) {
                    _binding?.mainAdsNative?.visibility = View.GONE
                    _binding?.shimmerLayout?.visibility = View.GONE
                    super.nativeAdFailed(loadAdError)
                }

                override fun nativeAdValidate(string: String) {
                    _binding?.mainAdsNative?.visibility = View.GONE
                    _binding?.shimmerLayout?.visibility = View.GONE
                    super.nativeAdValidate(string)
                }
            })

    }

    private fun reCall() {
        when (language_reload) {
            1 -> {
                loadNative()
            }

            2 -> {
                if (recallActive == 2) {
                    loadNative()
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
        AdsBanners.loadBanner(
            activity = activity?:return,
            view = _binding?.adsView!!,
            addConfig = val_banner_language_screen,
            bannerId = id_banner_language_screen,
            bannerListener = {
                _binding?.shimmerLayout?.visibility = View.GONE
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
    }

    override fun onResume() {
        super.onResume()
    }
}